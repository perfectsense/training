package brightspot.core.timed;

import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

public interface TimedContentAssetProvider extends Recordable {

    String INTERNAL_NAME = "brightspot.core.timed.TimedContentAssetProvider";

    String getAssetId();

    TimedContentMetaData getTimedContentMetadata();

    void setTimedContentMetadata(TimedContentMetaData timedContentMetadata);

    Long getTimedContentDuration();

    StorageItem getTimedContentThumbnailFallback();
}
