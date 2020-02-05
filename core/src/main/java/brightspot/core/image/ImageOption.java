package brightspot.core.image;

import java.util.Map;

import brightspot.core.tool.DefaultImplementationSupplier;
import com.psddev.cms.image.ImageSize;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

@Recordable.Embedded
public abstract class ImageOption extends Record {

    public abstract StorageItem getImageOptionFile();

    public abstract String getImageOptionAltText();

    public Map<String, String> getAttributes() {
        StorageItem file = getImageOptionFile();
        return file != null ? ImageSize.getAttributes(file) : null;
    }

    public static ImageOption createDefault() {
        return DefaultImplementationSupplier.createDefault(ImageOption.class, SharedImageOption.class);
    }

    @Override
    public String getLabel() {
        return getImageOptionAltText();
    }
}
