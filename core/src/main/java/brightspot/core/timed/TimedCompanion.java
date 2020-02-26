package brightspot.core.timed;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.HtmlWriter;
import com.psddev.dari.util.ObjectUtils;

/**
 * Companion content for {@linkplain TimedContent} that is tagged to a specific point in time within the timed content's
 * duration.
 */
@Recordable.Embedded
public class TimedCompanion extends Content {

    private transient TimedContent timedContent;

    @DisplayName("Companion")
    @Required
    private Content companionContent;

    // Stored in seconds
    @ToolUi.Hidden
    @Required
    @DisplayName("Timestamp")
    private Long offset;

    @ToolUi.NoteHtml("<span data-dynamic-html='${content.getOffsetLabelNoteHtml(toolPageContext)}'></span>")
    @ToolUi.Placeholder("hh:mm:ss")
    @ToolUi.DisplayName("Timestamp")
    private String offsetLabel;

    public TimedContent getTimedContent() {
        return timedContent;
    }

    public void setTimedContent(TimedContent timedContent) {
        this.timedContent = timedContent;
    }

    public Content getCompanionContent() {
        return companionContent;
    }

    public void setCompanionContent(Content companionContent) {
        this.companionContent = companionContent;
    }

    /**
     * @return The offset (in seconds) for this companion.
     */
    public Long getOffset() {
        updateOffset();
        return offset;
    }

    /**
     * @return The offset (in seconds) for this companion.
     */
    public Long getOffsetSeconds() {
        return getOffset();
    }

    /**
     * @return The offset (in milliseconds) for this companion.
     */
    public Long getOffsetMilliseconds() {
        Long offsetSeconds = getOffset();
        return offsetSeconds != null ? offsetSeconds * 1000 : null;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public String getOffsetLabelNoteHtml(ToolPageContext page) {

        StringWriter html = new StringWriter();
        HtmlWriter writer = new HtmlWriter(html);

        try {
            writer.writeRaw(new TimedCompanionOffsetLabelNoteRenderer(offsetLabel)
                .render(this, getState().getField("offsetLabel"), page));

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return html.toString();
    }

    private void updateOffset() {
        offset = DurationUtils.timeFormatToSeconds(offsetLabel);
    }

    @Override
    protected void beforeSave() {
        super.beforeSave();
        updateOffset();
    }

    private void updateOffsetLabel() {
        if (offset != null) {
            offsetLabel = DurationUtils.secondsToDurationLabel(offset);
        }
    }

    @Override
    protected void beforeCommit() {
        super.beforeCommit();
        updateOffsetLabel();
    }

    @Override
    public String getLabel() {

        Content content = getCompanionContent();

        String percentageLabel = DurationUtils.durationsToPercentageLabel(
            DurationUtils.timeFormatToDuration(offsetLabel),
            DurationUtils.getTimedContentDuration(getTimedContent()));

        String durationLabel = DurationUtils.durationToLabel(DurationUtils.timeFormatToDuration(offsetLabel));
        String contentLabel = content != null ? content.getLabel() : null;

        StringBuilder builder = new StringBuilder();

        if (durationLabel != null) {
            builder.append(durationLabel);

            if (percentageLabel != null) {
                builder.append(" (").append(percentageLabel).append(")");
            } else {
                builder.append(" ( ! )");
            }
        }

        if (contentLabel != null) {
            if (builder.length() > 0) {
                builder.append(" - ");
            }

            builder.append(contentLabel);
        }

        return builder.length() > 0 ? builder.toString() : null;
    }

    // Sorts the timed companions based on their offsets.
    static void sortList(List<TimedCompanion> timedCompanions) {
        if (timedCompanions == null) {
            return;
        }
        Collections.sort(timedCompanions, (tc1, tc2) -> ObjectUtils.compare(tc1.getOffset(), tc2.getOffset(), true));
    }
}
