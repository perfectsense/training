package brightspot.core.background;

import java.util.Optional;

import brightspot.core.image.Image;
import com.psddev.cms.image.ImageSize;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Shared Image")
public class SharedImageBackground extends Background {

    @Required
    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String getCssValue() {
        return Optional.ofNullable(getImage())
            .map(Image::getFile)
            .map(file -> "url('" + ImageSize.getUrl(file) + "')")
            .orElse(null);
    }
}
