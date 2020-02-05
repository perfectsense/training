package brightspot.core.imageitemstream;

import brightspot.core.image.Image;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

public interface ImageItem extends Recordable {

    /**
     * Returns the title of the {@link ImageItem}.
     *
     * @return plain text.
     */
    String getImageItemTitle();

    /**
     * Returns the description of the {@link ImageItem}.
     *
     * @return RichText with inline elements.
     */
    String getImageItemDescription();

    /**
     * Returns the attribution of the {@link ImageItem}.
     *
     * @return RichText with inline elements.
     */
    String getImageItemAttribution();

    /**
     * Returns the {@link Image} of this {@link ImageItem}.
     *
     * @return an {@link Image}.
     */
    Image getImageItemImage();

    /**
     * Returns the preview image of this {@link ImageItem}.
     *
     * @return a {@link StorageItem}.
     */
    StorageItem getImageItemPreviewImage();
}
