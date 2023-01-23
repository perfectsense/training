package brightspot.ap;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import brightspot.image.WebImage;
import com.psddev.ap.AssociatedPressImage;
import com.psddev.cms.db.ExternalItemConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssociatedPressImageToWebImageConverter extends ExternalItemConverter<AssociatedPressImage, WebImage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssociatedPressImageToWebImageConverter.class);

    @Override
    public Collection<? extends WebImage> convert(AssociatedPressImage associatedPressImage) {
        WebImage image = new WebImage();

        image.setInternalName(associatedPressImage.getTitle());
        image.setCaptionOverride(associatedPressImage.getCaption());
        image.setCreditOverride(associatedPressImage.getCreditLine());
        image.setSourceOverride(associatedPressImage.getProvider());
        image.setCopyrightNoticeOverride(associatedPressImage.getCopyrightNotice());

        try {
            image.setFile(associatedPressImage.generateStorageItemFromUrl());
        } catch (IOException error) {
            LOGGER.error("Could not download file", error);
        }

        return Collections.singleton(image);
    }
}
