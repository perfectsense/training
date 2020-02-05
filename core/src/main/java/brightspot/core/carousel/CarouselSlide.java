package brightspot.core.carousel;

import brightspot.core.image.ImageOption;
import com.psddev.dari.db.Recordable;

@Deprecated
public interface CarouselSlide extends Recordable {

    ImageOption getPreviewImage();
}
