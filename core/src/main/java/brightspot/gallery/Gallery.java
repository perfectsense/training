package brightspot.gallery;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import brightspot.author.HasAuthorsWithField;
import brightspot.breadcrumbs.HasBreadcrumbs;
import brightspot.cascading.CascadingPageElements;
import brightspot.image.WebImage;
import brightspot.image.WebImageAsset;
import brightspot.imageitemstream.AdvancedImageItemStream;
import brightspot.imageitemstream.ExistingImageItemStream;
import brightspot.imageitemstream.ExistingImageItemStreamProvider;
import brightspot.imageitemstream.ImageItemStream;
import brightspot.imageitemstream.SimpleImageItemStream;
import brightspot.imageitemstream.WebImageItem;
import brightspot.page.Page;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.promo.page.PagePromotableWithOverrides;
import brightspot.rte.LargeRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.search.modifier.exclusion.SearchExcludable;
import brightspot.section.HasSectionWithField;
import brightspot.section.Section;
import brightspot.section.SectionPrefixPermalinkRule;
import brightspot.seo.SeoWithFields;
import brightspot.share.Shareable;
import brightspot.site.DefaultSiteMapItem;
import brightspot.tag.HasTagsWithField;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.SearchResultSelection;
import com.psddev.cms.tool.SearchResultSelectionGeneratable;
import com.psddev.crosslinker.db.Crosslinkable;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

@ToolUi.FieldDisplayOrder({
    "title",
    "description",
    "hasAuthorsWithField.authors",
    "body",
    "items",
    "hasSectionWithField.section",
    "hasTags.tags"
})
@Recordable.LabelFields("title")
@SearchResultSelectionGeneratable.ItemTypes(WebImage.class)
@ToolUi.IconName(Gallery.ICON_NAME)
@Recordable.PreviewField("getPreviewFile")
public class Gallery extends Content implements
        CascadingPageElements,
        ExistingImageItemStreamProvider,
        DefaultSiteMapItem,
        HasAuthorsWithField,
        HasBreadcrumbs,
        HasSectionWithField,
        HasTagsWithField,
        Page,
        PagePromotableWithOverrides,
        SearchExcludable,
        SearchResultSelectionGeneratable,
        SeoWithFields,
        Shareable {

    public static final String ICON_NAME = "photo_library";
    public static final String PROMOTABLE_TYPE = "gallery";

    @Indexed
    @Required
    @ToolUi.NoteHtml("<span data-dynamic-html=\"${content.getSeoTitleNoteHtml()}\"></span>")
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String title;

    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String description;

    @Crosslinkable.Crosslinked
    @ToolUi.RichText(inline = false, toolbar = LargeRichTextToolbar.class)
    private String body;

    @Embedded
    @Required
    @Crosslinkable.Crosslinked
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
        return PROMOTABLE_TYPE;
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
            .map(WebImageItem::getWebImage);
    }

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, SectionPrefixPermalinkRule.class);
    }
}
