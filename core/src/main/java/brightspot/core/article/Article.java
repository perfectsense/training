package brightspot.core.article;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brightspot.core.creativework.CreativeWork;
import brightspot.core.image.ImageOption;
import brightspot.core.lead.Lead;
import brightspot.core.link.Linkable;
import brightspot.core.page.Page;
import brightspot.core.page.opengraph.article.OpenGraphArticle;
import brightspot.core.permalink.AbstractPermalinkRule;
import brightspot.core.person.Authorable;
import brightspot.core.pkg.Packageable;
import brightspot.core.promo.PromotableWithOverrides;
import brightspot.core.readreceipt.Readable;
import brightspot.core.section.SectionPrefixPermalinkRule;
import brightspot.core.section.Sectionable;
import brightspot.core.share.Shareable;
import brightspot.core.site.ExpressSiteMapItem;
import brightspot.core.site.FrontEndSettings;
import brightspot.core.slug.Sluggable;
import brightspot.core.tag.Tag;
import brightspot.core.tag.Taggable;
import brightspot.core.tool.DirectoryItemUtils;
import brightspot.core.tool.RichTextUtils;
import brightspot.core.update.LastUpdatedProvider;
import brightspot.corporate.tag.CorporateTaggable;
import brightspot.news.embargo.Embargoable;
import brightspot.news.revision.Revisable;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;
import com.psddev.feed.FeedItem;
import com.psddev.sitemap.NewsSiteMapItem;
import com.psddev.sitemap.SiteMapEntry;
import com.psddev.sitemap.SiteMapNews;
import com.psddev.sitemap.SiteMapSettingsModification;
import com.psddev.theme.StyleEmbeddedContentCreator;

@Seo.OpenGraphType("article")
@ToolUi.FieldDisplayOrder({
    "headline",
    "subHeadline",
    "sluggable.slug",
    "authorable.authors",
    "authorable.byline",
    // Main Tab
    "lead",
    "body",
    "sectionable.section",
    "packageable.pkg",
    "taggable.tags",
    "taggable.tagging",
    "revisable.revisions",
    "embargoable.embargo",
    // Main Tab
    "promotable.promoTitle",
    "promotable.promoDescription",
    "promotable.promoImage",
    "shareable.shareTitle",
    "shareable.shareDescription",
    "shareable.shareImage",
    "page.layoutOption" }) // Overrides Tab
@ToolUi.FieldDisplayPreview({
    "headline",
    "subHeadline",
    "authorable.authors",
    "sectionable.section",
    "taggable.tags",
    "cms.content.updateDate",
    "cms.content.updateUser"})
@ToolUi.IconName("subject")
@ToolUi.SearchShortcut(shortcut = "bo", field = "getBody/getPlainTextPreview")
@ToolUi.SearchShortcut(shortcut = "hl", field = "getHeadline")
@ToolUi.SearchShortcut(shortcut = "sh", field = "getSubHeadline")
@ToolUi.SearchShortcut(shortcut = "sl", field = "sluggable.getSlug")
public class Article extends CreativeWork implements
        Authorable,
        CorporateTaggable,
        Directory.Item,
        Embargoable,
        ExpressSiteMapItem,
        FeedItem,
        Linkable,
        NewsSiteMapItem,
        OpenGraphArticle,
        Packageable,
        Page,
        PromotableWithOverrides,
        Readable,
        Revisable,
        Sectionable,
        Shareable,
        Sluggable,
        Taggable {

    public static final String PROMOTABLE_TYPE = "article";

    @Required
    private Body body = Body.createDefault();

    @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    private Lead lead;

    @Indexed
    @ToolUi.Hidden
    @ToolUi.Filterable
    @ToolUi.DropDown
    @Where("groups = " + Body.INTERNAL_NAME
        + " && internalName != " + Body.INTERNAL_NAME
        + " && isAbstract = false")
    private ObjectType bodyType;

    @Indexed
    @ToolUi.Hidden
    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    @Override
    protected void beforeSave() {

        this.updateBodyType();
    }

    // *** Promotable implementation *** //
    @Override
    public String getPromotableTitleFallback() {
        return getHeadline();
    }

    @Override
    public String getPromotableDescriptionFallback() {
        return ObjectUtils.firstNonBlank(
            getSubHeadline(),
            Optional.ofNullable(getBody()).map(Body::getPlainTextPreview).orElse(null));
    }

    /**
     * Gets the first image on the article. First looks for an image in the article's {@link Article#lead} and then
     * looks in the article's {@link Article#body}.
     *
     * @return an {@link ImageOption} or {@code null}, if no image is found.
     */
    @Override
    public ImageOption getPromotableImageFallback() {

        ImageOption fallbackImage = Optional.ofNullable(getLead())
            .map(Lead::getLeadImage)
            .orElse(null);

        if (fallbackImage != null) {
            return fallbackImage;
        }

        Body body = getBody();
        if (body == null) {
            return null;
        }

        String bodyString = null;
        if (body instanceof RichTextBody) {
            bodyString = ((RichTextBody) body).getRichText();
        } else if (body instanceof ListBody) {
            bodyString = ((ListBody) body).getItems().stream()
                .map(ListItem::getBody)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        }

        return RichTextUtils.getFirstImageFromRichText(bodyString);
    }

    @Override
    public String getPromotableType() {
        return PROMOTABLE_TYPE;
    }

    @Override
    public String getPromotableDuration() {
        long characterCount = (getBody() == null ? 0 : getBody().getCharacterCount());

        // TODO: localization needed. This implementation is highly dependent on language!
        // Average adult reading time given: 275 wpm. Average number of characters per English
        // word is 4-5 characters. Therefore, average is 1100-1375 CPM. 1200 CPM used.
        long readTime = characterCount / 1_200;

        if (readTime < 1) {
            readTime = 1;
        }

        return Long.toString(readTime) + " Min Read";
    }

    // *** Shareable implementation *** //
    @Override
    public String getShareableTitleFallback() {
        return getPromotableTitleFallback();
    }

    @Override
    public String getShareableDescriptionFallback() {
        return getPromotableDescriptionFallback();
    }

    @Override
    public ImageOption getShareableImageFallback() {
        return getPromotableImageFallback();
    }

    @Override
    public String getSluggableSlugFallback() {
        return StringUtils.toNormalized(getHeadline());
    }

    private void updateBodyType() {
        if (body != null) {
            bodyType = body.getState().getType();
        }
    }

    // *** CreativeWork implementation *** //
    @Override
    public String getSeoTitle() {
        return getPromotableTitle();
    }

    @Override
    public String getSeoDescription() {
        return getPromotableDescription();
    }

    // *** Linkable implementation *** //
    @Override
    public String getLinkableText() {
        return getPromotableTitle();
    }

    @Override
    public List<SiteMapEntry> getNewsSiteMapEntries() {
        List<SiteMapEntry> siteMapEntries = new ArrayList<>();
        Stream.concat(Site.Static.findAll().stream(), Stream.of(new Site[] { null })).forEach(site -> {

            Locale locale = ObjectUtils.firstNonNull(
                FrontEndSettings.get(site, FrontEndSettings::getLocale),
                Locale.getDefault());
            String sitePermalinkPath = as(Directory.ObjectModification.class).getSitePermalinkPath(site);
            if (!StringUtils.isBlank(sitePermalinkPath)) {
                SiteMapEntry siteMapEntry = new SiteMapEntry();
                siteMapEntry.setUpdateDate(
                    ObjectUtils.firstNonNull(
                        LastUpdatedProvider.getMostRecentUpdateDate(getState()),
                        getState().as(Content.ObjectModification.class).getPublishDate()
                    )
                );
                siteMapEntry.setPermalink(SiteSettings.get(
                    site,
                    f -> f.as(SiteMapSettingsModification.class).getSiteMapDefaultUrl() + StringUtils.ensureStart(
                        sitePermalinkPath,
                        "/")));

                SiteMapNews siteMapNews = new SiteMapNews();
                siteMapNews.setName(site != null ? site.getName() : "Global");

                siteMapNews.setLanguage(locale.getISO3Language());
                siteMapNews.setPublicationDate(this.getPublishDate());
                siteMapNews.setTitle(ObjectUtils.firstNonBlank(
                    this.as(Seo.ObjectModification.class).getTitle(),
                    this.getHeadline()
                ));
                List<String> keywords = this.asTaggableData().getTags().stream()
                    .map(Tag::getDisplayName)
                    .collect(Collectors.toList());

                if (keywords.size() < 10) {
                    keywords.addAll(this.as(Seo.ObjectModification.class).getKeywords());
                }

                if (!ObjectUtils.isBlank(keywords)) {
                    if (keywords.size() > 10) {
                        keywords = keywords.subList(0, 10);
                    }
                    siteMapNews.setKeywords(keywords);
                }

                siteMapEntry.setNews(Arrays.asList(siteMapNews));

                siteMapEntries.add(siteMapEntry);
            }
        });
        return siteMapEntries;
    }

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
        return DirectoryItemUtils.getCanonicalUrl(site, this);
    }

    @Override
    public String getFullContentEncoded() {
        if (body instanceof RichTextBody) {
            return Optional.ofNullable(((RichTextBody) body).getRichText())
                .map(RichTextUtils::stripRichTextElements)
                .map(RichTextUtils::richTextToPlainText)
                .orElse(null);
        }
        return null;
    }

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, SectionPrefixPermalinkRule.class);
    }
}
