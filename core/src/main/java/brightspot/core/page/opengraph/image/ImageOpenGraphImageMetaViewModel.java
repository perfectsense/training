package brightspot.core.page.opengraph.image;

import brightspot.core.image.Image;
import brightspot.core.image.ImageOption;
import com.psddev.dari.util.StorageItem;

public class ImageOpenGraphImageMetaViewModel extends OpenGraphImageMetaViewModel<Image> {

    @Override
    protected ImageOption getImageOption() {
        return null;
    }

    @Override
    protected StorageItem getImageFile() {
        return model.getFile();
    }
}
