package brightspot.core.page.opengraph.image;

import java.util.Map;
import java.util.Optional;

import brightspot.core.image.ImageOption;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.image.ImageSize;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;
import com.psddev.styleguide.facebook.OpenGraphImageMetaView;

/**
 * As a catch-all for content that are not handleable with the other viewmodels, in order to avoid warning.
 */
public class OpenGraphImageMetaViewModel<T extends Recordable> extends ViewModel<T> implements OpenGraphImageMetaView {

    @CurrentSite
    protected Site site;

    private ImageOption imageOption;

    private StorageItem image;

    @Override
    protected void onCreate(ViewResponse response) {
        imageOption = SiteSettings.get(
            site,
            s -> s.as(OpenGraphImageSettingsModification.class).getDefaultOpenGraphImage());
        image = imageOption != null ? imageOption.getImageOptionFile() : null;
    }

    protected ImageOption getImageOption() {
        return imageOption;
    }

    protected StorageItem getImageFile() {
        return Optional.ofNullable(getImageOption())
            .map(ImageOption::getImageOptionFile)
            .orElse(image);
    }

    @Override
    public Map<String, ?> getImage() {
        return addAdditionalAttributes(ImageSize.getAttributes(getImageFile()));
    }

    @Override
    public CharSequence getAlt() {
        return Optional.ofNullable(getImageOption())
            .map(ImageOption::getImageOptionAltText)
            .orElse(null);
    }

    @Override
    public CharSequence getMimeType() {
        return Optional.ofNullable(image)
            .map(StorageItem::getContentType)
            .orElse("image");
    }

    public static Map<String, String> addAdditionalAttributes(Map<String, String> attributeMap) {
        if (attributeMap != null) {
            String url = attributeMap.get(ImageSize.SRC_ATTRIBUTE);
            if (url != null && url.startsWith("https://")) {
                attributeMap.put("srcsecure", url);
            }
        }
        return attributeMap;
    }
}
