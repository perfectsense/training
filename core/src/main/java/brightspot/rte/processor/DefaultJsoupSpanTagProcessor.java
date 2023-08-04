package brightspot.rte.processor;

import brightspot.rte.importer.html.jsoup.JsoupHtmlTagProcessor;
import org.jsoup.nodes.Element;

/**
 * Process {@code <span>} tags.
 *
 * By default, unwraps them.
 */
public class DefaultJsoupSpanTagProcessor implements JsoupHtmlTagProcessor {

    private static final String TAG = "span";

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

        element.unwrap();
        return element.outerHtml();
    }
}
