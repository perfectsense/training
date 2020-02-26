package brightspot.core.pkg;

import brightspot.core.image.ImageOption;
import com.psddev.dari.db.Record;

public class ImageBanner extends Record implements PackageBanner {

    @Required
    private ImageOption image;

    public ImageOption getImage() {
        return image;
    }

    public void setImage(ImageOption image) {
        this.image = image;
    }
}
