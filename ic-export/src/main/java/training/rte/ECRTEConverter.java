package training.rte;

import java.util.Objects;

import brightspot.util.FixedMap;
import com.psddev.cms.db.RichTextElement;
import com.psddev.cms.rte.ExternalContentRichTextElement;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import org.jsoup.nodes.Element;
import training.ExportUtils;

public class ECRTEConverter extends RichTextConverter {

    @Override
    public void convert(Recordable parent, Element element) {
        RichTextElement rte = Objects.requireNonNull(RichTextElement.fromElement(parent, element));
        if (!(rte instanceof ExternalContentRichTextElement)) {
            throw new IllegalArgumentException("Received " + rte.getClass().getName());
        }

        ExternalContentRichTextElement ecrte = (ExternalContentRichTextElement) rte;

        Element replacement = new Element("brightspot-cms-external-content");
        replacement.attr("data-state", ObjectUtils.toJson(FixedMap.of(
            "url", ecrte.getUrl(),
            "maximumWidth", ecrte.getMaximumWidth(),
            "maximumHeight", ecrte.getMaximumHeight(),
            "_id", ecrte.getId(),
            "_type", ExportUtils.getExportType(ecrte)
        )));

        replacement.text(ecrte.getUrl());

        element.replaceWith(replacement);
    }
}
