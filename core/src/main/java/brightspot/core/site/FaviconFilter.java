package brightspot.core.site;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.psddev.cms.db.ImageTag;
import com.psddev.cms.db.PageFilter;
import com.psddev.cms.db.Site;
import com.psddev.dari.util.AbstractFilter;
import com.psddev.dari.util.ImageEditor;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;
import com.psddev.dari.util.UrlBuilder;
import com.psddev.dari.util.UrlStorageItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Serves resized favicons from {@link FrontEndSettings#getFavicon()}. This functionality is based on recommendations
 * by
 * <a href="https://realfavicongenerator.net/blog/new-favicon-package-less-is-more/">Real Favicon Generator</a>.
 *
 * This filter will override the response at the following URLs (if {@link FrontEndSettings#getFavicon()} is not {@code
 * null}):
 *
 * <ul>
 * <li>/apple-touch-icon.png (180x180)
 * <li>/favicon-?x?.png (e.g. /favicon-120x120.png)
 * <li>/apple-touch-icon-?x?.png (e.g. /apple-touch-icon-120x120.png)
 * <li>/favicon.ico
 * </ul>
 */
public class FaviconFilter extends AbstractFilter implements AbstractFilter.Auto {

    private static final Logger LOGGER = LoggerFactory.getLogger(FaviconFilter.class);
    private static final String FAVICON_PATH_PREFIX = "/favicon";
    private static final String FAVICON_ICO_PATH = "/favicon.ico";
    private static final String APPLE_TOUCH_ICON_PATH_PREFIX = "/apple-touch-icon";
    private static final Pattern PNG_URL_PATTERN = Pattern.compile("(/[a-zA-Z-]+)((?<=-)(\\d*)x(\\d*))?(-[a-zA-Z]+)?.png");
    private static final Set<String> PATH_PREFIXES;

    static {
        PATH_PREFIXES = new HashSet<>();
        PATH_PREFIXES.add(FAVICON_PATH_PREFIX);
        PATH_PREFIXES.add(APPLE_TOUCH_ICON_PATH_PREFIX);
    }

    public boolean isFilePath(HttpServletRequest request, HttpServletResponse response) {
        return PageFilter.Static.getMainObject(request) == null
            && PATH_PREFIXES.parallelStream().anyMatch(pathPrefix -> request.getRequestURI().startsWith(pathPrefix));
    }

    public String getMimeType(HttpServletRequest request, HttpServletResponse response) {
        return ObjectUtils.getContentType(request.getRequestURI());
    }

    public InputStream getIcoInputStream(HttpServletRequest request, HttpServletResponse response) {

        Site site = PageFilter.Static.getSite(request);
        StorageItem faviconFile = Optional.ofNullable(FrontEndSettings.get(site, FrontEndSettings::getFaviconIco))
            .orElse(null);

        if (faviconFile == null) {
            return null;
        }

        try {
            return new BufferedInputStream(faviconFile.getData());

        } catch (IOException e) {
            LOGGER.error("Can't read favicon ico for Site [{}]", site == null ? null : site.getId().toString(), e);
        }

        return null;
    }

    public InputStream getInputStream(HttpServletRequest request, HttpServletResponse response) {
        StorageItem faviconFile = Optional.ofNullable(FrontEndSettings.get(
            PageFilter.Static.getSite(request),
            FrontEndSettings::getFavicon))
            .orElse(null);

        if (faviconFile == null) {
            return null;
        }
        String generatedFaviconPath = null;

        try {
            generatedFaviconPath = createPngUrl(request, response, faviconFile);

        } catch (URISyntaxException e) {
            LOGGER.error("Can't create favicon url", e);
        }

        if (StringUtils.isBlank(generatedFaviconPath)) {
            return null;
        }

        try {
            UrlStorageItem generatedFavicon = new UrlStorageItem();
            generatedFavicon.setPath(generatedFaviconPath);
            return new BufferedInputStream(generatedFavicon.getData());

        } catch (IOException e) {
            LOGGER.error("Can't read favicon: {}", generatedFaviconPath, e);
        }
        return null;
    }

    private String createPngUrl(HttpServletRequest request, HttpServletResponse response, StorageItem file)
        throws URISyntaxException {

        Matcher matcher = PNG_URL_PATTERN.matcher(request.getRequestURI());

        if (matcher.find()) {
            Integer width = Optional.ofNullable(matcher.group(3))
                .map(intWidth -> ObjectUtils.to(Integer.class, intWidth))
                .orElse(-1);

            Integer height = Optional.ofNullable(matcher.group(4))
                .map(intHeight -> ObjectUtils.to(Integer.class, intHeight))
                .orElse(-1);

            if ((width == -1 && height == -1)
                && matcher.group(1).equals(APPLE_TOUCH_ICON_PATH_PREFIX)
                || matcher.group(1).equals(FAVICON_PATH_PREFIX)) {
                width = 180;
                height = 180;
            }

            return createPngUrl(request, response, file, width, height);
        } else {
            return null;
        }
    }

    private String createPngUrl(
        HttpServletRequest request,
        HttpServletResponse response,
        StorageItem file,
        Integer width,
        Integer height) throws URISyntaxException {

        if (width == null || width < 10 || width > 512
            || height == null || height < 10 || height > 512
            || !width.equals(height)) {
            return null;
        }

        ImageEditor editor = ImageEditor.Static.getDefault();
        file = editor.edit(file, "format", null, "png");

        String faviconUrl = new ImageTag.Builder(file)
            .setWidth(width)
            .setHeight(height)
            .toUrl();

        URI uri = new URI(faviconUrl);
        if (!uri.isAbsolute()) {
            faviconUrl = new UrlBuilder(request)
                .currentScheme()
                .currentHost()
                .path(faviconUrl)
                .toString();
        }

        return faviconUrl;
    }

    // TODO : intentionally duplicated, pending BSP-3379
    @Override
    protected void doRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws Exception {

        if (!isFilePath(request, response)) {
            chain.doFilter(request, response);
            return;
        }

        InputStream inputStream;

        if (FAVICON_ICO_PATH.equals(request.getRequestURI())) {
            inputStream = getIcoInputStream(request, response);
        } else {
            inputStream = getInputStream(request, response);
        }

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
