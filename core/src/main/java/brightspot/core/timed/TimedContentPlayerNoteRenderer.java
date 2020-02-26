package brightspot.core.timed;

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import brightspot.core.video.Video;
import com.psddev.cms.db.ImageTag;
import com.psddev.cms.db.Localization;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.State;
import com.psddev.dari.util.HtmlWriter;
import com.psddev.dari.util.PageContextFilter;
import com.psddev.dari.util.StorageItem;

/**
 * Note renderer for displaying both a player for audio/video content as well as a companion selector interface.
 */
public class TimedContentPlayerNoteRenderer implements ToolUi.NoteRenderer {

    private static final String NOTE_CSS_CLASS = "TimedContentPlayerNote";

    private static final String NOTE_THUMBNAIL_CSS_CLASS = NOTE_CSS_CLASS + "-thumbnail";
    private static final String NOTE_PLAYER_CSS_CLASS = NOTE_CSS_CLASS + "-player";

    static final String EXTERNAL_PLAYER_CONTROL_CSS_CLASS = NOTE_CSS_CLASS + "-player-control";

    static final String TIMED_COMPANION_SELECTOR_CSS_CLASS = "TimedCompanionSelector";
    private static final String TIMED_CONTENT_PREVIEW_CSS_CLASS = "TimedContentPreview";

    private static final String NOTE_PLAYER_FRAME_CSS_CLASS = NOTE_PLAYER_CSS_CLASS + "-frame";

    @Override
    public String render(Object object) {
        return render(object, null, getToolPageContext());
    }

    public String render(Object object, ObjectField field, ToolPageContext page) {
        return render(object, field, page, false);
    }

    public String render(Object object, ObjectField field, ToolPageContext page, boolean isCompanionSelector) {

        Context context = createContext(object, field, page, isCompanionSelector);

        if (context == null) {
            return null;
        }

        StringWriter html = new StringWriter();
        HtmlWriter writer = new HtmlWriter(html);

        try {
            writeTimedContentPlayer(writer, context);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return html.toString();
    }

    private Context createContext(Object object, ObjectField field, ToolPageContext page, boolean isCompanionSelector) {

        Context context = new Context();

        TimedContent timedContent = null;

        if (object instanceof TimedContentProvider) {
            timedContent = ((TimedContentProvider) object).getTimedContent();
        }

        if ((object instanceof TimedContent)) {
            timedContent = (TimedContent) object;
        }

        if (timedContent == null) {
            return null;
        }

        context.page = page;
        context.timedContent = timedContent;
        context.object = object;
        context.field = field;
        context.isCompanionSelector = isCompanionSelector;
        context.toolPlayer = timedContent.getTimedContentToolPlayer();
        context.previewWidth = 350;
        context.previewHeight = 250;

        if (context.toolPlayer != null) {
            context.previewImage = context.toolPlayer.getTimedContentToolPlayerPreview(page);

            if (context.isCompanionSelector) {
                context.playerFrameUrl = context.toolPlayer.getTimedCompanionSelectionFrameUrl(context.page);

            } else {
                context.playerFrameUrl = context.toolPlayer.getTimedContentPreviewFrameUrl(context.page);
            }
        }

        return context;
    }

    // NOTE_CSS_CLASS
    private void writeTimedContentPlayer(HtmlWriter writer, Context context) throws IOException {

        writeDynamicCss(writer, context);

        // If there's no player and we're selecting companion content, then don't show anything at all.
        if (context.playerFrameUrl == null) {

            if (context.isCompanionSelector) {
                return;

            } else if (context.previewImage == null) {
                writer.writeHtml(Localization.currentUserText(
                    Video.class,
                    "message.noPreviewAvailable",
                    "No Preview Available."));
                return;
            }
        }

        writer.writeStart("div", "class", NOTE_CSS_CLASS);
        {
            writeTimedContentPlayerThumbnail(writer, context);

            if (context.playerFrameUrl != null) {
                writeTimedContentPlayerPlayer(writer, context);
            }
        }
        writer.writeEnd();
    }

    // NOTE_THUMBNAIL_CSS_CLASS
    private void writeTimedContentPlayerThumbnail(HtmlWriter writer, Context context) throws IOException {

        writer.writeStart("div", "class", NOTE_THUMBNAIL_CSS_CLASS);
        {
            if (context.playerFrameUrl != null) {
                writer.writeStart("a",
                    "class", NOTE_THUMBNAIL_CSS_CLASS + "-link",
                    "target", getFrameId(context.object, context.field),
                    "href", context.playerFrameUrl);
                {
                    writeThumbnailImage(writer, context);
                }
                writer.writeEnd();

            } else {
                writeThumbnailImage(writer, context);
            }
        }
        writer.writeEnd();
    }

    // NOTE_PLAYER_CSS_CLASS
    private void writeTimedContentPlayerPlayer(HtmlWriter writer, Context context) throws IOException {

        writer.writeStart("div", "class", NOTE_PLAYER_CSS_CLASS);
        {
            writer.writeStart("span",
                "class", NOTE_PLAYER_CSS_CLASS + "-close link");
            {
                writer.writeHtml(Localization.currentUserText(getClass(), "action.close", "Close"));
            }
            writer.writeEnd();

            if (context.isCompanionSelector) {
                writeTimedCompanionSelector(writer, context);
            } else {
                writeTimedContentPreview(writer, context);
            }
        }
        writer.writeEnd();
    }

    // TIMED_COMPANION_SELECTOR_CSS_CLASS
    private void writeTimedCompanionSelector(HtmlWriter writer, Context context) throws IOException {

        writer.writeStart("div", "class", TIMED_COMPANION_SELECTOR_CSS_CLASS);
        {
            writer.writeStart("div", "class", TIMED_COMPANION_SELECTOR_CSS_CLASS + "-player");
            {
                writePlayerFrame(writer, context);
            }
            writer.writeEnd();

            writer.writeStart("div", "class", TIMED_COMPANION_SELECTOR_CSS_CLASS + "-controls");
            {
                writer.writeStart("button", "class", TIMED_COMPANION_SELECTOR_CSS_CLASS + "-controls-button");
                {
                    // TODO: Localize
                    writer.writeHtml("Add Companion");
                }
                writer.writeEnd();

                writer.writeStart("span", "class", TIMED_COMPANION_SELECTOR_CSS_CLASS + "-controls-timestamp",
                    "data-ts", "0",
                    "data-ts-formatted", "0:00");
                {
                    writer.writeHtml("@ 0:00");
                }
                writer.writeEnd();

            }
            writer.writeEnd();
        }
        writer.writeEnd();
    }

    // TIMED_CONTENT_PREVIEW_CSS_CLASS
    private void writeTimedContentPreview(HtmlWriter writer, Context context) throws IOException {
        writer.writeStart("div", "class", TIMED_CONTENT_PREVIEW_CSS_CLASS);
        {
            writePlayerFrame(writer, context);
        }
    }

    // NOTE_PLAYER_FRAME_CSS_CLASS
    private void writePlayerFrame(HtmlWriter writer, Context context) throws IOException {

        String frameId = getFrameId(context.object, context.field);

        writer.writeStart("div", "class", NOTE_PLAYER_FRAME_CSS_CLASS);
        {
            writer.writeStart("div", "class", "frame", "name", frameId);
            writer.writeEnd();
        }
        writer.writeEnd();
    }

    private String getFrameId(Object object, ObjectField field) {
        return "VideoPreview-" + State.getInstance(object).getId() + "-" + field.getInternalName();
    }

    private void writeThumbnailImage(HtmlWriter writer, Context context) throws IOException {

        String previewImageUrl = null;

        if (context.previewImage != null) {
            previewImageUrl = new ImageTag.Builder(context.previewImage)
                .setWidth(context.previewWidth)
                .setHeight(context.previewHeight)
                .toUrl();
        }

        if (previewImageUrl != null) {
            writer.writeTag("img",
                "src", previewImageUrl,
                "width", context.previewWidth,
                "height", context.previewHeight);
        } else {
            writer.writeStart("span", "class", "TimedContentPlayerNote-thumbnail-noimage");
            writer.writeEnd();
        }
    }

    private void writeDynamicCss(HtmlWriter writer, Context context) throws IOException {

        writer.writeStart("style");
        {
            String inputName = State.getInstance(context.object).getId() + "/" + context.field.getInternalName();

            if (!context.isCompanionSelector) {
                writer.writeCss(".inputContainer[data-name=\"" + inputName + "\"] > .inputSmall",
                    "display", "none !important");

            } else {
                writer.writeCss(".inputContainer[data-name=\"" + inputName + "\"] > .inputNote",
                    "margin-left", "0", "position", "absolute");

                if (context.playerFrameUrl != null) {
                    writer.writeCss(".inputContainer[data-name=\"" + inputName + "\"] > .inputLarge",
                        "margin-left", "360px", "min-height", "319px", "padding-top", "18px");
                }
            }
        }
        writer.writeEnd();
    }

    private ToolPageContext getToolPageContext() {

        HttpServletRequest request = PageContextFilter.Static.getRequestOrNull();
        HttpServletResponse response = PageContextFilter.Static.getResponseOrNull();

        if (request == null || response == null) {
            return null;
        }

        return new ToolPageContext(request.getServletContext(), request, response);
    }

    private static class Context {

        ToolPageContext page;
        String playerFrameUrl;
        TimedContent timedContent;
        Object object;
        ObjectField field;
        TimedContentToolPlayer toolPlayer;
        int previewWidth;
        int previewHeight;
        boolean isCompanionSelector;
        StorageItem previewImage;
    }
}
