package brightspot.core.background;

import java.util.Optional;

import com.psddev.cms.image.ImageSize;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

@Recordable.DisplayName("One-Off Image")
public class OneOffImageBackground extends Background {

    @Required
    @MimeTypes("+image/")
    private StorageItem file;

    public StorageItem getFile() {
        return file;
    }

    public void setFile(StorageItem file) {
        this.file = file;
    }

    @Override
    public String getCssValue() {
        return Optional.ofNullable(getFile())
            .map(file -> "url('" + ImageSize.getUrl(file) + "')")
            .orElse(null);
    }
}
