package brightspot.rte.processor;

import brightspot.rte.importer.html.jsoup.JsoupHtmlTagProcessor;
import org.jsoup.nodes.Element;

/**
 * Process {@code <strong>} tags.
 *
 * By default, replaces them with {@code <b>} tags.
 */
public class DefaultJsoupStrongTagProcessor implements JsoupHtmlTagProcessor {

    private static final String TAG = "strong";

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

        element.tagName("b");
        return element.outerHtml();
    }
}
