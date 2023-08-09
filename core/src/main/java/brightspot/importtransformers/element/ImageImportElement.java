package brightspot.importtransformers.element;

import java.io.IOException;
import java.util.Optional;

import brightspot.image.WebImage;
import brightspot.importapi.ImportObjectModification;
import brightspot.importapi.ImportTransformer;
import brightspot.importapi.ImportingDatabase;
import brightspot.importapi.element.ImportElement;
import brightspot.importtransformers.ImportTransformerUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.StorageItem;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An import element to convert from a legacy Iframe into suitable BSP Rich Text Embed. Matches to '_type': RichText
 */
public class ImageImportElement extends ImportElement {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageImportElement.class);

    private static final String SRC_FIELD = "src";
    private static final String ALT_TEXT_FIELD = "altText";
    private static final String CREDIT_FIELD = "credit";
    private static final String CAPTION_FIELD = "caption";
    private static final String WIDTH_FIELD = "width";
    private static final String HEIGHT_FIELD = "height";

    @JsonProperty(SRC_FIELD)
    private String src;

    @JsonProperty(ALT_TEXT_FIELD)
    private String altText;

    @JsonProperty(CREDIT_FIELD)
    private String credit;

    @JsonProperty(CAPTION_FIELD)
    private String caption;

    @JsonProperty(WIDTH_FIELD)
    private String width;

    @JsonProperty(HEIGHT_FIELD)
    private String height;

    @Override
    public String transform(ImportTransformer<?> transformer) {

        if (StringUtils.isBlank(this.getSrc())) {
            return null;
        }

        Site importSite = ((ImportingDatabase) Database.Static.getDefault()).getSite();

        WebImage image = ImportTransformer.findByExternalId(WebImage.class, this.getSrc(), url -> {
            try {
                String imageUrl = ImportTransformerUtil.prependFileBaseUrlIfNeeded(
                    this.getSrc(),
                    transformer.getFileBaseUrl());

                WebImage w = Query.from(WebImage.class)
                    .where(ImportObjectModification.SOURCE_URL_FIELD_NAME + " = " + imageUrl)
                    .and("cms.site.owner = ?", importSite)
                    .first();
                if (w != null) {
                    return w;
                }

                w = new WebImage();

                StorageItem file = Optional.ofNullable(transformer.createStorageItemFromUrl(imageUrl))
                    .map(f -> ImportTransformerUtil.validateImageFile(f, imageUrl))
                    .orElse(null);
                if (file == null) {
                    return null;
                }

                w.setFile(file);

                w.setAltTextOverride(this.getAltText());
                w.setCreditOverride(this.getCredit());
                w.setCaptionOverride(this.getCaption());

                Optional.ofNullable(this.getHeight())
                    .map(ImportElementUtil::parseInteger)
                    .ifPresent(w::setHeight);

                Optional.ofNullable(this.getWidth())
                    .map(ImportElementUtil::parseInteger)
                    .ifPresent(w::setWidth);

                w.as(ImportObjectModification.class).setSourceUrl(imageUrl);
                return w;

            } catch (IOException ex) {
                LOGGER.error("Failed to fetch image {} due to {}", this.getSrc(), ex.getMessage());
                return null;
            }
        });

        if (image == null) {
            return null;
        }

        Element bspImage = ImportElementUtil.createImgElement(
            image,
            this.getAltText(),
            this.getCaption(),
            this.getCredit());
        if (bspImage == null) {
            return null;
        }

        return bspImage.toString();
    }

    public String getSrc() {
        return src;
    }

    public String getAltText() {
        return altText;
    }

    public String getCredit() {
        return credit;
    }

    public String getCaption() {
        return caption;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }
}
