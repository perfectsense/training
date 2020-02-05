package brightspot.core.image;

import java.util.Optional;

import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

@Recordable.DisplayName("Shared")
@Recordable.PreviewField("image/file")
public class SharedImageOption extends ImageOption {

    @Required
    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public StorageItem getImageOptionFile() {
        return Optional.ofNullable(getImage())
            .map(Image::getFile)
            .orElse(null);
    }

    @Override
    public String getImageOptionAltText() {
        return Optional.ofNullable(getImage())
            .map(Image::getAltText)
            .orElse(null);
    }

    public static SharedImageOption create(Image image) {

        if (image != null) {
            SharedImageOption imageOption = new SharedImageOption();
            imageOption.setImage(image);

            return imageOption;
        }
        return null;
    }
}
