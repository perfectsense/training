package brightspot.shutterstock;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import brightspot.image.WebImage;
import brightspot.tag.HasTagsWithFieldData;
import brightspot.tag.Tag;
import com.psddev.cms.db.ExternalItemConverter;
import com.psddev.cms.db.Site;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.db.Query;
import com.psddev.dari.web.WebRequest;
import com.psddev.shutterstock.ShutterstockImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutterstockImageToWebImageConverter extends ExternalItemConverter<ShutterstockImage, WebImage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutterstockImage.class);

    @Override
    public Collection<? extends WebImage> convert(ShutterstockImage shutterstockImage) {
        WebImage image = new WebImage();
        image.setCaptionOverride(shutterstockImage.getDescription());
        image.setDateTaken(shutterstockImage.getDateAdded());

        Set<String> keywords = shutterstockImage.getKeywords();

        if (!keywords.isEmpty()) {
            image.setKeywords(keywords);

            if (WebRequest.isAvailable()) {
                Site site = WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite();
                //  Auto-tag based on keywords
                image.as(HasTagsWithFieldData.class).setTags(Query.from(Tag.class).where("tag.getTagDisplayNamePlainText = ?", keywords).and(site != null ? site.itemsPredicate() : null).selectAll());
            }
        }

        try {
            image.setFile(shutterstockImage.generateStorageItemFromUrl());
        } catch (IOException error) {
            LOGGER.error("Unable to write Getty Image to StorageItem!", error);
        }

        return Collections.singletonList(image);

    }
}
