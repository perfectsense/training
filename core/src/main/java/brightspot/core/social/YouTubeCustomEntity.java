package brightspot.core.social;

import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.Recordable;

/**
 * A type of {@link YouTubeEntity} that represents a custom url.
 */
@Recordable.DisplayName("Custom")
public class YouTubeCustomEntity extends YouTubeEntity implements Interchangeable {

    @Override
    public String getBaseUrl() {
        return "https://www.youtube.com/";
    }

    @Override
    public boolean loadTo(Object newObject) {
        if (newObject instanceof YouTubeChannelEntity) {
            ((YouTubeChannelEntity) newObject).setYouTubeId(this.getYouTubeId());
            return true;
        }
        return false;
    }
}
