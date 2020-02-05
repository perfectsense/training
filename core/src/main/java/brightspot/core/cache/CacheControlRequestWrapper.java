package brightspot.core.cache;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * An {@link HttpServletRequestWrapper} which removes the ability to access certain request properties.
 *
 * Parameters beginning with "_" will remain available.
 */
public class CacheControlRequestWrapper extends HttpServletRequestWrapper {

    CacheControlRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public Cookie[] getCookies() {
        return null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return super.getParameterMap().entrySet().stream()
            .filter(entry -> entry.getKey() != null && entry.getKey().startsWith("_"))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public String getParameter(String name) {
        String[] values = getParameterValues(name);
        return values != null && values.length > 0 ? values[0] : null;
    }

    @Override
    public String[] getParameterValues(String name) {
        return getParameterMap().get(name);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(getParameterMap().keySet());
    }
}
