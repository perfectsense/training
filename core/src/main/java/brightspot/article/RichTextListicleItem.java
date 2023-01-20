package brightspot.article;

import java.util.Optional;

import brightspot.image.WebImage;
import brightspot.image.WebImageAsset;
import brightspot.rte.LargeRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.rte.image.ImageRichTextElement;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
@Recordable.DisplayName("Rich Text")
public class RichTextListicleItem extends Record implements ListicleItem {

    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String heading;

    @ToolUi.RichText(inline = false, toolbar = LargeRichTextToolbar.class)
    private String body;

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getHeading() {
        return this.heading;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Returns the first image within the listicle item.
     *
     * @return an {@link WebImage} or {@code null}, if no image was found.
     */
    @Override
    public WebImageAsset getFirstImage() {
        return ImageRichTextElement.getFirstImageFromRichText(getBody());
    }

    private Optional<String> getHeadingPlainText() {
        return Optional.ofNullable(getHeading())
            .map(RichTextUtils::stripRichTextElements)
            .map(RichTextUtils::richTextToPlainText);
    }

    private Optional<String> getBodyPlainText() {
        return Optional.ofNullable(getBody())
            .map(RichTextUtils::stripRichTextElements)
            .map(RichTextUtils::richTextToPlainText);
    }

    @Override
    public long getTextCharacterCount() {
        return getHeadingPlainText().map(String::length).orElse(0)
            + getBodyPlainText().map(String::length).orElse(0);
    }

    @Override
    public String getFullContentEncoded() {
        return getHeadingPlainText().map(heading -> heading + " ").orElse("")
            + getBodyPlainText().orElse("");
    }

    @Override
    public String getSuggestableText() {
        return getHeadingPlainText().map(heading -> heading + " ").orElse("")
            + getBodyPlainText().orElse("");
    }

    @Override
    public String getLabel() {
        return getHeadingPlainText().orElse(null);
    }
}
