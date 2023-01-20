package brightspot.sailthru;

import java.util.Collections;
import java.util.Objects;

import com.psddev.cms.image.ImageSize;
import com.psddev.cms.image.ImageSizeBuilder;

public enum SailthruImageSizes {

    //Sailthru doesn't document what sizes they actually want, so this is a guess
    THUMBNAIL("Sailthru Thumbnail", 2000, 1000, true),
    FULL("Sailthru Full", 1024, 1000, false);

    private final ImageSize imageSize;

    SailthruImageSizes(String name, int width, int height, boolean crop) {

        Objects.requireNonNull(name, "Image size name can not be null!");

        ImageSizeBuilder imageSizeBuilder = ImageSize.builder()
            .displayName(name)
            .internalName(name);

        if (crop) {

            imageSizeBuilder.width(width).height(height);
        } else {

            imageSizeBuilder.maximumWidth(width).maximumHeight(height);
        }

        imageSizeBuilder.alternateFormats(Collections.singletonList("webp"));

        this.imageSize = imageSizeBuilder.build();
    }

    public ImageSize getImageSize() {

        return imageSize;
    }
}
