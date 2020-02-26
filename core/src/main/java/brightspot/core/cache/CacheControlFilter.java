package brightspot.core.cache;

import java.util.Arrays;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import brightspot.core.requestextras.responseheader.ExtraResponseHeaderFilter;
import com.psddev.auth.AuthenticationFilter;
import com.psddev.cms.db.PageFilter;
import com.psddev.cms.db.SiteSettings;
import com.psddev.dari.util.AbstractFilter;
import com.psddev.dari.util.StringUtils;
import com.psddev.dari.util.WebPageContext;

/**
 * An {@link AbstractFilter} which prevents access to request parameters and cookies when a given response may be
 * cached.
 */
public class CacheControlFilter extends AbstractFilter implements AbstractFilter.Auto {

    private static final String SKIP_CACHE_CONTROL_PARAMETER = "_skipCacheControl";

    @Override
    protected void doRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws Exception {

        // Temporary toggle
        if (Boolean.FALSE.equals(SiteSettings.get(
            PageFilter.Static.getSite(request),
            site -> site.as(CacheControlSettings.class).isEnableCacheControl()))) {
            chain.doFilter(request, response);
            return;
        }

        // Skips logic when in Preview or no main object is present
        if (PageFilter.Static.getMainObject(request) == null
            || PageFilter.Static.isPreview(request)
            || com.psddev.cms.tool.AuthenticationFilter.Static.getInsecureToolUser(request) != null) {
            chain.doFilter(request, response);
            return;
        }

        WebPageContext page = new WebPageContext((ServletContext) null, request, response);
        Boolean skipCacheControl = page.param(Boolean.class, SKIP_CACHE_CONTROL_PARAMETER);

        // If response may be cached and there is no request param to skip cache control,
        // prevent access to params and cookies. Otherwise, if request is not intended
        // to skip cache control behavior, fall back to default rendering pipeline.
        if (isResponseCached(request, response) && !Boolean.TRUE.equals(skipCacheControl)) {
            chain.doFilter(new CacheControlRequestWrapper(request), response);
            return;
        } else if (!Boolean.TRUE.equals(skipCacheControl)) {
            chain.doFilter(request, response);
            return;
        }

        // TODO: the below functionality will be moved to a separate filter, with partial rendering behaviors.
        // At this point, the response should never be cached
        response.setHeader("Cache-Control", "private");

        // Currently, there is only one scenario where CacheControlFilter
        // does not need to return any updates to the cached response. There
        // may be more in the future.
        if (AuthenticationFilter.getAuthenticatedEntity(request) == null) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * Runs before PageFilter and AuthenticationFilter.
     */
    @Override
    public void updateDependencies(
        Class<? extends AbstractFilter> filterClass,
        List<Class<? extends Filter>> dependencies) {
        if (PageFilter.class.isAssignableFrom(filterClass)
            || AuthenticationFilter.class.isAssignableFrom(filterClass)) {
            dependencies.add(getClass());
        }
    }

    /**
     * Runs after com.psddev.cms.tool.AuthenticationFilter
     */
    @Override
    protected Iterable<Class<? extends Filter>> dependencies() {
        return Arrays.asList(ExtraResponseHeaderFilter.class, com.psddev.cms.tool.AuthenticationFilter.class);
    }

    private static final List<String> PREVENT_CACHE_DIRECTIVES = Arrays.asList(
        "private",
        "no-cache",
        "no-store",
        "max-age: 0"
    );

    /**
     * Checks if a given {@link HttpServletResponse response} contains any of the {@code PREVENT_CACHE_DIRECTIVES}.
     *
     * @return {@code true} if the response may be cached
     */
    static boolean isResponseCached(HttpServletRequest request, HttpServletResponse response) {
        String cacheControl = response.getHeader("Cache-Control");
        return !StringUtils.isBlank(cacheControl)
            && PREVENT_CACHE_DIRECTIVES.stream().noneMatch(cacheControl::contains);
    }
}
