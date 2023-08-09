package brightspot.rte.processor;

import brightspot.rte.importer.html.jsoup.JsoupHtmlTagProcessor;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

/**
 * Process {@code <div>} tags.
 *
 * By default, converts "<div>some text </div>" to "some text <br>".
 */
public class DefaultJsoupDivTagProcessor implements JsoupHtmlTagProcessor {

    private static final String TAG = "div";

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

        if ((element.hasText() || !element.children().isEmpty())) {
            element.appendChild(new Element(Tag.valueOf("br"), ""));
            element.unwrap();

            return element.outerHtml();
        } else {
            return null;
        }
    }
}
