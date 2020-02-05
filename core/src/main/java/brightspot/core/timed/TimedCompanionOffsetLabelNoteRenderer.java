package brightspot.core.timed;

import java.io.IOException;
import java.io.StringWriter;
import java.time.Duration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.State;
import com.psddev.dari.util.HtmlWriter;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.PageContextFilter;

/**
 * Note renderer for displaying the timestamp label for a {@linkplain TimedCompanion} as well as controls for
 * interacting with the timed companion selector player.
 */
public class TimedCompanionOffsetLabelNoteRenderer implements ToolUi.NoteRenderer {

    private String offsetLabel;

    public TimedCompanionOffsetLabelNoteRenderer() {
    }

    public TimedCompanionOffsetLabelNoteRenderer(String offsetLabel) {
        this.offsetLabel = offsetLabel;
    }

    @Override
    public String render(Object object) {
        return render(object, null, getToolPageContext());
    }

    public String render(Object object, ObjectField field, ToolPageContext page) {

        if (!(object instanceof TimedCompanion)) {
            return null;
        }

        TimedCompanion tc = (TimedCompanion) object;

        StringWriter html = new StringWriter();
        HtmlWriter writer = new HtmlWriter(html);

        String offsetLabel = getOffsetLabel(object, field);

        try {
            boolean isInvalidTimestamp = false;

            if (offsetLabel != null) {

                Duration offsetDuration = DurationUtils.timeFormatToDuration(offsetLabel);

                if (offsetDuration == null) {
                    // TODO: Localize
                    writer.writeHtml("WARNING: Invalid timestamp! Use format: hh:mm:ss");
                    isInvalidTimestamp = true;

                } else {
                    String offsetDurationLabel = DurationUtils.durationToLabel(offsetDuration);

                    Duration playableDuration = DurationUtils.getTimedContentDuration(tc.getTimedContent());

                    if (playableDuration != null) {

                        if (offsetDuration.compareTo(playableDuration) > 0) {
                            // TODO: Localize
                            writer.writeHtml("WARNING: Timestamp of " + offsetDurationLabel + " occurs after the "
                                + getTimedContentTypeLabel(tc.getTimedContent()) + " end at "
                                + DurationUtils.durationToLabel(playableDuration) + ".");
                            isInvalidTimestamp = true;

                        } else {
                            String offsetDurationUnitesLabel = DurationUtils.durationToLabelWithUnits(offsetDuration);
                            String percentLabel = DurationUtils.secondsToPercentageLabel(
                                offsetDuration.getSeconds(),
                                playableDuration.getSeconds());

                            // TODO: Localize
                            writer.writeHtml(offsetDurationUnitesLabel + " (" + percentLabel + " into the "
                                + getTimedContentTypeLabel(tc.getTimedContent()) + ").");
                        }

                    } else {
                        // TODO: Localize
                        writer.writeHtml("WARNING: Timestamp of " + offsetDurationLabel + " may be invalid because "
                            + getTimedContentTypeLabel(tc.getTimedContent()) + " length is unknown!");
                        isInvalidTimestamp = true;
                    }
                }

                if (!isInvalidTimestamp) {
                    writer.writeStart(
                        "span",
                        "class",
                        TimedContentPlayerNoteRenderer.EXTERNAL_PLAYER_CONTROL_CSS_CLASS);
                    {
                        writer.writeHtml(" | ");

                        writer.writeStart("a",
                            "class", "TimedCompanionJumpToTimeAction",
                            "data-ts", offsetDuration.getSeconds(),
                            "href", "javascript:;");
                        {
                            // TODO: Localize
                            writer.writeHtml("Jump To Time");
                        }
                        writer.writeEnd();
                    }
                    writer.writeEnd();
                }
            }

            writer.writeStart("span", "class", TimedContentPlayerNoteRenderer.EXTERNAL_PLAYER_CONTROL_CSS_CLASS);
            {
                if (offsetLabel != null) {
                    writer.writeHtml(" | ");
                }

                writer.writeStart("a",
                    "class", "TimedCompanionSyncAction",
                    "href", "javascript:;");
                {
                    // TODO: Localize
                    writer.writeHtml("Sync With " + getTimedContentTypeLabel(tc.getTimedContent()));
                }
                writer.writeEnd();
            }
            writer.writeEnd();

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return html.toString();
    }

    private String getOffsetLabel(Object object, ObjectField field) {

        if (offsetLabel == null) {
            offsetLabel = ObjectUtils.to(String.class, State.getInstance(object).get(field.getInternalName()));
        }

        return offsetLabel;
    }

    private ToolPageContext getToolPageContext() {

        HttpServletRequest request = PageContextFilter.Static.getRequestOrNull();
        HttpServletResponse response = PageContextFilter.Static.getResponseOrNull();

        if (request == null || response == null) {
            return null;
        }

        return new ToolPageContext(request.getServletContext(), request, response);
    }

    private String getTimedContentTypeLabel(TimedContent timedContent) {
        String typeLabel = null;

        if (timedContent != null) {
            typeLabel = timedContent.getState().getType().getLabel();
        }

        if (typeLabel == null) {
            // TODO: Localize
            typeLabel = "Media";
        }

        return typeLabel;
    }
}
