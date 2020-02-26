package brightspot.core.image;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import brightspot.core.asset.AbstractAsset;
import brightspot.core.asset.AssetMimeTypes;
import brightspot.core.gallery.SlideContent;
import brightspot.core.imageitemstream.ImageItem;
import brightspot.core.lead.LeadItem;
import brightspot.core.permalink.AbstractPermalinkRule;
import brightspot.core.permalink.NoPermalinkRule;
import brightspot.core.permalink.PermalinkRuleSettings;
import brightspot.core.promo.Promotable;
import brightspot.core.tag.Taggable;
import brightspot.core.tool.RichTextUtils;
import brightspot.core.tool.SmallRichTextToolbar;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ContentEditWidgetDisplay;
import com.psddev.cms.tool.content.UrlsWidget;
import com.psddev.dari.db.ColorImage;
import com.psddev.dari.db.ConditionallyValidatable;
import com.psddev.dari.db.Location;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;
import com.psddev.image.Describable;

@AssetMimeTypes(ImageMimeTypesProvider.class)
@Recordable.PreviewField("file")
@ToolUi.Publishable(false)
@ToolUi.IconName(Image.ICON_NAME)
@ToolUi.SearchShortcut(shortcut = "cp", field = "getCaption")
@ToolUi.SearchShortcut(shortcut = "cr", field = "getCredit")
@ToolUi.SearchShortcut(shortcut = "sr", field = "getSource")
@ToolUi.FieldDisplayPreview({
        "title",
        "caption",
        "credit",
        "source",
        "copyrightNotice",
        "taggable.tags",
        "cms.content.updateDate",
        "cms.content.updateUser"})
public class Image extends AbstractAsset implements
    ColorImage,
    ContentEditWidgetDisplay,
    Directory.Item,
    ImageItem,
    LeadItem,
    ConditionallyValidatable,
    Describable,
    Promotable,
    SlideContent,
    Taggable {

    public static final String ICON_NAME = "photo";
    public static final String PROMOTABLE_TYPE = "image";
    public static final String TITLE_FIELD = "getTitle";

    static final String[] VIEWABLE_MIME_TYPES = {
        "image/bmp",
        "image/gif",
        "image/jpeg",
        "image/jpg",
        "image/png",
        "image/x-png",
        "image/svg+xml" };

    @ToolUi.Placeholder(dynamicText = "${content.altFallback}", editable = true)
    private String altText;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    @ToolUi.Placeholder(dynamicText = "${content.captionFallback}", editable = true)
    private String caption;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    @ToolUi.Placeholder(dynamicText = "${content.creditFallback}", editable = true)
    private String credit;

    @ToolUi.Placeholder(dynamicText = "${content.sourceFallback}", editable = true)
    private String source;

    @ToolUi.Placeholder(dynamicText = "${content.copyrightFallback}", editable = true)
    private String copyrightNotice;

    private Date dateTaken;

    @Indexed
    @ToolUi.ReadOnly
    @ToolUi.Hidden
    private Integer width;

    @Indexed
    @ToolUi.ReadOnly
    @ToolUi.Hidden
    private Integer height;

    @Indexed
    @ToolUi.Tab("Location")
    private Location location;

    @Indexed
    @ToolUi.Hidden
    public String getCaption() {
        if (StringUtils.isBlank(caption)) {
            return getCaptionFallback();
        }
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Indexed
    @ToolUi.Hidden
    public String getCredit() {
        if (StringUtils.isBlank(credit)) {
            return getCreditFallback();
        }
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getAltText() {
        if (StringUtils.isBlank(altText)) {
            return getAltFallback();
        }
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public String getCopyrightNotice() {
        if (StringUtils.isBlank(copyrightNotice)) {
            return getCopyrightFallback();
        }
        return copyrightNotice;
    }

    public void setCopyrightNotice(String copyrightNotice) {
        this.copyrightNotice = copyrightNotice;
    }

    @Indexed
    @ToolUi.Hidden
    public String getSource() {
        if (StringUtils.isBlank(source)) {
            return getSourceFallback();
        }
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public StorageItemImageMetadata getFileMetadata() {
        return new StorageItemImageMetadata(getFile());
    }

    // --- ColorImage support ---

    @Override
    public StorageItem getColorImage() {
        return getFile();
    }

    // --- GallerySlideItem support ---

    @Override
    public String getGallerySlideContentTitle() {
        return getTitle();
    }

    @Override
    public String getGallerySlideContentDescription() {
        return getCaption();
    }

    @Override
    public StorageItem getGallerySlideContentImageFile() {
        return getFile();
    }

    @Override
    public ImageOption getLeadItemImage() {
        return getPromotableImage();
    }

    // --- Private Helpers ---

    /**
     * Not for external use!
     */
    @Override
    public String getTitleFallback() {
        return ObjectUtils.firstNonBlank(
            getFileMetadata().getTitle(),
            getFileMetadata().getOriginalFilePath(),
            getFileMetadata().getOriginalFileName(),
            getFileNameFromStoragePath());
    }

    /**
     * Not for external use!
     */
    public String getCaptionFallback() {
        return getFileMetadata().getCaption();
    }

    /**
     * Not for external use!
     */
    public String getCreditFallback() {
        String byline = getFileMetadata().getByline();
        String source = getFileMetadata().getCredit();

        if (byline != null) {
            if (source != null) {
                return byline + "/" + source;

            } else {
                return byline;
            }
        } else {
            return source;
        }
    }

    /**
     * Not for external use!
     */
    public String getSourceFallback() {
        return getFileMetadata().getSource();
    }

    /**
     * Not for external use!
     */
    public String getCopyrightFallback() {
        return getFileMetadata().getCopyrightNotice();
    }

    /**
     * Not for external use!
     */
    public String getAltFallback() {
        return getTitle();
    }

    private Date getDateTakenFallback() {
        return getFileMetadata().getDateTaken();
    }

    /**
     * Not for external use!
     */
    public Location getLocationFallback() {
        return getFileMetadata().getGpsLocation();
    }

    @Override
    protected void beforeCommit() {

        if (location == null) {
            location = getLocationFallback();
        }

        if (getFile() != null) {
            // hack to work around BSP-2976
            Map<String, Object> metadata = getFile().getMetadata();
            if (!metadata.containsKey("cms.crops")) {
                metadata.put("cms.crops", new HashMap<>());
            }
            if (!metadata.containsKey("cms.focus")) {
                metadata.put("cms.focus", new HashMap<>());
            }
            if (!metadata.containsKey("cms.edits")) {
                metadata.put("cms.edits", new HashMap<>());
            }
        }
    }

    @Override
    protected void beforeSave() {
        if (getFile() != null) {
            if (dateTaken == null) {
                setDateTaken(getDateTakenFallback());
            }
            if (getKeywords().isEmpty()) {
                getKeywords().addAll(getFileMetadata().getKeywords());
            }

            setWidth(getFileMetadata().getWidth());
            setHeight(getFileMetadata().getHeight());
        }

        super.beforeSave();
    }

    @Override
    public String getPromotableTitle() {
        return getTitle();
    }

    @Override
    public ImageOption getPromotableImage() {
        StorageItem file = getFile();

        if (file == null || Stream.of(VIEWABLE_MIME_TYPES)
            .noneMatch(mimeType -> mimeType.equals(file.getContentType()))) {
            return null;
        }

        SharedImageOption shared = new SharedImageOption();
        shared.setImage(this);
        return shared;
    }

    @Override
    public String getPromotableDescription() {
        return RichTextUtils.richTextToPlainText(getCaption());
    }

    @Override
    public String getPromotableType() {
        return PROMOTABLE_TYPE;
    }

    @Override
    public String getGallerySlideContentAttribution() {
        return getCredit();
    }

    @Override
    public String getImageItemTitle() {
        return getTitle();
    }

    @Override
    public String getImageItemDescription() {
        return getCaption();
    }

    @Override
    public String getImageItemAttribution() {
        return getCredit();
    }

    @Override
    public Image getImageItemImage() {
        return this;
    }

    @Override
    public StorageItem getImageItemPreviewImage() {
        return getFile();
    }

    @Override
    public boolean shouldValidate(ObjectField objectField) {
        return !("title".equals(objectField.getInternalName())
            && !ObjectUtils.isBlank(getTitleFallback()));
    }

    @Override
    public StorageItem getDescribableImage() {
        return getFile();
    }

    /**
     * Returns a generated permalink from a CMS-configured {@link AbstractPermalinkRule} implementation for Image if it
     * exists, otherwise returns {@code null}.
     */
    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, (s) -> null);
    }

    /**
     * Conditionally displays {@link UrlsWidget} if a mapped {@link AbstractPermalinkRule} implementation for Image
     * exists in global settings.
     */
    @Override
    public boolean shouldDisplayContentEditWidget(String widgetName) {
        if (UrlsWidget.class.getName().equals(widgetName)) {
            AbstractPermalinkRule permalinkRule = PermalinkRuleSettings.get(new Image());
            return permalinkRule != null && !(permalinkRule instanceof NoPermalinkRule);
        }
        return true;
    }
}
