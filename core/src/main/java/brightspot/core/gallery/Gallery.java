package brightspot.core.gallery;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brightspot.core.creativework.CreativeWork;
import brightspot.core.image.Image;
import brightspot.core.image.ImageOption;
import brightspot.core.image.OneOffImageOption;
import brightspot.core.imageitemstream.AdvancedImageItemStream;
import brightspot.core.imageitemstream.ImageItem;
import brightspot.core.imageitemstream.ImageItemPromo;
import brightspot.core.imageitemstream.SimpleImageItemStream;
import brightspot.core.imageitemstream.SlideToImageItemPromo;
import brightspot.core.link.Linkable;
import brightspot.core.module.ModuleType;
import brightspot.core.page.Page;
import brightspot.core.permalink.AbstractPermalinkRule;
import brightspot.core.person.Authorable;
import brightspot.core.pkg.Packageable;
import brightspot.core.promo.PromotableWithOverrides;
import brightspot.core.readreceipt.Readable;
import brightspot.core.section.SectionPrefixPermalinkRule;
import brightspot.core.section.Sectionable;
import brightspot.core.share.Shareable;
import brightspot.core.site.ExpressSiteMapItem;
import brightspot.core.tag.Taggable;
import brightspot.core.tool.LargeRichTextToolbar;
import brightspot.corporate.tag.CorporateTaggable;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.SearchResultSelection;
import com.psddev.cms.tool.SearchResultSelectionGeneratable;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Recordable.PreviewField("getPreviewFile")
@SearchResultSelectionGeneratable.ItemTypes(ImageItem.class)
@ToolUi.FieldDisplayOrder({
    "headline",
    "subHeadline",
    "authorable.authors",
    "authorable.byline",
    // Main Tab
    "body",
    "items",
    "slides",
    "endCard",
    "sectionable.section",
    "packageable.pkg",
    "taggable.tags",
    "taggable.tagging",
    // Main Tab
    "promotable.promoTitle",
    "promotable.promoDescription",
    "promotable.promoImage",
    "shareable.shareTitle",
    "shareable.shareDescription",
    "shareable.shareImage",
    "page.layoutOption" }) // Overrides Tab
@ToolUi.FieldDisplayPreview({
    "promotable.promoImage",
    "headline",
    "subheadline",
    "sectionable.section",
    "taggable.tags",
    "cms.content.updateDate",
    "cms.content.updateUser"})
@ToolUi.IconName(Gallery.ICON_NAME)
public class Gallery extends CreativeWork implements
    Authorable,
    Directory.Item,
    CorporateTaggable,
    ExpressSiteMapItem,
    Linkable,
    Packageable,
    Page,
    PromotableWithOverrides,
    Readable,
    SearchResultSelectionGeneratable,
    Shareable,
    Sectionable,
    Taggable {

    public static final String ICON_NAME = "photo_library";
    public static final String PROMOTABLE_TYPE = "gallery";
    private static final Logger LOGGER = LoggerFactory.getLogger(Gallery.class);

    @ToolUi.RichText(inline = false, toolbar = LargeRichTextToolbar.class)
    private String body;

    @Required
    @Embedded
    private GalleryItemStream items = GalleryItemStream.createDefault();

    @Deprecated
    @ToolUi.ReadOnly
    @ToolUi.Hidden
    private List<Slide> slides;

    @ToolUi.Hidden
    private ModuleType endCard;

    public GalleryItemStream getItemStream() {

        replaceSlidesWithItemStream(true);
        return items;
    }

    public void setItemStream(GalleryItemStream items) {
        this.items = items;
    }

    public ModuleType getEndCard() {
        return endCard;
    }

    public void setEndCard(ModuleType endCard) {
        this.endCard = endCard;
    }

    /**
     * Not for public use.
     *
     * Retrieves {@link StorageItem} of first {@link StorageItem} present in {@link #slides}.
     *
     * @return May be {@code null}.
     */
    @Ignored(false)
    @ToolUi.Hidden
    public StorageItem getPreviewFile() {

        return getFirstImage()
            .map(Image::getFile)
            .orElse(null);
    }

    // *** Promotable implementation ***
    @Override
    public String getPromotableTitleFallback() {
        return getHeadline();
    }

    @Override
    public String getPromotableDescriptionFallback() {
        return getSubHeadline();
    }

    @Override
    public ImageOption getPromotableImageFallback() {
        return getFirstImageOption()
            .orElse(null);
    }

    @Override
    public String getPromotableType() {
        return PROMOTABLE_TYPE;
    }

    @Override
    public String getPromotableDuration() {

        long count = getItemStream() == null ? 0 : getItemStream().getCount(getOwner(), this);
        // TODO: Localization of label needed
        return count + " Images";
    }

    // *** Shareable implementation ***
    @Override
    public String getShareableTitleFallback() {
        return getPromotableTitleFallback();
    }

    @Override
    public String getShareableDescriptionFallback() {
        return getPromotableDescriptionFallback();
    }

    @Override
    public String getSeoTitle() {
        return getPromotableTitle();
    }

    @Override
    public String getSeoDescription() {
        return getPromotableDescription();
    }

    @Override
    public ImageOption getShareableImageFallback() {
        return getPromotableImageFallback();
    }

    // *** Linkable implementation ***
    @Override
    public String getLinkableText() {
        return getPromotableTitle();
    }

    @Override
    public void fromSelection(SearchResultSelection selection) {

        SimpleImageItemStream itemStream = new SimpleImageItemStream();
        for (Object item : selection.createItemsQuery().selectAll()) {
            if (item instanceof Image) {
                itemStream.getItems().add((Image) item);
            }
        }
        if (!itemStream.getItems().isEmpty()) {

            this.items = itemStream;
        }
    }

    private Optional<Image> getFirstImage() {
        return Optional.ofNullable(getItemStream())
            .map(galleryItemStream -> galleryItemStream.getItems(getOwner(), this, 0, 1))
            .map(List::stream)
            .map(stream -> stream.filter(Objects::nonNull))
            .flatMap(Stream::findFirst)
            .map(ImageItem::getImageItemImage);
    }

    private Optional<ImageOption> getFirstImageOption() {

        return getFirstImage()
            .map(OneOffImageOption::create);
    }

    protected Site getOwner() {

        Site.ObjectModification siteData = this.as(Site.ObjectModification.class);
        return siteData != null ? siteData.getOwner() : null;
    }

    @Override
    protected void beforeSave() {

        super.beforeSave();
        replaceSlidesWithItemStream(false);
    }

    private void replaceSlidesWithItemStream(boolean saveImmediately) {

        if (items == null) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Did not find items! Creating new default item stream!");
            }
            items = GalleryItemStream.createDefault();
            if (saveImmediately) {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Saving changes immediately!");
                }
                saveImmediately();
            }
        }

        if (slides != null && !slides.isEmpty()) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Found existing slide deck in Gallery! Replacing {} slides!", slides.size());
            }

            // converting Slides to GalleryItemPromos
            SlideToImageItemPromo adapter = new SlideToImageItemPromo();
            List<ImageItemPromo> promos = slides.stream()
                .map(adapter::adapt)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Size of replacement list:  {} ImageItemPromos!", promos.size());
            }

            // Creating replacement for existing itemStream
            AdvancedImageItemStream galleryItemStream = new AdvancedImageItemStream();
            galleryItemStream.getItems().addAll(promos);

            // Setting itemStream
            this.setItemStream(galleryItemStream);

            // clearing
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Clearing slides!");
            }
            slides.clear();
            slides = null;
            if (saveImmediately) {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Saving changes immediately!");
                }
                saveImmediately();
            }
        }
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, SectionPrefixPermalinkRule.class);
    }
}
