package brightspot.rte.processor;

import brightspot.rte.importer.html.jsoup.JsoupHtmlTagProcessor;
import org.jsoup.nodes.Element;

/**
 * Process {@code <em>} tags.
 *
 * By default, replaces them with {@code <i>} tags.
 */
public class DefaultJsoupEmTagProcessor implements JsoupHtmlTagProcessor {

    private static final String TAG = "em";

    private static final String CONVERTED_TAG = "i";

    @Override
    public String getHtmlTag() {
        return TAG;
    }

    @Override
    public String head(Element element, int depth) {
        return null;
    }

    @Override
    public String tail(Element element, int depth) {
        if (element == null) {
            return null;
        }

        element.tagName(CONVERTED_TAG);
        return element.outerHtml();
    }
}
