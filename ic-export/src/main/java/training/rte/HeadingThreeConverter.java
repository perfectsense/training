package training.rte;

import java.util.ArrayList;
import java.util.Objects;

import brightspot.core.rte.heading.HeadingThree;
import com.psddev.cms.db.RichTextElement;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import org.jsoup.nodes.Element;
import training.ExportUtils;

public class HeadingThreeConverter extends RichTextConverter {

    @Override
    public void convert(Recordable parent, Element element) {
        RichTextElement rte = Objects.requireNonNull(RichTextElement.fromElement(parent, element));
        if (!(rte instanceof HeadingThree)) {
            throw new IllegalArgumentException("Received " + rte.getClass().getName());
        }

        HeadingThree headingThree = (HeadingThree) rte;

        Element replacement = new Element("h3");
        replacement.attr("data-state", ObjectUtils.toJson(ExportUtils.buildRef(headingThree)));

        // replacement updates childNodes so need a copy to prevent ConcurrentModificationException
        new ArrayList<>(element.childNodes()).forEach(replacement::appendChild);

        element.replaceWith(replacement);
    }
}
