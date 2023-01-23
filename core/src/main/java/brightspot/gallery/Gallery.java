package brightspot.gallery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brightspot.author.HasAuthorsWithField;
import brightspot.breadcrumbs.HasBreadcrumbs;
import brightspot.cascading.CascadingPageElements;
import brightspot.homepage.Homepage;
import brightspot.image.WebImage;
import brightspot.image.WebImageAsset;
import brightspot.imageitemstream.AdvancedImageItemStream;
import brightspot.imageitemstream.ExistingImageItemStream;
import brightspot.imageitemstream.ExistingImageItemStreamProvider;
import brightspot.imageitemstream.ImageItemStream;
import brightspot.imageitemstream.SimpleImageItemStream;
import brightspot.imageitemstream.WebImageItem;
import brightspot.mediatype.HasMediaTypeWithOverride;
import brightspot.mediatype.MediaType;
import brightspot.module.list.page.PagePromo;
import brightspot.module.promo.page.PagePromoModulePlacementInline;
import brightspot.page.Page;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.promo.page.InternalPagePromoItem;
import brightspot.promo.page.PagePromotableWithOverrides;
import brightspot.rte.LargeRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.rte.carousel.CarouselRichTextElement;
import brightspot.search.boost.HasSiteSearchBoostIndexes;
import brightspot.search.modifier.exclusion.SearchExcludable;
import brightspot.section.HasSection;
import brightspot.section.HasSectionWithField;
import brightspot.section.Section;
import brightspot.section.SectionPrefixPermalinkRule;
import brightspot.seo.SeoWithFields;
import brightspot.share.Shareable;
import brightspot.site.DefaultSiteMapItem;
import brightspot.sponsoredcontent.HasSponsorWithField;
import brightspot.tag.HasTags;
import brightspot.tag.HasTagsWithField;
import brightspot.util.RichTextUtils;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.SearchResultSelection;
import com.psddev.cms.tool.SearchResultSelectionGeneratable;
import com.psddev.cms.ui.content.Suggestible;
import com.psddev.cms.ui.content.place.Placeable;
import com.psddev.cms.ui.content.place.PlaceableTarget;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

@ToolUi.FieldDisplayOrder({
    "title",
    "description",
    "hasAuthorsWithField.authors",
    "body",
    "items",
    "hasSectionWithField.section",
    "hasTags.tags",
    "seo.title",
    "seo.suppressSeoDisplayName",
    "seo.description",
    "seo.keywords",
    "seo.robots",
    "ampPage.ampDisabled"
})
@Recordable.LabelFields("title")
@SearchResultSelectionGeneratable.ItemTypes(WebImage.class)
@ToolUi.IconName(Gallery.ICON_NAME)
@Recordable.PreviewField("getPreviewFile")
@ToolUi.FieldDisplayPreview({
        "pagePromotable.promoImage",
        "title",
        "description",
        "hasSectionWithField.section",
        "hasTags.tags",
        "cms.content.updateDate",
        "cms.content.updateUser" })
public class Gallery extends Content implements
        CascadingPageElements,
        ExistingImageItemStreamProvider,
        DefaultSiteMapItem,
        HasAuthorsWithField,
        HasBreadcrumbs,
        HasMediaTypeWithOverride,
        HasSectionWithField,
        HasSiteSearchBoostIndexes,
        HasSponsorWithField,
        HasTagsWithField,
        Interchangeable,
        Page,
        PagePromotableWithOverrides,
        Placeable,
        SearchExcludable,
        SearchResultSelectionGeneratable,
        Shareable,
        SeoWithFields,
        Suggestible {

    public static final String ICON_NAME = "photo_library";

    @Indexed
    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    @ToolUi.NoteHtml("<span data-dynamic-html=\"${content.getSeoTitleNoteHtml()}\"></span>")
    private String title;

    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String description;

    @ToolUi.RichText(inline = false, toolbar = LargeRichTextToolbar.class)
    private String body;

    @Embedded
    @Required
    @TypesExclude({ ExistingImageItemStream.class, SimpleImageItemStream.class })
    private ImageItemStream items = new AdvancedImageItemStream();

    /**
     * @return rich text
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return rich text
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ImageItemStream getItemStream() {
        return items;
    }

    public void setItemStream(ImageItemStream items) {
        this.items = items;
    }

    /**
     * Not for public use.
     *
     * Retrieves {@link StorageItem} of first {@link StorageItem} present in {@link #items}.
     *
     * @return May be {@code null}.
     */
    @Ignored(false)
    @ToolUi.Hidden
    public StorageItem getPreviewFile() {
        return getFirstImage()
            .map(WebImage::getFile)
            .orElse(null);
    }

    // --- HasBreadcrumbs support ---

    @Override
    public List<Section> getBreadcrumbs() {
        List<Section> ancestors = getSectionAncestors();
        Collections.reverse(ancestors);
        return ancestors;
    }

    // --- HasMediaTypeOverride Support ---

    @Override
    public MediaType getPrimaryMediaTypeFallback() {
        return MediaType.GALLERY;
    }

    // --- HasSiteSearchBoostIndexes support ---

    @Override
    public String getSiteSearchBoostTitle() {
        return getTitle();
    }

    @Override
    public String getSiteSearchBoostDescription() {
        return getDescription();
    }

    // *** Promotable implementation ***
    @Override
    public String getPagePromotableTitleFallback() {
        return getTitle();
    }

    @Override
    public String getPagePromotableDescriptionFallback() {
        return getDescription();
    }

    @Override
    public WebImage getPagePromotableImageFallback() {
        return getFirstImage().orElse(null);
    }

    @Override
    public String getPagePromotableType() {
        return Optional.ofNullable(getPrimaryMediaType())
            .map(MediaType::getIconName)
            .orElse(null);
    }

    @Override
    public String getPagePromotableCategoryFallback() {
        return Optional.ofNullable(asHasSectionData().getSectionParent())
                .map(Section::getSectionDisplayNameRichText)
                .orElse(null);
    }

    @Override
    public String getPagePromotableCategoryUrlFallback(Site site) {
        return Optional.ofNullable(asHasSectionData().getSectionParent())
                .map(section -> section.getLinkableUrl(site))
                .orElse(null);
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return RichTextUtils.richTextToPlainText(getTitle());
    }

    // --- SeoWithFallbacks support ---

    @Override
    public String getSeoTitleFallback() {
        return RichTextUtils.richTextToPlainText(getPagePromotableTitle());
    }

    @Override
    public String getSeoDescriptionFallback() {
        return RichTextUtils.richTextToPlainText(getPagePromotableDescription());
    }

    // *** Shareable implementation ***
    @Override
    public String getShareableTitleFallback() {
        return RichTextUtils.richTextToPlainText(getPagePromotableTitleFallback());
    }

    @Override
    public String getShareableDescriptionFallback() {
        return RichTextUtils.richTextToPlainText(getPagePromotableDescriptionFallback());
    }

    @Override
    public WebImageAsset getShareableImageFallback() {
        return getPagePromotableImageFallback();
    }

    // --- Suggestible support ---

    @Override
    public List<String> getSuggestibleFields() {
        return Stream.of(
                "seo.title",
                "seo.description",
                "pagePromotable.promoTitle",
                "pagePromotable.promoDescription",
                "pagePromotable.promoImage",
                "shareable.shareTitle",
                "shareable.shareDescription",
                "shareable.shareImage"
        ).collect(Collectors.toList());
    }

    // *** Linkable implementation ***
    @Override
    public String getLinkableText() {
        return getPagePromotableTitle();
    }

    // --- ExistingImageItemStreamProvider support ---

    @Override
    public ImageItemStream getExistingImageItemStream() {
        return getItemStream();
    }

    @Override
    public String getExistingItemStreamFallbackTitle() {
        return getTitle();
    }

    // --- SearchResultSelectionGeneratable support ---

    @Override
    public void fromSelection(SearchResultSelection selection) {

        AdvancedImageItemStream itemStream = new AdvancedImageItemStream();
        for (Object item : selection.createItemsQuery().selectAll()) {
            if (item instanceof WebImage) {
                WebImageItem webItem = new WebImageItem();
                webItem.setItem((WebImage) item);
                itemStream.getItems().add(webItem);
            }
        }
        if (!itemStream.getItems().isEmpty()) {

            this.items = itemStream;
        }
    }

    private Optional<WebImage> getFirstImage() {
        return Optional.ofNullable(getItemStream())
            .map(galleryItemStream -> galleryItemStream.getItems(
                as(Site.ObjectModification.class).getOwner(),
                this,
                0,
                1))
            .map(List::stream)
            .map(stream -> stream.filter(Objects::nonNull))
            .flatMap(Stream::findFirst)
            .filter(WebImageItem.class::isInstance)
            .map(WebImageItem.class::cast)
            .map(WebImageItem::getWebImage);
    }

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, SectionPrefixPermalinkRule.class);
    }

    // --- Placeable support ---

    @Override
    public List<PlaceableTarget> getPlaceableTargets() {
        List<PlaceableTarget> targets = new ArrayList<>();

        targets.add(Query.from(Homepage.class)
                .where("cms.site.owner = ?", as(Site.ObjectModification.class).getOwner())
                .first());
        targets.addAll(this.as(HasTags.class)
                .getTags()
                .stream()
                .filter(PlaceableTarget.class::isInstance)
                .filter(tag -> !tag.isHiddenTag())
                .map(PlaceableTarget.class::cast)
                .collect(Collectors.toSet()));
        targets.addAll(this.as(HasSection.class)
                .getSectionAncestors()
                .stream()
                .filter(PlaceableTarget.class::isInstance)
                .map(PlaceableTarget.class::cast)
                .collect(Collectors.toSet()));

        return targets;
    }

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        // Support the following types for Shelf drag and drop:
        // - PagePromo in PageListModule
        // - PagePromoModulePlacementInline
        // - CarouselRichTextElement
        if (target instanceof PagePromo) {

            PagePromo pagePromo = (PagePromo) target;
            InternalPagePromoItem internalPagePromoItem = new InternalPagePromoItem();
            internalPagePromoItem.setItem(this);
            pagePromo.setItem(internalPagePromoItem);

            return true;

        } else if (target instanceof PagePromoModulePlacementInline) {

            PagePromoModulePlacementInline pagePromoModulePlacementInline = (PagePromoModulePlacementInline) target;
            InternalPagePromoItem internalPagePromoItem = new InternalPagePromoItem();
            internalPagePromoItem.setItem(this);
            pagePromoModulePlacementInline.setItem(internalPagePromoItem);

            return true;

        } else if (target instanceof CarouselRichTextElement) {

            CarouselRichTextElement carouselRichTextElement = (CarouselRichTextElement) target;
            ExistingImageItemStream existingImageItemStream = new ExistingImageItemStream();
            existingImageItemStream.setGallery(this);
            carouselRichTextElement.setItemStream(existingImageItemStream);

            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        // Support the following types for Shelf drag and drop:
        // - PagePromo in PageListModule
        // - PagePromoModulePlacementInline
        // - CarouselRichTextElement
        return ImmutableList.of(
                getState().getTypeId(), // enable dragging and dropping as itself from the shelf
                ObjectType.getInstance(CarouselRichTextElement.class).getId(),
                ObjectType.getInstance(PagePromo.class).getId(),
                ObjectType.getInstance(PagePromoModulePlacementInline.class).getId()
        );
    }
}
