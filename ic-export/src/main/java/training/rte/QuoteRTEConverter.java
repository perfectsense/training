package training.rte;

import java.util.ArrayList;
import java.util.Objects;

import brightspot.core.quote.QuoteRichTextElement;
import brightspot.util.FixedMap;
import com.psddev.cms.db.RichTextElement;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.nodes.Element;
import training.ExportUtils;

public class QuoteRTEConverter extends RichTextConverter {

    @Override
    public void convert(Recordable parent, Element element) {
        RichTextElement rte = Objects.requireNonNull(RichTextElement.fromElement(parent, element));
        if (!(rte instanceof QuoteRichTextElement)) {
            throw new IllegalArgumentException("Received " + rte.getClass().getName());
        }

        QuoteRichTextElement quoteRTE = (QuoteRichTextElement) rte;

        Element replacement = new Element("bsp-pull-quote");
        replacement.attr("data-state", ObjectUtils.toJson(FixedMap.of(
            "text", StringEscapeUtils.escapeHtml4(quoteRTE.getQuote()),
            "attribution", StringEscapeUtils.escapeHtml4(quoteRTE.getAttribution()),
            "_id", quoteRTE.getId(),
            "_type", ExportUtils.getExportType(quoteRTE)
        )));

        // replacement updates childNodes so need a copy to prevent ConcurrentModificationException
        new ArrayList<>(element.childNodes()).forEach(replacement::appendChild);

        element.replaceWith(replacement);
    }
}
