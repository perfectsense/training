package brightspot.adobe.stock;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import brightspot.image.WebImage;
import brightspot.tag.HasTagsWithFieldData;
import brightspot.tag.Tag;
import com.psddev.adobe.stock.AdobeStockImage;
import com.psddev.cms.db.ExternalItemConverter;
import com.psddev.cms.db.Site;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.web.WebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdobeImageToWebImageConverter extends ExternalItemConverter<AdobeStockImage, WebImage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdobeImageToWebImageConverter.class);

    @Override
    public Collection<? extends WebImage> convert(AdobeStockImage adobeStockImage) {
        WebImage image = new WebImage();
        image.setInternalName(adobeStockImage.getTitle());
        image.setCreditOverride(adobeStockImage.getCreatorName());
        image.setWidth(ObjectUtils.to(Integer.class, adobeStockImage.getWidth()));
        image.setHeight(ObjectUtils.to(Integer.class, adobeStockImage.getHeight()));

        String category = adobeStockImage.getCategory();

        if (category != null) {
            Set<String> keywords = Collections.singleton(category);
            image.setKeywords(keywords);

            if (WebRequest.isAvailable()) {
                Site site = WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite();
                //  Auto-tag based on keywords
                image.as(HasTagsWithFieldData.class).setTags(Query.from(Tag.class).where("tag.getTagDisplayNamePlainText = ?", keywords).and(site != null ? site.itemsPredicate() : null).selectAll());
            }
        }

        try {
            image.setFile(adobeStockImage.generateStorageItemFromUrl());
        } catch (IOException error) {
            LOGGER.error("Unable to write Getty Image to StorageItem!", error);
        }

        return Collections.singletonList(image);

    }
}
