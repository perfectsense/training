package brightspot.core.site;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.psddev.cms.db.PageFilter;
import com.psddev.dari.util.AbstractFilter;

/**
 * Display not found and server error pages See: {@link ErrorHttpServletResponseWrapper}.
 */
public class ErrorFilter extends AbstractFilter implements AbstractFilter.Auto {

    @Override
    protected void doRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws Exception {
        Set<ErrorHandler> handlers = Optional.ofNullable(
            FrontEndSettings.get(PageFilter.Static.getSite(request), FrontEndSettings::getErrorHandlers))
            .orElseGet(Collections::emptySet);
        try {
            chain.doFilter(request, new ErrorHttpServletResponseWrapper(request, response, handlers));
        } catch (Exception e) {
            // Try all until one returns true, then stop.
            boolean handlerFound = false;
            for (ErrorHandler handler : handlers) {
                if (handler.handleException(request, response, e)) {
                    handlerFound = true;
                    break;
                }
            }
            if (!handlerFound) {
                throw e;
            }
        }
    }

    @Override
    public void updateDependencies(
        Class<? extends AbstractFilter> filterClass,
        List<Class<? extends Filter>> dependencies) {
        if (PageFilter.class.isAssignableFrom(filterClass)) {
            dependencies.add(0, getClass());
        }
    }
}
