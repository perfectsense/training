package brightspot.core.imageitemstream;

import java.util.Optional;

import brightspot.core.gallery.Slide;
import brightspot.core.image.Image;
import com.psddev.dari.db.StateValueAdapter;

public class SlideToImageItemPromo implements StateValueAdapter<Slide, ImageItemPromo> {

    @Override
    public ImageItemPromo adapt(Slide source) {

        if (source != null) {

            Image item = Optional.ofNullable(source.getContent())
                .filter(Image.class::isInstance)
                .map(Image.class::cast)
                .orElse(null);

            if (item != null) {

                ImageItemPromo imageItemPromo = new ImageItemPromo();

                imageItemPromo.setItem(item);
                imageItemPromo.setTitle(source.getTitle());
                imageItemPromo.setDescription(source.getDescription());

                return imageItemPromo;
            }
        }

        return null;
    }
}
