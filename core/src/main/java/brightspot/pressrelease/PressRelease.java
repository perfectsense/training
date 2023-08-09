package brightspot.pressrelease;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brightspot.breadcrumbs.HasBreadcrumbs;
import brightspot.cascading.CascadingPageElements;
import brightspot.embargo.Embargoable;
import brightspot.google.drive.docs.GoogleDocumentImport;
import brightspot.homepage.Homepage;
import brightspot.image.WebImageAsset;
import brightspot.image.WebImagePlacement;
import brightspot.l10n.LocaleProvider;
import brightspot.link.InternalLink;
import brightspot.microsoft.drives.conversion.document.MicrosoftDocumentImport;
import brightspot.module.list.page.PagePromo;
import brightspot.module.promo.page.PagePromoModulePlacementInline;
import brightspot.opengraph.article.OpenGraphArticle;
import brightspot.page.Page;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.permalink.Permalink;
import brightspot.promo.page.InternalPagePromoItem;
import brightspot.promo.page.PagePromotableWithOverrides;
import brightspot.rte.LargeRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.rte.image.ImageRichTextElement;
import brightspot.rte.link.LinkRichTextElement;
import brightspot.search.boost.HasSiteSearchBoostIndexes;
import brightspot.search.modifier.exclusion.SearchExcludable;
import brightspot.section.HasSecondarySections;
import brightspot.section.HasSecondarySectionsWithField;
import brightspot.section.HasSection;
import brightspot.section.HasSectionWithField;
import brightspot.section.Section;
import brightspot.section.SectionPrefixPermalinkRule;
import brightspot.seo.EnhancedSeoBodyDynamicNote;
import brightspot.seo.EnhancedSeoHeadlineDynamicNote;
import brightspot.seo.EnhancedSeoWithFields;
import brightspot.share.Shareable;
import brightspot.sharedcontent.SharedContent;
import brightspot.site.DefaultSiteMapItem;
import brightspot.tag.HasTags;
import brightspot.tag.HasTagsWithField;
import brightspot.tag.Tag;
import brightspot.update.LastUpdatedProvider;
import brightspot.urlslug.HasUrlSlugWithField;
import brightspot.util.RichTextUtils;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.content.Suggestible;
import com.psddev.cms.ui.content.place.Placeable;
import com.psddev.cms.ui.content.place.PlaceableTarget;
import com.psddev.cms.ui.form.DynamicNoteClass;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Query;
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
    "headline",
    "subheadline",
    "hasUrlSlug.urlSlug",
    "lead",
    "body",
    "hasSectionWithField.section",
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
@Recordable.LabelFields("headline")
@ToolUi.IconName("business")
@ToolUi.FieldDisplayPreview({
    "headline",
    "subheadline",
    "hasSectionWithField.section",
    "hasTags.tags",
    "cms.content.updateDate",
    "cms.content.updateUser" })
public class PressRelease extends Content implements
    CascadingPageElements,
    EnhancedSeoWithFields,
    Embargoable,
    DefaultSiteMapItem,
    FeedItem,
    GoogleDocumentImport,
    HasBreadcrumbs,
    HasSecondarySectionsWithField,
    HasSectionWithField,
    HasSiteSearchBoostIndexes,
    HasTagsWithField,
    HasUrlSlugWithField,
    Interchangeable,
    MicrosoftDocumentImport,
    NewsSiteMapItem,
    OpenGraphArticle,
    Page,
    PagePromotableWithOverrides,
    Placeable,
    SearchExcludable,
    Shareable,
    SharedContent,
    Suggestable,
    Suggestible {

    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    @DynamicNoteClass(EnhancedSeoHeadlineDynamicNote.class)
    private String headline;

    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String subheadline;

    private PressReleaseLead lead;

    @ToolUi.RichText(toolbar = LargeRichTextToolbar.class, lines = 10, inline = false)
    @DynamicNoteClass(EnhancedSeoBodyDynamicNote.class)
    private String body;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSubheadline() {
        return subheadline;
    }

    public void setSubHeadline(String subheadline) {
        this.subheadline = subheadline;
    }

    public PressReleaseLead getLead() {
        return lead;
    }

    public void setLead(PressReleaseLead lead) {
        this.lead = lead;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    // --- Directory.Item support ---

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, SectionPrefixPermalinkRule.class);
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

    // --- HasUrlSlugWithField support ---

    @Override
    public String getUrlSlugFallback() {
        return Utils.toNormalized(RichTextUtils.richTextToPlainText(getHeadline()));
    }

    // --- Linkable support ---

    @Override
    public String getLinkableText() {
        return getPagePromotableTitle();
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
        // Returns empty list as press releases do not have authors
        return new ArrayList<>();
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

    // --- NewsSiteMapItem  support ---

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

    // --- SeoWithFields support ---

    @Override
    public String getSeoTitleFallback() {
        return RichTextUtils.richTextToPlainText(getPagePromotableTitle());
    }

    @Override
    public String getSeoDescriptionFallback() {
        return RichTextUtils.richTextToPlainText(getPagePromotableDescription());
    }

    // --- Suggestable support ---

    @Override
    public String getSuggestableText() {
        return Optional.ofNullable(RichTextUtils.richTextToPlainText(getHeadline())).orElse("") + " "
            + Optional.ofNullable(getBody())
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

    @Override
    public String getLabel() {
        return RichTextUtils.richTextToPlainText(getHeadline());
    }

    // --- Promotable implementation ---

    @Override
    public String getPagePromotableTitleFallback() {
        return RichTextUtils.richTextToPlainText(getHeadline());
    }

    @Override
    public String getPagePromotableDescriptionFallback() {
        return RichTextUtils.richTextToPlainText(getSubheadline());
    }

    @Override
    public WebImageAsset getPagePromotableImageFallback() {
        return Optional.ofNullable(getLead())
            .map(PressReleaseLead::getPressReleaseLeadImage)
            .orElseGet(() -> ImageRichTextElement.getFirstImageFromRichText(getBody()));
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

    // --- Shareable implementation ---

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

    // --- FeedItem implementation ---

    @Override
    public String getFullContentEncoded() {
        return Optional.ofNullable(getBody())
            .map(RichTextUtils::stripRichTextElements)
            .map(RichTextUtils::richTextToPlainText)
            .orElse(null);
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
        targets.addAll(this.as(HasSecondarySections.class)
            .getSecondarySections()
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
            getState().getTypeId(), // enable dragging and dropping as itself from the shelf
            ObjectType.getInstance(PagePromo.class).getId(),
            ObjectType.getInstance(PagePromoModulePlacementInline.class).getId(),
            ObjectType.getInstance(LinkRichTextElement.class).getId()
        );
    }

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
}
