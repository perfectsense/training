package brightspot.getty;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import brightspot.image.WebImage;
import brightspot.tag.HasTagsWithFieldData;
import brightspot.tag.Tag;
import com.gettyimages.api.SdkException;
import com.psddev.cms.db.ExternalItemConverter;
import com.psddev.cms.db.Site;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.db.Query;
import com.psddev.dari.web.WebRequest;
import com.psddev.getty.GettyImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
    converting from a Getty Image to a WebImage
 */
public class GettyImageToWebImageConverter extends ExternalItemConverter<GettyImage, WebImage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GettyImage.class);

    @Override
    public Collection<? extends WebImage> convert(GettyImage gettyImage) {
        WebImage image = new WebImage();
        image.setInternalName(gettyImage.getTitle());
        image.setCaptionOverride(gettyImage.getCaption());
        image.setCreditOverride(gettyImage.getCredit());
        image.setSourceOverride(gettyImage.getEditorialSource());
        image.setCopyrightNoticeOverride(gettyImage.getCopyright());
        image.setDateTaken(gettyImage.getDateCreated());

        Set<String> keywords = gettyImage.getKeywords();

        if (!keywords.isEmpty()) {
            image.setKeywords(keywords);
            if (WebRequest.isAvailable()) {
                Site site = WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite();
                //  Auto-tag based on keywords
                image.as(HasTagsWithFieldData.class).setTags(Query.from(Tag.class).where("tag.getTagDisplayNamePlainText = ?", keywords).and(site != null ? site.itemsPredicate() : null).selectAll());
            }
        }

        try {
            image.setFile(gettyImage.generateStorageItemFromUrl(WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite()));
        } catch (IOException error) {
            LOGGER.error("Unable to write Getty Image to StorageItem!", error);
        } catch (SdkException error) {
            LOGGER.error("Error encountered while fetching largest_downloads URL from Getty API!", error);
        }

        return Collections.singletonList(image);
    }
}
