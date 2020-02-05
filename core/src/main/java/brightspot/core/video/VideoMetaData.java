package brightspot.core.video;

import java.util.Set;

import brightspot.core.image.ImageOption;
import brightspot.core.timed.TimedContentMetaData;
import com.psddev.dari.util.StorageItem;

public interface VideoMetaData extends TimedContentMetaData {

    StorageItem getPreviewStorageItem();

    ImageOption getThumbnail();

    Set<Option> getOptions();

    VideoProvider getVideoProvider();
}
