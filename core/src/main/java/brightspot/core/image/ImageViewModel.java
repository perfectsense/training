package brightspot.core.image;

import java.util.Map;

import com.psddev.cms.image.ImageSize;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.image.ImageView;

public class ImageViewModel extends ViewModel<Image> implements ImageView {

    @Override
    public Map<String, ?> getImage() {
        return ImageSize.getAttributes(model.getFile());
    }

    @Override
    public Map<String, ?> getNarrowImage() {
        return ImageSize.getAttributes(model.getFile());
    }

    @Override
    public CharSequence getAlt() {
        // Plain text
        return model.getAltText();
    }
}
