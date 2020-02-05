package brightspot.core.imageitemstream;

import java.util.Optional;

import brightspot.core.carousel.CarouselSlide;
import brightspot.core.carousel.ImageCarouselSlide;
import brightspot.core.image.Image;
import brightspot.core.image.ImageOption;
import brightspot.core.image.OneOffImageOption;
import brightspot.core.image.SharedImageOption;
import com.psddev.dari.db.StateValueAdapter;

public class CarouselSlideToImageItemPromo implements StateValueAdapter<CarouselSlide, ImageItemPromo> {

    @Override
    public ImageItemPromo adapt(CarouselSlide source) {

        if (source != null && source instanceof ImageCarouselSlide) {

            ImageCarouselSlide slide = (ImageCarouselSlide) source;

            Image item = Optional.ofNullable(slide.getImage())
                .map(this::getImageOptionImage)
                .orElse(null);

            if (item != null) {

                ImageItemPromo imageItemPromo = new ImageItemPromo();

                imageItemPromo.setItem(item);
                imageItemPromo.setDescription(slide.getCaption());
                imageItemPromo.setAttribution(slide.getCredit());

                return imageItemPromo;
            }
        }

        return null;
    }

    private Image getImageOptionImage(ImageOption imageOption) {

        if (imageOption != null) {

            if (imageOption instanceof SharedImageOption) {

                return ((SharedImageOption) imageOption).getImage();

            } else if (imageOption instanceof OneOffImageOption) {

                Image image = new Image();

                image.setFile(imageOption.getImageOptionFile());
                image.setTitle(imageOption.getImageOptionAltText());
                image.setAltText(imageOption.getImageOptionAltText());

                return image;
            }

        }

        return null;
    }
}
