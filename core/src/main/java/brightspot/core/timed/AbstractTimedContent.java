package brightspot.core.timed;

import java.time.Duration;
import java.util.Optional;

import brightspot.core.image.ImageOption;
import brightspot.core.tool.MediumRichTextToolbar;
import brightspot.core.tool.RichTextUtils;
import brightspot.core.tool.SmallRichTextToolbar;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.util.ObjectUtils;

/**
 * An abstract class that implements many of the basic TimedContent methods, in the context of the Broadcast plugin.
 */
public abstract class AbstractTimedContent extends Content implements
    TimedContent {

    public static final String METADATA_TAB = "Metadata";

    @Required
    @DisplayName("Name")
    private String title;

    @ToolUi.NoteHtml("<span data-dynamic-html=\"${content.getPreviewNoteHtml(toolPageContext)}\"></span>")
    private String preview;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    @ToolUi.Placeholder(dynamicText = "${content.shortDescriptionPlaceholder}", editable = true)
    private String shortDescription;

    @ToolUi.RichText(inline = false, toolbar = MediumRichTextToolbar.class)
    private String fullDescription;

    @ToolUi.Tab("Overrides")
    @DisplayName("Thumbnail")
    private ImageOption thumbnailOption;

    @ToolUi.Tab(AbstractTimedContent.METADATA_TAB)
    @ToolUi.Placeholder(dynamicText = "${content.getDurationLabel()}")
    @ToolUi.ReadOnly
    private String duration;

    public String getPreviewNoteHtml(ToolPageContext page) {
        return new TimedContentPlayerNoteRenderer()
            .render(this, getState().getField("preview"), page);
    }

    public abstract String getDurationLabel();

    /**
     * Returns the {@code title}.
     *
     * @return a plain text {@link String} (required).
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the {@code title}.
     *
     * @param title a plain text {@link String} (required).
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the {@code full description}.
     *
     * @return a RichText {@link String} with block-level elements (optional).
     */
    public String getFullDescription() {
        return fullDescription;
    }

    /**
     * Sets the {@code full description}.
     *
     * @param fullDescription a RichText {@link String} with block-level elements (optional).
     */
    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    /**
     * Returns the {@code short description}.
     *
     * @return an inline RichText {@link String} (optional).
     */
    public String getShortDescription() {
        return ObjectUtils.isBlank(shortDescription)
            ? getShortDescriptionPlaceholder()
            : shortDescription;
    }

    public String getShortDescriptionPlaceholder() {
        return ObjectUtils.isBlank(fullDescription) ? "" : RichTextUtils.getFirstBodyParagraph(fullDescription);
    }

    /**
     * Sets the {@code short description}.
     *
     * @param shortDescription an inline RichText {@link String} (optional).
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public ImageOption getThumbnailOption() {
        return thumbnailOption;
    }

    public void setThumbnailOption(ImageOption thumbnailOption) {
        this.thumbnailOption = thumbnailOption;
    }

    @Override
    public Long getTimedContentDuration() {
        return Optional.ofNullable(DurationUtils.getTimedContentDuration(this))
            .map(Duration::getSeconds)
            .orElse(null);
    }

    @Override
    @Indexed
    @ToolUi.Hidden
    public TimedContent getTimedContentItemContent() {
        return this;
    }

    @Override
    public Long getTimedContentItemDuration() {
        return getTimedContentDuration();
    }
}
