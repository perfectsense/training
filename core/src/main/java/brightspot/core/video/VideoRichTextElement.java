package brightspot.core.video;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import brightspot.core.playlist.Playlist;
import com.psddev.cms.db.RichTextElement;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Recordable.DisplayName("Video")
@RichTextElement.Tag(value = VideoRichTextElement.TAG_NAME,
    block = true,
    initialBody = "Video",
    preview = true,
    position = -30.0,
    readOnly = true,
    root = true,
    keymaps = { "Shift-Ctrl-V" },
    tooltip = "Add Video (Shift-Ctrl-V)",
    menu = "Enhancements"
)
@ToolUi.IconName(Video.ICON_NAME)
public class VideoRichTextElement extends RichTextElement {

    public static final String TAG_NAME = "bsp-video";

    private static final String STATE_ATTRIBUTE = "data-state";

    @Required
    private VideoMetaData video;

    private Playlist playlist;

    public VideoMetaData getVideo() {
        return video;
    }

    public void setVideo(VideoMetaData video) {
        this.video = video;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    @Override
    public void fromAttributes(Map<String, String> attributes) {
        if (attributes != null) {
            String stateString = attributes.get(STATE_ATTRIBUTE);
            if (stateString != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> simpleValues = (Map<String, Object>) ObjectUtils.fromJson(stateString);
                getState().setValues(simpleValues);
            }
        }
    }

    @Override
    public Map<String, String> toAttributes() {

        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put(STATE_ATTRIBUTE, ObjectUtils.toJson(getState().getSimpleValues()));
        return attributes;
    }

    @Override
    public void fromBody(String body) {
        // do nothing
    }

    @Override
    public String toBody() {
        return video != null ? video.getHeadline() : "Video";
    }

    @Override
    public void writePreviewHtml(ToolPageContext page) throws IOException {
        if (video != null) {

            String thumbnailUrl = page.getPreviewThumbnailUrl(video);

            if (thumbnailUrl != null) {
                page.writeStart("img", "src", thumbnailUrl, "height", 300);
            }
        }
    }
}
