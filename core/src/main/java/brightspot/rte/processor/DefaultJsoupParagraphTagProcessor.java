package brightspot.rte.processor;

import brightspot.rte.importer.html.jsoup.JsoupHtmlTagProcessor;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

/**
 * Converts {@code <p>} tags into double breaks.
 */
public class DefaultJsoupParagraphTagProcessor implements JsoupHtmlTagProcessor {

    private static final String TAG = "p";

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

        for (Node child : element.childNodes()) {
            // Remove empty formatting tags within <p> tags, skipping elements that are only text
            if (child.childNodes().isEmpty() && !(child instanceof TextNode)) {
                child.remove();
            }
        }

        // If element is empty, replace with br
        if (element.childNodes().isEmpty()) {
            element.html("<br>");
        } else if (element.nextElementSibling() != null) {
            // Only append br if the element is not the last sibling
            element.appendChild(new Element("br"));
        }

        return JsoupHtmlTagProcessor.getText(element);
    }
}
