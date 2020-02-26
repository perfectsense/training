package brightspot.core.site;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.psddev.cms.db.PageFilter;
import com.psddev.dari.util.AbstractFilter;

/**
 * Support for <a href="https://developer.mozilla.org/en-US/docs/Web/Manifest">Web App Manifests</a>. Currently, some
 * Android devices use this.
 *
 * Intercepts requests to {@link ManifestFilter#PATH} and returns the xml from {@link
 * FrontEndSettings#getWebAppManifest()} ()}.
 */
public class ManifestFilter extends AbstractFilter implements AbstractFilter.Auto {

    public static final String PATH = "/manifest.json";
    private static final String CONTENT_TYPE = "application/json";

    public boolean isFilePath(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return PageFilter.Static.getMainObject(request) == null
            && request.getRequestURI().equals(PATH);
    }

    public InputStream getInputStream(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return Optional.ofNullable(FrontEndSettings.get(
            PageFilter.Static.getSite(request),
            FrontEndSettings::getWebAppManifest))
            .map(manifestString -> new ByteArrayInputStream(manifestString.getBytes(StandardCharsets.UTF_8)))
            .orElse(null);
    }

    public String getMimeType(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return CONTENT_TYPE;
    }

    // TODO : intentionally duplicated, pending BSP-3379
    @Override
    protected void doRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws Exception {

        if (!isFilePath(request, response)) {
            chain.doFilter(request, response);
            return;
        }

        InputStream inputStream = getInputStream(request, response);

        if (inputStream == null) {
            chain.doFilter(request, response);
            return;
        }

        response.setContentType(getMimeType(request, response));
        ServletOutputStream out = response.getOutputStream();
        int b;
        while ((b = inputStream.read()) != -1) {
            out.write(b);
        }

        inputStream.close();
        out.close();
    }

    // TODO : intentionally duplicated, pending BSP-3379
    @Override
    public void updateDependencies(
        Class<? extends AbstractFilter> filterClass,
        List<Class<? extends Filter>> dependencies) {
        if (PageFilter.class.isAssignableFrom(filterClass)) {
            dependencies.add(getClass());
        }
    }
}
