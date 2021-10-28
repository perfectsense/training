package training.rte;

import java.util.ArrayList;
import java.util.Objects;

import brightspot.core.rte.heading.HeadingTwo;
import com.psddev.cms.db.RichTextElement;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import org.jsoup.nodes.Element;
import training.ExportUtils;

public class HeadingTwoConverter extends RichTextConverter {

    @Override
    public void convert(Recordable parent, Element element) {
        RichTextElement rte = Objects.requireNonNull(RichTextElement.fromElement(parent, element));
        if (!(rte instanceof HeadingTwo)) {
            throw new IllegalArgumentException("Received " + rte.getClass().getName());
        }

        HeadingTwo headingTwo = (HeadingTwo) rte;

        Element replacement = new Element("h2");
        replacement.attr("data-state", ObjectUtils.toJson(ExportUtils.buildRef(headingTwo)));

        // replacement updates childNodes so need a copy to prevent ConcurrentModificationException
        new ArrayList<>(element.childNodes()).forEach(replacement::appendChild);

        element.replaceWith(replacement);
    }
}
