package brightspot.core.tool;

import java.io.StringWriter;
import java.util.Optional;

import brightspot.core.image.Image;
import brightspot.core.image.ImageOption;
import com.psddev.cms.db.ImageTag;
import com.psddev.dari.util.HtmlWriter;
import com.psddev.dari.util.StorageItem;

public interface ImagePreviewHtml {

    default String writePreviewImageHtml(Image image) {
        return Optional.ofNullable(image)
                .map(Image::getFile)
                .map(this::writePreviewImageHtml)
                .orElse(null);
    }

    default String writePreviewImageHtml(ImageOption image) {
        return Optional.ofNullable(image)
                .map(ImageOption::getImageOptionFile)
                .map(this::writePreviewImageHtml)
                .orElse(null);
    }

    /**
     * Returns CMS UI HTML markup for the image fallback when it's not explicitly published in the object field. Used
     * for dynamic ToolUi.NoteHtml.
     *
     * @return CMS UI HTML
     */
    default String writePreviewImageHtml(StorageItem file) {

        if (file == null) {
            return "<span></span>";
        }

        StringWriter stringWriter = new StringWriter();
        HtmlWriter htlWriter = new HtmlWriter(stringWriter);
        try {
            htlWriter.writeStart("p", "style", "margin:0 0 3px 0;");
            {
                htlWriter.writeHtml("Defaults to");
            }
            htlWriter.writeEnd();

            htlWriter.writeTag("img",
                "src", new ImageTag.Builder(file).setHeight(100).toUrl(),
                "style", "width: auto; height: 100px; border:solid 1px #cdcdcd; padding: 3px;");

        } catch (Exception e) {
            // ignore
        }

        return stringWriter.toString();
    }
}
