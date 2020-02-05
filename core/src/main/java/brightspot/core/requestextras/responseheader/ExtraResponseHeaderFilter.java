package brightspot.core.requestextras.responseheader;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import brightspot.core.requestextras.RequestExtras;
import com.psddev.dari.util.AbstractFilter;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.RoutingFilter;
import com.psddev.dari.util.StringUtils;

/**
 * Add {@link CustomResponseHeaders} to a matching request.
 */
public class ExtraResponseHeaderFilter extends AbstractFilter implements AbstractFilter.Auto {

    private static final String PROCESSED_ATTRIBUTE = ExtraResponseHeaderFilter.class.getName() + ".processed";

    @Override
    protected void doRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws Exception {

        if (ObjectUtils.to(boolean.class, request.getAttribute(PROCESSED_ATTRIBUTE))) {
            chain.doFilter(request, response);
            return;
        }

        try {

            Map<String, String> headers = new LinkedHashMap<>();
            for (CustomResponseHeaders customResponseHeaders : RequestExtras.getRequestExtras(request)
                .getByClass(CustomResponseHeaders.class)) {
                for (ResponseHeader responseHeader : customResponseHeaders.getResponseHeaders()) {
                    headers.put(responseHeader.getHeaderName(), responseHeader.getHeaderValue());
                }
            }

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (!StringUtils.isBlank(key) && !StringUtils.isBlank(value)) {
                    response.addHeader(entry.getKey(), entry.getValue());
                }
            }

        } finally {

            request.setAttribute(PROCESSED_ATTRIBUTE, true);
            chain.doFilter(request, response);
        }
    }

    @Override
    public void updateDependencies(
        Class<? extends AbstractFilter> filterClass,
        List<Class<? extends Filter>> dependencies) {
        if (RoutingFilter.class.equals(filterClass)) {
            dependencies.add(getClass());
        }
    }
}
