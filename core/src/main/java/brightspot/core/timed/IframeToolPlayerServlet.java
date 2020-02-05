package brightspot.core.timed;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.ServletException;

import com.psddev.cms.tool.PageServlet;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.util.RoutingFilter;
import com.psddev.dari.util.StringUtils;

/**
 * Timed content tool player that renders another URL within an iframe. This is typically only useful for simple preview
 * and not for {@linkplain TimedCompanion} selection.
 */

@RoutingFilter.Path(value = IframeToolPlayerServlet.PAGE_URL)
public class IframeToolPlayerServlet extends PageServlet {

    static final String PAGE_URL = "/_express/timed/iframe-tool-player";

    private static final String EMBED_URL_PARAMETER = "url";

    public static String getPageUrl(String url) {
        Objects.requireNonNull(url);
        return StringUtils.addQueryParameters(PAGE_URL, EMBED_URL_PARAMETER, url);
    }

    @Override
    protected String getPermissionId() {
        return null;
    }

    @Override
    protected void doService(final ToolPageContext page) throws IOException, ServletException {

        String url = page.param(String.class, EMBED_URL_PARAMETER);

        // error checking
        Map<String, String> requiredParams = new LinkedHashMap<>();
        requiredParams.put(EMBED_URL_PARAMETER, url);

        if (requiredParams.values().stream().anyMatch(Objects::isNull)) {
            String missingRequiredParams = requiredParams.entrySet().stream()
                .filter(entry -> entry.getValue() == null)
                .map(Map.Entry::getKey)
                .map(key -> "[" + key + "]")
                .collect(Collectors.joining(", "));

            throw new IllegalArgumentException("Missing required parameters " + missingRequiredParams + "!");
        }

        String frameId = page.createId();

        page.writeStart("div", "class", "TimedContentIframeToolPlayer", "style", "position:relative;height:250px");
        {
            page.writeStart("iframe",
                "id", frameId,
                "scrolling", "no",
                "src", url,
                "allowfullscreen", "",
                "style", page.cssString(
                    "border-style", "none",
                    "width", "100%"));
            page.writeEnd();
        }
        page.writeEnd();
    }
}
