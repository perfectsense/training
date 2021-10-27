package brightspot.article;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brightspot.author.HasAuthorsWithField;
import brightspot.breadcrumbs.HasBreadcrumbs;
import brightspot.cascading.CascadingPageElements;
import brightspot.embargo.Embargoable;
import brightspot.image.WebImage;
import brightspot.image.WebImageAsset;
import brightspot.l10n.LocaleProvider;
import brightspot.opengraph.article.OpenGraphArticle;
import brightspot.page.Page;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.permalink.Permalink;
import brightspot.promo.page.PagePromotableWithOverrides;
import brightspot.rte.LargeRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.rte.image.ImageRichTextElement;
import brightspot.search.modifier.exclusion.SearchExcludable;
import brightspot.section.HasSecondarySectionsWithField;
import brightspot.section.HasSectionWithField;
import brightspot.section.Section;
import brightspot.section.SectionPrefixPermalinkRule;
import brightspot.seo.SeoWithFields;
import brightspot.share.Shareable;
import brightspot.sharedcontent.SharedContent;
import brightspot.site.DefaultSiteMapItem;
import brightspot.tag.HasTagsWithField;
import brightspot.tag.Tag;
import brightspot.update.LastUpdatedProvider;
import brightspot.urlslug.HasUrlSlugWithField;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.crosslinker.db.Crosslinkable;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Utils;
import com.psddev.feed.FeedItem;
import com.psddev.sitemap.NewsSiteMapItem;
import com.psddev.sitemap.SiteMapEntry;
import com.psddev.sitemap.SiteMapNews;
import com.psddev.sitemap.SiteMapSettingsModification;
import com.psddev.suggestions.Suggestable;
import com.psddev.theme.StyleEmbeddedContentCreator;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Attribute;

@ToolUi.FieldDisplayOrder({
        "headline",
        "subheadline",
        "hasUrlSlug.urlSlug",
        "hasAuthorsWithField.authors",
        "lead",
        "body",
        "hasSectionWithField.section",
        "hasTags.tags",
        "embargoable.embargo"
})
@Recordable.LabelFields("headline")
@ToolUi.IconName("subject")
@Crosslinkable.SimulationName("Default")
public abstract class AbstractRichTextArticle extends Content implements
        CascadingPageElements,
        Crosslinkable,
        Embargoable,
        DefaultSiteMapItem,
        FeedItem,
        HasAuthorsWithField,
        HasBreadcrumbs,
        HasSecondarySectionsWithField,
        HasSectionWithField,
        HasTagsWithField,
        HasUrlSlugWithField,
        NewsSiteMapItem,
        OpenGraphArticle,
        Page,
        PagePromotableWithOverrides,
        SearchExcludable,
        SeoWithFields,
        Shareable,
        SharedContent,
        Suggestable {

    public static final String PROMOTABLE_TYPE = "article";

    @Indexed
    @Required
    @ToolUi.NoteHtml("<span data-dynamic-html=\"${content.getSeoTitleNoteHtml()}\"></span>")
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String headline;

    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String subheadline;

    @Crosslinkable.Crosslinked
    @ToolUi.RichText(inline = false, toolbar = LargeRichTextToolbar.class, lines = 10)
    private String body;

    @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    private ArticleLead lead;

    /**
     * @return rich text
     */
    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    /**
     * @return rich text
     */
    public String getSubheadline() {
        return subheadline;
    }

    public void setSubheadline(String subheadline) {
        this.subheadline = subheadline;
    }

    /**
     * @return rich text
     */
    @Indexed
    @ToolUi.Hidden
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ArticleLead getLead() {
        return lead;
    }

    public void setLead(ArticleLead lead) {
        this.lead = lead;
    }

    // --- HasBreadcrumbs support ---

    @Override
    public List<Section> getBreadcrumbs() {
        List<Section> ancestors = getSectionAncestors();
        Collections.reverse(ancestors);
        return ancestors;
    }

    // --- HasUrlSlugWithField support ---

    @Override
    public String getUrlSlugFallback() {
        return Utils.toNormalized(RichTextUtils.richTextToPlainText(getHeadline()));
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

    /**
     * Gets the first image on the article. First looks for an image in the article's {@link Article#lead} and then
     * looks in the article's {@link Article#body}.
     *
     * @return an {@link WebImage} or {@code null}, if no image is found.
     */
    @Override
    public WebImageAsset getPagePromotableImageFallback() {
        return Optional.ofNullable(getLead())
                .map(ArticleLead::getArticleLeadImage)
                .orElseGet(() -> getFirstImageFromRichText(getBody()));
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

    public String getReadDuration() {
        long characterCount = Optional.ofNullable(getBody())
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

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return RichTextUtils.richTextToPlainText(getHeadline());
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

    // --- Shareable support ---

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

    // --- Linkable support ---

    @Override
    public String getLinkableText() {
        return getPagePromotableTitle();
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

        if (keywords.size() < 10) {
            keywords.addAll(this.getSeoKeywords());
        }

        if (!ObjectUtils.isBlank(keywords)) {
            if (keywords.size() > 10) {
                keywords = keywords.subList(0, 10);
            }
            siteMapNews.setKeywords(keywords);
        }

        siteMapEntry.setNews(Collections.singletonList(siteMapNews));

        return Collections.singletonList(siteMapEntry);
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

    // --- FeedItem support ---

    @Override
    public String getFullContentEncoded() {
        return Optional.ofNullable(getBody())
                .map(RichTextUtils::stripRichTextElements)
                .map(RichTextUtils::richTextToPlainText)
                .orElse(null);
    }

    // --- Directory.Item support ---

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, SectionPrefixPermalinkRule.class);
    }

    // --- Suggestable support ---

    @Override
    public String getSuggestableText() {
        return Optional.ofNullable(RichTextUtils.richTextToPlainText(getHeadline())).orElse("") + " "
                + Optional.ofNullable(RichTextUtils.richTextToPlainText(getSubheadline())).orElse("") + " "
                + Optional.ofNullable(getBody())
                .map(RichTextUtils::stripRichTextElements)
                .map(RichTextUtils::richTextToPlainText)
                .orElse("");
    }

    /**
     * Returns the first image within the provided rich text.
     *
     * @param richText a rich text {@link String} (nullable).
     * @return an {@link WebImage} or {@code null}, if no image was found.
     */
    public static WebImage getFirstImageFromRichText(String richText) {

        if (!StringUtils.isBlank(richText)) {

            return RichTextUtils.documentFromRichText(richText).select(ImageRichTextElement.TAG_NAME)
                    .stream()
                    .map(e -> {

                        Map<String, String> attributes = e.attributes().asList().stream().collect(Collectors.toMap(Attribute::getKey, Attribute::getValue));
                        ImageRichTextElement imageRTE = new ImageRichTextElement();
                        imageRTE.fromAttributes(attributes);
                        imageRTE.fromBody(e.html());
                        return imageRTE.getImage();
                    })
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }
}
