package brightspot.core.gallery;

import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

@Deprecated
public interface SlideContent extends Recordable {

    String getGallerySlideContentTitle();

    String getGallerySlideContentDescription();

    default String getGallerySlideContentAttribution() {
        return null;
    }

    StorageItem getGallerySlideContentImageFile();
}
