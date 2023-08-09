package brightspot.rte.processor;

import brightspot.rte.blockquote.BlockQuoteRichTextElement;
import brightspot.rte.importer.html.jsoup.JsoupHtmlTagProcessor;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

/**
 * Process {@code <blockquote>} tags.
 *
 * By default, converts them to {@link BlockQuoteRichTextElement}, stripping their internal html, if any.
 */
public class DefaultJsoupBlockquoteTagProcessor implements JsoupHtmlTagProcessor {

    private static final String DATA_ATTR = "data-state";

    private static final String TAG = "blockquote";

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

        if (element.hasText() || !element.children().isEmpty()) {
            BlockQuoteRichTextElement quoteRTE = new BlockQuoteRichTextElement();
            quoteRTE.setText(element.text());
            if (element.hasAttr("cite")) {
                quoteRTE.setAttribution(element.attr("cite"));
            }

            Element bspQuote = new Element(Tag.valueOf(BlockQuoteRichTextElement.TAG_NAME), "");
            bspQuote.attr(DATA_ATTR, quoteRTE.toAttributes().get(DATA_ATTR));
            bspQuote.text(quoteRTE.toBody());

            return bspQuote.outerHtml();
        }

        return null;
    }
}
