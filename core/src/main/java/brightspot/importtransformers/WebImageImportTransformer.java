package brightspot.importtransformers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import brightspot.image.WebImage;
import brightspot.importapi.ImportTransformer;
import brightspot.tag.HasTagsWithFieldData;
import brightspot.tag.TagPage;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.Utils;

public class WebImageImportTransformer extends ImportTransformer<WebImage> {

    private String internalName;

    private String url;

    private String altText;

    private String caption;

    private String credit;

    private String source;

    private String copyrightNotice;

    private Set<String> tags;

    private Long dateTakenTimestamp;

    private Long dateUploadedTimestamp;

    private List<String> keywords;

    private Location location;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCopyrightNotice() {
        return copyrightNotice;
    }

    public void setCopyrightNotice(String copyrightNotice) {
        this.copyrightNotice = copyrightNotice;
    }

    public Set<String> getTags() {
        if (tags == null) {
            tags = new HashSet<>();
        }
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Long getDateTakenTimestamp() {
        return dateTakenTimestamp;
    }

    public void setDateTakenTimestamp(Long dateTakenTimestamp) {
        this.dateTakenTimestamp = dateTakenTimestamp;
    }

    public Long getDateUploadedTimestamp() {
        return dateUploadedTimestamp;
    }

    public void setDateUploadedTimestamp(Long dateUploadedTimestamp) {
        this.dateUploadedTimestamp = dateUploadedTimestamp;
    }

    public List<String> getKeywords() {
        if (keywords == null) {
            keywords = new ArrayList<>();
        }
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public WebImage transform() throws Exception {

        WebImage image = new WebImage();
        StorageItem file = createStorageItemFromUrl(getUrl());
        image.setFile(file);

        image.setInternalName(getInternalName());
        image.setAltTextOverride(getAltText());
        image.setCaptionOverride(caption);
        image.setCreditOverride(credit);
        image.setSourceOverride(source);
        image.setCopyrightNoticeOverride(copyrightNotice);
        if (getDateTakenTimestamp() != null) {
            image.setDateTaken(new Date(getDateTakenTimestamp()));
        }
        if (getDateUploadedTimestamp() != null) {
            image.setDateUploaded(new Date(getDateUploadedTimestamp()));
        }
        image.setKeywords(new HashSet<>(getKeywords()));

        if (getLocation() != null) {
            com.psddev.dari.db.Location location = new com.psddev.dari.db.Location(getLocation().getLatitude(), getLocation().getLongitude());
            image.setLocation(location);
        }

        for (String tagName : getTags()) {
            TagPage tag = findByExternalId(TagPage.class, tagName, (tn) -> {
                TagPage t = new TagPage();
                t.setDisplayName(Utils.toLabel(tn));
                return t;
            });
            image.as(HasTagsWithFieldData.class).getTags().add(tag);
        }

        return image;
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
