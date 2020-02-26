package brightspot.core.image;

import java.util.Map;
import java.util.Optional;

import com.psddev.cms.image.ImageSize;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.ViewModelOverlayValueEntryView;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.jsonld.JsonLdNode;
import com.psddev.cms.view.jsonld.JsonLdType;
import com.psddev.dari.util.StorageItem;
import com.psddev.styleguide.core.image.ImageView;

@JsonLdType("ImageObject")
public class ImageOptionViewModel extends ViewModel<ImageOption> implements ImageView, ViewModelOverlayValueEntryView {

    Optional<StorageItem> imageOptionFile;

    @Override
    protected void onCreate(ViewResponse response) {
        super.onCreate(response);
        imageOptionFile = Optional.ofNullable(model.getImageOptionFile());
    }

    @Override
    public Map<String, ?> getImage() {
        return imageOptionFile
            .map(e -> ImageSize.getAttributes(e))
            .orElse(null);
    }

    @Override
    public Map<String, ?> getNarrowImage() {
        return imageOptionFile
            .map(e -> ImageSize.getAttributes(e))
            .orElse(null);
    }

    @Override
    public CharSequence getAlt() {
        // Plain text
        return model.getImageOptionAltText();
    }

    @JsonLdNode("url")
    public CharSequence getUrlData() {
        return imageOptionFile
            .map(StorageItem::getPublicUrl)
            .orElse(null);
    }
}
