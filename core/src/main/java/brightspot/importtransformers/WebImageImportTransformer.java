package brightspot.importtransformers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import brightspot.image.WebImage;
import brightspot.importapi.ImportObjectModification;
import brightspot.importapi.ImportTransformer;
import brightspot.importtransformers.element.ImportElementUtil;
import brightspot.tag.HasTagsWithFieldData;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.PredicateParser;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import org.apache.commons.lang3.StringUtils;

public class WebImageImportTransformer extends ImportTransformer<WebImage> {

    private static final String INTERNAL_NAME_FIELD = "internalName";
    private static final String URL_FIELD = "url";
    private static final String ALT_TEXT_FIELD = "altText";
    private static final String CAPTION_FIELD = "caption";
    private static final String CREDIT_FIELD = "credit";
    private static final String SOURCE_FIELD = "source";
    private static final String COPYRIGHT_NOTICE_FIELD = "copyrightNotice";
    private static final String TAGS_FIELD = "tags";
    private static final String DATE_TAKEN_FIELD = "dateTakenTimestamp";
    private static final String DATE_UPLOADED_FIELD = "dateUploadedTimestamp";
    private static final String KEYWORDS_FIELD = "keywords";
    private static final String LOCATION_FIELD = "location";

    @JsonProperty(INTERNAL_NAME_FIELD)
    private String internalName;

    @JsonProperty(URL_FIELD)
    private String url;

    @JsonProperty(ALT_TEXT_FIELD)
    private String altText;

    @JsonProperty(CAPTION_FIELD)
    private String caption;

    @JsonProperty(CREDIT_FIELD)
    private String credit;

    @JsonProperty(SOURCE_FIELD)
    private String source;

    @JsonProperty(COPYRIGHT_NOTICE_FIELD)
    private String copyrightNotice;

    @JsonProperty(TAGS_FIELD)
    private List<TagPageImportTransformer> tagReferences;

    @JsonProperty(DATE_TAKEN_FIELD)
    private Long dateTakenTimestamp;

    @JsonProperty(DATE_UPLOADED_FIELD)
    private Long dateUploadedTimestamp;

    @JsonProperty(KEYWORDS_FIELD)
    private List<String> keywords;

    @JsonProperty(LOCATION_FIELD)
    private Location location;

    @Override
    public WebImage transform() throws Exception {

        Preconditions.checkArgument(
            StringUtils.isNotBlank(this.getUrl()),
            "url not provided for WebImage with externalId [" + this.getExternalId() + "]");

        WebImage image = new WebImage();

        String imageUrl = ImportTransformerUtil.prependFileBaseUrlIfNeeded(this.getUrl(), this.getFileBaseUrl());

        StorageItem file = Optional.ofNullable(this.createStorageItemFromUrl(imageUrl))
            .map(f -> ImportTransformerUtil.validateImageFile(f, imageUrl))
            .orElse(null);
        if (file == null) {
            return null;
        }

        image.setFile(file);

        image.setInternalName(this.getInternalName());
        image.setAltTextOverride(this.getAltText());
        image.setCaptionOverride(ImportElementUtil.processInlineRichText(this.getCaption(), this));
        image.setCreditOverride(ImportElementUtil.processInlineRichText(this.getCredit(), this));
        image.setSourceOverride(this.getSource());
        image.setCopyrightNoticeOverride(this.getCopyrightNotice());

        if (getDateTakenTimestamp() != null) {
            image.setDateTaken(new Date(this.getDateTakenTimestamp()));
        }

        if (getDateUploadedTimestamp() != null) {
            image.setDateUploaded(new Date(this.getDateUploadedTimestamp()));
        }

        image.setKeywords(new HashSet<>(this.getKeywords()));

        if (getLocation() != null) {
            com.psddev.dari.db.Location location = new com.psddev.dari.db.Location(
                this.getLocation().getLatitude(),
                this.getLocation().getLongitude());
            image.setLocation(location);
        }

        image.as(HasTagsWithFieldData.class).setTags(ImportTransformerUtil.retrieveTags(this.getTagReferences(), this));

        image.as(ImportObjectModification.class).setSourceUrl(imageUrl);

        return image;
    }

    @Override
    public Predicate getUniqueFieldPredicate() {
        if (StringUtils.isBlank(this.getUrl())) {
            return null;
        }
        return PredicateParser.Static.parse(
            ImportObjectModification.SOURCE_URL_FIELD_NAME + " = \"" + this.getUrl() + "\"");
    }

    @Override
    public String getExternalId() {
        return ObjectUtils.firstNonBlank(this.externalId, this.getIdAsString(), this.url);
    }

    public String getInternalName() {
        return internalName;
    }

    public String getUrl() {
        return url;
    }

    public String getAltText() {
        return altText;
    }

    public String getCaption() {
        return caption;
    }

    public String getCredit() {
        return credit;
    }

    public String getSource() {
        return source;
    }

    public String getCopyrightNotice() {
        return copyrightNotice;
    }

    public List<TagPageImportTransformer> getTagReferences() {
        if (tagReferences == null) {
            tagReferences = new ArrayList<>();
        }
        return tagReferences;
    }

    public Long getDateTakenTimestamp() {
        return dateTakenTimestamp;
    }

    public Long getDateUploadedTimestamp() {
        return dateUploadedTimestamp;
    }

    public List<String> getKeywords() {
        if (keywords == null) {
            keywords = new ArrayList<>();
        }
        return keywords;
    }

    public Location getLocation() {
        return location;
    }

    public static class Location {

        double latitude;
        double longitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}
