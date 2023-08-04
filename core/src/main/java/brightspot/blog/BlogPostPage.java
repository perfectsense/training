package brightspot.blog;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brightspot.ad.injection.rte.SupportsAdInjection;
import brightspot.author.HasAuthorsWithField;
import brightspot.breadcrumbs.HasBreadcrumbs;
import brightspot.cascading.CascadingPageElements;
import brightspot.commenting.HasCommenting;
import brightspot.commenting.coral.HasCoralPageMetadata;
import brightspot.commenting.disqus.HasDisqusPageMetadata;
import brightspot.embargo.Embargoable;
import brightspot.google.drive.docs.GoogleDocumentImport;
import brightspot.image.WebImageAsset;
import brightspot.image.WebImagePlacement;
import brightspot.l10n.LocaleProvider;
import brightspot.link.InternalLink;
import brightspot.mediatype.HasMediaTypeWithOverride;
import brightspot.mediatype.MediaType;
import brightspot.microsoft.drives.conversion.document.MicrosoftDocumentImport;
import brightspot.module.list.page.PagePromo;
import brightspot.module.promo.page.PagePromoModulePlacementInline;
import brightspot.opengraph.article.OpenGraphArticle;
import brightspot.page.Page;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.permalink.DefaultPermalinkRule;
import brightspot.permalink.Permalink;
import brightspot.promo.page.InternalPagePromoItem;
import brightspot.promo.page.PagePromotableWithOverrides;
import brightspot.rte.LargeRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.rte.image.ImageRichTextElement;
import brightspot.rte.link.LinkRichTextElement;
import brightspot.search.boost.HasSiteSearchBoostIndexes;
import brightspot.search.modifier.exclusion.SearchExcludable;
import brightspot.section.HasSecondarySectionsWithField;
import brightspot.section.HasSection;
import brightspot.section.Section;
import brightspot.seo.EnhancedSeoBodyDynamicNote;
import brightspot.seo.EnhancedSeoHeadlineDynamicNote;
import brightspot.seo.EnhancedSeoWithFields;
import brightspot.seo.SeoWithFields;
import brightspot.share.Shareable;
import brightspot.sharedcontent.SharedContent;
import brightspot.site.DefaultSiteMapItem;
import brightspot.sponsoredcontent.HasSponsorWithField;
import brightspot.tag.HasTagsWithField;
import brightspot.tag.Tag;
import brightspot.update.LastUpdatedProvider;
import brightspot.urlslug.HasUrlSlugWithField;
import brightspot.util.RichTextUtils;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ContentEditDrawerItem;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.content.Suggestible;
import com.psddev.cms.ui.form.DynamicNoteClass;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Utils;
import com.psddev.feed.FeedItem;
import com.psddev.sitemap.NewsSiteMapItem;
import com.psddev.sitemap.SiteMapEntry;
import com.psddev.sitemap.SiteMapNews;
import com.psddev.sitemap.SiteMapSettingsModification;
import com.psddev.suggestions.Suggestable;
import org.apache.commons.lang3.StringUtils;

@ToolUi.FieldDisplayOrder({
    "hasBlogWithField.blog",
    "headline",
    "subheadline",
    "hasUrlSlug.urlSlug",
    "hasAuthorsWithField.authors",
    "lead",
    "body",
    "hasSecondarySectionsWithField.secondarySections",
    "hasTags.tags",
    "embargoable.embargo",
    "seo.title",
    "seo.suppressSeoDisplayName",
    "seo.description",
    "seo.keywords",
    "seo.robots",
    "seo.focusKeyWord",
    "seo.getFocusKeywordDensity",
    "seo.disableSeoRecommendations",
    "ampPage.ampDisabled"
})
@Recordable.DisplayName("Blog Post")
@ToolUi.IconName("rate_review")
public class BlogPostPage extends Content implements
    CascadingPageElements,
    ContentEditDrawerItem,
    Embargoable,
    EnhancedSeoWithFields,
    DefaultSiteMapItem,
    FeedItem,
    GoogleDocumentImport,
    HasAuthorsWithField,
    HasBlogWithField,
    HasBreadcrumbs,
    HasCommenting,
    HasCoralPageMetadata,
    HasDisqusPageMetadata,
    HasMediaTypeWithOverride,
    HasSecondarySectionsWithField,
    HasSection,
    HasSiteSearchBoostIndexes,
    HasSponsorWithField,
    HasTagsWithField,
    HasUrlSlugWithField,
    Interchangeable,
    MicrosoftDocumentImport,
    NewsSiteMapItem,
    OpenGraphArticle,
    Page,
    PagePromotableWithOverrides,
    SearchExcludable,
    SeoWithFields,
    Shareable,
    SharedContent,
    Suggestable,
    Suggestible,
    SupportsAdInjection, Recordable {

    @Indexed
    @Required
    @DynamicNoteClass(EnhancedSeoHeadlineDynamicNote.class)
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String headline;

    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String subheadline;

    @ToolUi.RichText(inline = false, toolbar = LargeRichTextToolbar.class, lines = 10)
    @DynamicNoteClass(EnhancedSeoBodyDynamicNote.class)
    private String body;

    private BlogPostLead lead;

    public String getFullBody() {
        return getBody();
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSubheadline() {
        return subheadline;
    }

    public void setSubheadline(String subheadline) {
        this.subheadline = subheadline;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public BlogPostLead getLead() {
        return lead;
    }

    public void setLead(BlogPostLead lead) {
        this.lead = lead;
    }

    // --- HasBreadcrumbs support ---

    @Override
    public List<?> getBreadcrumbs() {
        List<Section> ancestors = getSectionAncestors();
        Collections.reverse(ancestors);
        return ancestors;
    }

    // --- HasSiteSearchBoostIndexes support ---

    @Override
    public String getSiteSearchBoostTitle() {
        return getHeadline();
    }

    @Override
    public String getSiteSearchBoostDescription() {
        return getSubheadline();
    }

    // --- Linkable support ---

    @Override
    public String getLinkableText() {
        return getPagePromotableTitle();
    }

    // --- SeoWithFields support ---

    @Override
    public String getSeoTitleFallback() {
        return RichTextUtils.richTextToPlainText(getPagePromotableTitle());
    }

    @Override
    public String getSeoDescriptionFallback() {
        return RichTextUtils.richTextToPlainText(getPagePromotableDescription());
    }

    // --- HasUrlSlugWithField support ---

    @Override
    public String getUrlSlugFallback() {
        return Utils.toNormalized(RichTextUtils.richTextToPlainText(getHeadline()));
    }

    // --- OpenGraphArticle support ---

    @Override
    public Date getOpenGraphArticleModifiedDate() {
        return getUpdateDate();
    }

    @Override
    public Date getOpenGraphArticlePublishedDate() {
        return getPublishDate();
    }

    @Override
    public List<String> getOpenGraphArticleAuthorUrls(Site site) {
        return Optional.ofNullable(asHasAuthorsWithFieldData().getAuthors())
            .flatMap(authors -> Optional.ofNullable(authors.stream()))
            .orElseGet(Stream::empty)
            .map(author -> Permalink.getPermalink(site, author))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    @Override
    public String getOpenGraphArticleSection() {
        return Optional.ofNullable(asHasSectionData().getSectionParent())
            .map(Section::getSectionDisplayNamePlainText)
            .orElse(null);
    }

    @Override
    public List<String> getOpenGraphArticleTags() {
        return getTags()
            .stream()
            .map(Tag::getTagDisplayNamePlainText)
            .collect(Collectors.toList());
    }

    // --- Directory.Item support ---

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, DefaultPermalinkRule.class);
    }

    // --- FeedElement support ---

    @Override
    public String getFeedTitle() {
        return getSeoTitle();
    }

    @Override
    public String getFeedDescription() {
        return getSeoDescription();
    }

    @Override
    public String getFeedLink(Site site) {
        return Permalink.getPermalink(site, this);
    }

    // --- NewsSiteMapItem support ---

    @Override
    public List<SiteMapEntry> getNewsSiteMapEntries(Site site) {
        String sitePermalinkPath = as(Directory.ObjectModification.class).getSitePermalinkPath(site);
        if (StringUtils.isBlank(sitePermalinkPath)) {
            return Collections.emptyList();
        }

        Locale locale = ObjectUtils.firstNonNull(
            LocaleProvider.getModelLocale(site, this),
            LocaleProvider.DEFAULT_LOCALE);

        SiteMapEntry siteMapEntry = new SiteMapEntry();
        siteMapEntry.setUpdateDate(
            ObjectUtils.firstNonNull(
                LastUpdatedProvider.getMostRecentUpdateDate(getState()),
                getState().as(Content.ObjectModification.class).getPublishDate()
            )
        );
        siteMapEntry.setPermalink(SiteSettings.get(
            site,
            f -> f.as(SiteMapSettingsModification.class).getSiteMapDefaultUrl()
                + StringUtils.prependIfMissing(sitePermalinkPath, "/")));

        SiteMapNews siteMapNews = new SiteMapNews();
        siteMapNews.setName(site != null ? site.getName() : "Global");

        siteMapNews.setLanguage(locale.getISO3Language());
        siteMapNews.setPublicationDate(this.getPublishDate());
        siteMapNews.setTitle(ObjectUtils.firstNonBlank(
            getSeoTitle(),
            RichTextUtils.richTextToPlainText(this.getHeadline())
        ));
        List<String> keywords = getTags()
            .stream()
            .map(Tag::getTagDisplayNamePlainText)
            .collect(Collectors.toList());

        if (!ObjectUtils.isBlank(keywords)) {
            if (keywords.size() > 10) {
                keywords = keywords.subList(0, 10);
            }
            siteMapNews.setKeywords(keywords);
        }

        siteMapEntry.setNews(Collections.singletonList(siteMapNews));

        return Collections.singletonList(siteMapEntry);
    }

    // --- Suggestable support ---

    @Override
    public String getSuggestableText() {
        return Optional.ofNullable(RichTextUtils.richTextToPlainText(getHeadline())).orElse("") + " "
            + Optional.ofNullable(RichTextUtils.richTextToPlainText(getSubheadline())).orElse("") + " "
            + Optional.ofNullable(getFullBody())
            .map(RichTextUtils::stripRichTextElements)
            .map(RichTextUtils::richTextToPlainText)
            .orElse("");
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

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return RichTextUtils.richTextToPlainText(getHeadline());
    }

    // *** Promotable implementation *** //

    @Override
    public String getPagePromotableTitleFallback() {
        return getHeadline();
    }

    @Override
    public String getPagePromotableDescriptionFallback() {
        return getSubheadline();
    }

    @Override
    public WebImageAsset getPagePromotableImageFallback() {
        return Optional.ofNullable(getLead())
            .map(BlogPostLead::getBlogPostLeadImage)
            .orElseGet(() -> ImageRichTextElement.getFirstImageFromRichText(getFullBody()));
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
            .orElseGet(null);
    }

    public String getReadDuration() {
        long characterCount = Optional.ofNullable(getFullBody())
            .map(RichTextUtils::stripRichTextElements)
            .map(RichTextUtils::richTextToPlainText)
            .map(String::length)
            .orElse(0);

        // TODO: localization needed. This implementation is highly dependent on language!
        // Average adult reading time given: 275 wpm. Average number of characters per English
        // word is 4-5 characters. Therefore, average is 1100-1375 CPM. 1200 CPM used.
        long readTime = characterCount / 1_200;

        if (readTime < 1) {
            readTime = 1;
        }

        return Long.toString(readTime) + " Min Read";
    }

    // --- Shareable support ---'

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

    // --- FeedItem support ---

    @Override
    public String getFullContentEncoded() {
        return Optional.ofNullable(getFullBody())
            .map(RichTextUtils::stripRichTextElements)
            .map(RichTextUtils::richTextToPlainText)
            .orElse(null);
    }

    // --- Enhanced SEO support ---

    @Override
    public List<String> getEnhancedSeoBodyAllImageAltTexts(String body) {
        List<ImageRichTextElement> iRtes = ImageRichTextElement.getImageEnhancementsFromRichText(body);
        if (ObjectUtils.isBlank(iRtes)) {
            return null;
        }
        return iRtes.stream()
            .filter(Objects::nonNull)
            .map(image -> image.as(WebImagePlacement.class).getWebImageAltText())
            .collect(Collectors.toList());
    }

    // --- Section support ---

    @Override
    public Section getSectionParent() {
        Blog parent = getBlog();
        if (parent instanceof Section) {
            return (Section) parent;
        }
        return null;
    }

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        // Support the following types for Shelf drag and drop:
        // - PagePromo in PageListModule
        // - PagePromoModulePlacementInline
        // - LinkRichTextElement
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

        } else if (target instanceof LinkRichTextElement) {

            LinkRichTextElement linkRte = (LinkRichTextElement) target;
            InternalLink internalLink = new InternalLink();
            internalLink.setItem(this);
            linkRte.setLink(internalLink);

            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        // Support the following types for Shelf drag and drop:
        // - PagePromo in PageListModule
        // - PagePromoModulePlacementInline
        // - LinkRichTextElement
        return ImmutableList.of(
            ObjectType.getInstance(PagePromo.class).getId(),
            ObjectType.getInstance(PagePromoModulePlacementInline.class).getId(),
            ObjectType.getInstance(LinkRichTextElement.class).getId()
        );
    }
}
