package training.rte;

import java.util.Objects;

import brightspot.core.image.Image;
import brightspot.core.image.ImageOption;
import brightspot.core.image.ImageRichTextElement;
import brightspot.core.image.OneOffImageOption;
import brightspot.core.image.SharedImageOption;
import brightspot.util.FixedMap;
import com.psddev.cms.db.RichTextElement;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import org.jsoup.nodes.Element;
import training.ExportUtils;

public class ImageRTEConverter extends RichTextConverter {

    @Override
    public void convert(Recordable parent, Element element) {
        RichTextElement rte = Objects.requireNonNull(RichTextElement.fromElement(parent, element));
        if (!(rte instanceof ImageRichTextElement)) {
            throw new IllegalArgumentException("Received " + rte.getClass().getName());
        }

        ImageRichTextElement imageRTE = (ImageRichTextElement) rte;

        ImageOption imageOption = imageRTE.getImage();
        if (imageOption == null) {
            element.remove();
            return;
        }

        Image image;
        if (imageOption instanceof OneOffImageOption) {
            image = ExportUtils.saveOneOffImage((OneOffImageOption) imageOption);

        } else if (imageOption instanceof SharedImageOption) {
            image = ((SharedImageOption) imageOption).getImage();

        } else {
            throw new IllegalArgumentException("Unhandled ImageOption type " + imageOption.getClass().getName());
        }

        if (image == null) {
            element.remove();
            return;
        }

        Element replacement = new Element("bsp-image");
        replacement.attr("data-state", ObjectUtils.toJson(FixedMap.of(
            "image", ExportUtils.buildRef(image),
            "_id", imageRTE.getId(),
            "_type", ExportUtils.getExportType(imageRTE)
        )));

        replacement.text(image.getLabel());

        element.replaceWith(replacement);
    }
}
