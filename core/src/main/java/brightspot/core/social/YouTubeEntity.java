package brightspot.core.social;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import org.apache.commons.lang3.StringUtils;

/**
 * The social field for YouTube in {@link SocialEntityData}. Used to produce a YouTube URL.
 */
@Recordable.Embedded
public abstract class YouTubeEntity extends Record {

    @ToolUi.Unlabeled
    private String youTubeId;

    public String getYouTubeId() {
        return youTubeId;
    }

    public void setYouTubeId(String youTubeId) {
        this.youTubeId = youTubeId;
    }

    public abstract String getBaseUrl();

    /**
     * @return the LinkedIn URL.
     */
    public String getUrl() {
        return !StringUtils.isBlank(youTubeId)
            ? getBaseUrl() + youTubeId
            : null;
    }
}
