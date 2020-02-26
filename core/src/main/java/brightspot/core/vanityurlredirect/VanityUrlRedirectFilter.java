package brightspot.core.vanityurlredirect;

import java.util.Arrays;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import brightspot.core.requestextras.responseheader.ExtraResponseHeaderFilter;
import com.psddev.cms.db.PageFilter;
import com.psddev.dari.util.AbstractFilter;
import com.psddev.dari.util.StringUtils;

/**
 * Class that handles redirection by extrapolating information from VanityUrlRedirect class, and sends the appropriate
 * HTTPServletResponse.
 */
public class VanityUrlRedirectFilter extends AbstractFilter implements AbstractFilter.Auto {

    /**
     * @param request provides currentUrl which aligns with localUrls field of the VanityUrlRedirect class.
     * @param response is altered depending on temporary and destination fields of the redirect object. Using
     * information from the Vanity Redirect object, this method redirects to the appropriate destination and sets the
     * redirect code to 302 if the temporary boolean is set, and 301 otherwise.
     */
    @Override
    protected void doRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws Exception {
        Object mainObject = PageFilter.Static.getMainObject(request);

        if (mainObject instanceof VanityUrlRedirect) {
            VanityUrlRedirect vanityUrlRedirect = (VanityUrlRedirect) mainObject;

            String redirectUrl = vanityUrlRedirect.getDestination();
            String currentUrl = request.getRequestURL().toString();

            if (!StringUtils.isBlank(redirectUrl) && !redirectUrl.equalsIgnoreCase(currentUrl)) {
                if (vanityUrlRedirect.isTemporary()) {
                    response.sendRedirect(redirectUrl);
                } else {
                    response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                    response.setHeader("Location", redirectUrl);
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    protected Iterable<Class<? extends Filter>> dependencies() {
        return Arrays.asList(ExtraResponseHeaderFilter.class);
    }

    @Override
    public void updateDependencies(Class<? extends AbstractFilter> aClass, List<Class<? extends Filter>> list) {
        if (PageFilter.class.isAssignableFrom(aClass)) {
            list.add(getClass());
        }
    }
}
