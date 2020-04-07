package bex.training.countdown;

import java.util.Optional;

import brightspot.core.image.Image;
import brightspot.core.module.ModuleType;
import brightspot.core.tool.ImagePreviewHtml;
import brightspot.core.tool.RichTextUtils;
import brightspot.core.tool.SmallRichTextToolbar;
import com.psddev.cms.db.ToolUi;
import org.apache.commons.lang3.StringUtils;

public class CountdownModule extends ModuleType implements
        ImagePreviewHtml {

    @Required
    private Countdown countdown;

    @DisplayName("Title")
    @ToolUi.Placeholder(dynamicText = "${content.getTitleFallback()}")
    private String titleOverride;

    @DisplayName("Description")
    @ToolUi.Placeholder(dynamicText = "${content.getDescriptionFallback()}")
    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String descriptionOverride;

    @DisplayName("Image")
    @ToolUi.NoteHtml("<span data-dynamic-html='${content.getImagePlaceholderHtml()}'></span>")
    private Image imageOverride;

    // --- Getters/Setters ---

    public Countdown getCountdown() {
        return countdown;
    }

    public void setCountdown(Countdown countdown) {
        this.countdown = countdown;
    }

    public String getTitleOverride() {
        return titleOverride;
    }

    public void setTitleOverride(String titleOverride) {
        this.titleOverride = titleOverride;
    }

    public String getDescriptionOverride() {
        return descriptionOverride;
    }

    public void setDescriptionOverride(String descriptionOverride) {
        this.descriptionOverride = descriptionOverride;
    }

    public Image getImageOverride() {
        return imageOverride;
    }

    public void setImageOverride(Image imageOverride) {
        this.imageOverride = imageOverride;
    }

    // --- API methods ---

    public String getTitle() {
        return Optional.ofNullable(getTitleOverride())
                .filter(StringUtils::isNotBlank)
                .orElseGet(this::getTitleFallback);
    }

    public String getDescription() {
        return Optional.ofNullable(getDescriptionOverride())
                .filter(d -> !RichTextUtils.isBlank(d))
                .orElseGet(this::getDescriptionFallback);
    }

    public Image getImage() {
        return Optional.ofNullable(getImageOverride())
                .orElseGet(this::getImageFallback);
    }

    // --- Fallbacks/Placeholders ---

    public String getTitleFallback() {
        return Optional.ofNullable(getCountdown())
                .map(Countdown::getName)
                .orElse(null);
    }

    public String getDescriptionFallback() {
        return Optional.ofNullable(getCountdown())
                .map(Countdown::getDescription)
                .orElse(null);
    }

    public Image getImageFallback() {
        return Optional.ofNullable(getCountdown())
                .map(Countdown::getImage)
                .orElse(null);
    }

    public String getImagePlaceholderHtml() {
        if (imageOverride != null) {
            return null;
        }

        return writePreviewImageHtml(getImageFallback());
    }
}
