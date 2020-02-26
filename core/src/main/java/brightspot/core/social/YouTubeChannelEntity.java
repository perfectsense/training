package brightspot.core.social;

import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.Recordable;

/**
 * A type of {@link YouTubeEntity} that represents a channel.
 */
@Recordable.DisplayName("Channel")
public class YouTubeChannelEntity extends YouTubeEntity implements Interchangeable {

    @Override
    public String getBaseUrl() {
        return "https://www.youtube.com/channel/";
    }

    @Override
    public boolean loadTo(Object newObject) {
        if (newObject instanceof YouTubeCustomEntity) {
            ((YouTubeCustomEntity) newObject).setYouTubeId(this.getYouTubeId());
            return true;
        }
        return false;
    }
}
