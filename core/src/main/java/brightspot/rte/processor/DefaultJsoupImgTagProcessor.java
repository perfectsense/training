package brightspot.rte.processor;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import brightspot.dam.asset.AssetType;
import brightspot.image.WebImage;
import brightspot.rte.image.ImageRichTextElement;
import brightspot.rte.importer.html.jsoup.JsoupHtmlTagProcessor;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ExternalItemImported;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUser;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.UrlStorageItem;
import com.psddev.dari.web.WebRequest;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convert {@code <img>} tags to bsp-image tags that can be parsed into {@link ImageRichTextElement}s by the RTE.
 */
public class DefaultJsoupImgTagProcessor implements JsoupHtmlTagProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJsoupImgTagProcessor.class);

    private static final String DATA_ATTR = "data-state";

    private static final String TAG = "img";

    private static final String IMAGE_INGESTED_DATE = "bsp.rteImporter.imageIngestedDate";

    private static final String IMAGE_INGESTED_URL = "bsp.rteImporter.imageIngestedUrl";

    private static final String METADATA_FILE_TYPE_KEY = "File Type";

    private static final String METADATA_FILE_EXTENSION_KEY = "Expected File Name Extension";

    @Override
    public String getHtmlTag() {
        return TAG;
    }

    @Override
    public String head(Element element, int depth) {
        return null;
    }

    @Override
    public String tail(Element element, int depth) {
        if (element == null) {
            return null;
        }

        ImageRichTextElement imageRTE = new ImageRichTextElement();
        try {
            imageRTE.setImage(getOrCreateImage(element));

            Element bspImage = new Element(Tag.valueOf(ImageRichTextElement.TAG_NAME), "");
            bspImage.attr(DATA_ATTR, imageRTE.toAttributes().get(DATA_ATTR));
            bspImage.text(imageRTE.toBody());

            return bspImage.outerHtml();
        } catch (Exception e) {
            LOGGER.error("Failed to fetch image {} due to {}", element.attr("abs:src"), e.getMessage());
        }

        return element.outerHtml();
    }

    private WebImage getOrCreateImage(Element element) {
        String imageUrl = element.attr("abs:src");
        String altText = element.attr("alt");
        String checksum = element.attr("checksum");

        if (StringUtils.isBlank(imageUrl)) {

            throw new IllegalArgumentException("Image url cannot be blank");
        }

        WebImage image = Query.from(WebImage.class)
            .where("cms.externalItemImported.itemId = ?", checksum)
            .findFirst()
            .orElseGet(WebImage::new);

        Site site = WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite();
        ToolUser user = WebRequest.getCurrent().as(ToolRequest.class).getCurrentUser();

        image.setAltTextOverride(!StringUtils.isBlank(altText) ? altText : null);
        StorageItem file = createImageStorageItem(element);
        image.setFile(file);

        // Gets expected file extension from metadata
        String fileExtension = Optional.ofNullable(file.getMetadata())
            .map(metadata -> metadata.get(METADATA_FILE_TYPE_KEY))
            .filter(Map.class::isInstance)
            .map(Map.class::cast)
            .map(fileMetadata -> fileMetadata.get(METADATA_FILE_EXTENSION_KEY))
            .filter(String.class::isInstance)
            .map(String.class::cast)
            .map(extension -> "." + extension)
            .orElse("");

        // Updates internal name to include file extension
        Optional.of(image.getFile())
            .map(AssetType::getFileNameFromStoragePath)
            .map(fileName -> fileName + fileExtension)
            .ifPresent(image::setInternalName);

        image.as(ExternalItemImported.class).setItemId(checksum);
        image.as(Site.ObjectModification.class).setOwner(site);
        image.as(Content.ObjectModification.class).setUpdateUser(user);

        // Store metadata in state
        State imageState = image.getState();
        imageState.put(IMAGE_INGESTED_URL, imageUrl);
        imageState.put(IMAGE_INGESTED_DATE, new Date());

        image.save();

        return image;
    }

    // Creates a new StorageItem instance with values from the original instance. This prevents having to make duplicate
    // S3 objects referencing the same data.
    private StorageItem createImageStorageItem(Element element) {
        StorageItem storageItem;
        if (element.hasAttr("data-storage")) {
            storageItem = StorageItem.Static.createIn(element.attr("data-storage"));
            storageItem.setPath(element.attr("data-path"));
        } else {
            // Create a UrlStorageItem using image src if no storage was set
            storageItem = new UrlStorageItem();
            storageItem.setPath(element.attr("abs:src"));
        }

        // Set fields from original StorageItem instance
        storageItem.setContentType(element.attr("data-contentType"));
        Object metadata = ObjectUtils.fromJson(element.attr("data-metadata"));
        if (metadata instanceof Map) {
            storageItem.setMetadata((Map<String, Object>) metadata);
        }

        return storageItem;
    }
}
