package brightspot.core.image;

import brightspot.core.lead.Lead;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

@Recordable.DisplayName("One-Off Image")
@Recordable.PreviewField("file")
public class OneOffImageOption extends ImageOption implements Lead {

    @Required
    @MimeTypes("+image/")
    private StorageItem file;

    private String altText;

    public StorageItem getFile() {
        return file;
    }

    public void setFile(StorageItem file) {
        this.file = file;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    @Override
    public StorageItem getImageOptionFile() {
        return getFile();
    }

    @Override
    public String getImageOptionAltText() {
        return getAltText();
    }

    @Override
    public ImageOption getLeadImage() {
        return this;
    }

    public static OneOffImageOption create(Image image) {

        if (image != null && image.getFile() != null) {
            OneOffImageOption imageOption = new OneOffImageOption();
            imageOption.setFile(image.getFile());
            imageOption.setAltText(image.getAltText());

            return imageOption;
        }
        return null;
    }
}
