package brightspot.podcast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import brightspot.breadcrumbs.HasBreadcrumbs;
import brightspot.cascading.CascadingPageElements;
import brightspot.embargo.Embargoable;
import brightspot.image.WebImage;
import brightspot.image.WebImageAsset;
import brightspot.landing.LandingCascadingData;
import brightspot.landing.LandingPageElements;
import brightspot.module.ModulePlacement;
import brightspot.page.Page;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.permalink.DefaultPermalinkRule;
import brightspot.permalink.Permalink;
import brightspot.podcast.providers.HasPodcastProvidersMetadataWithField;
import brightspot.promo.page.PagePromotableWithOverrides;
import brightspot.promo.podcast.PodcastPromotable;
import brightspot.rss.feed.RssFeedItem;
import brightspot.rss.feed.RssFeedWithFields;
import brightspot.rss.feed.apple.AppleRssFeedWithFields;
import brightspot.rss.feed.apple.RssWebImage;
import brightspot.rte.LargeRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.search.boost.HasSiteSearchBoostIndexes;
import brightspot.search.modifier.exclusion.SearchExcludable;
import brightspot.search.sortalphabetical.AlphabeticallySortable;
import brightspot.section.HasSecondarySectionsWithField;
import brightspot.section.HasSectionWithField;
import brightspot.section.Section;
import brightspot.seo.SeoWithFields;
import brightspot.share.Shareable;
import brightspot.sharedcontent.SharedContent;
import brightspot.site.DefaultSiteMapItem;
import brightspot.tag.HasTagsWithField;
import brightspot.urlslug.HasUrlSlugWithField;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.form.Note;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Utils;
import com.psddev.feed.FeedItem;
import com.psddev.suggestions.Suggestable;

@ToolUi.IconName("settings_voice")
@Recordable.DisplayName("Podcast")
@ToolUi.FieldDisplayOrder({
        "title",
        "description",
        "hasUrlSlug.urlSlug",
        "coverArt",
        "body",
        "gatewayEpisode",
        "hasSectionWithField.section",
        "hasTags.tags",
        "embargoable.embargo",
        "seo.title",
        "seo.suppressSeoDisplayName",
        "seo.description",
        "seo.keywords",
        "seo.robots",
        "ampPage.ampDisabled"
})
public class PodcastPage extends Content implements
        AlphabeticallySortable,
        AppleRssFeedWithFields,
        CascadingPageElements,
        Embargoable,
        DefaultSiteMapItem,
        FeedItem,
        HasBreadcrumbs,
        HasPodcastProvidersMetadataWithField,
        HasSecondarySectionsWithField,
        HasSectionWithField,
        HasSiteSearchBoostIndexes,
        HasTagsWithField,
        HasUrlSlugWithField,
        LandingPageElements,
        Page,
        PagePromotableWithOverrides,
        Podcast,
        PodcastPromotable,
        RssFeedWithFields,
        Section,
        SearchExcludable,
        SeoWithFields,
        Shareable,
        SharedContent,
        Suggestable {

    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String title;

    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String description;

    @Required
    @Note("Cover art image must have minimum dimensions of 1400x1400 pixels and aspect ratio of 1:1.")
    private WebImage coverArt;

    @ToolUi.RichText(toolbar = LargeRichTextToolbar.class, lines = 10, inline = false)
    private String body;

    @DisplayName("Gateway Episode (Best Episode)")
    @Where(HasPodcastData.PODCAST_FIELD + " = ?")
    private PodcastEpisode gatewayEpisode;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WebImage getCoverArt() {
        return coverArt;
    }

    public void setCoverArt(WebImage coverArt) {
        this.coverArt = coverArt;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public PodcastEpisode getGatewayEpisode() {
        return gatewayEpisode;
    }

    public void setGatewayEpisode(PodcastEpisode gatewayEpisode) {
        this.gatewayEpisode = gatewayEpisode;
    }

    // --- Has Breadcrumbs Support ---
    @Override
    public List<Section> getBreadcrumbs() {
        List<Section> ancestors = getSectionAncestors();
        Collections.reverse(ancestors);
        return ancestors;
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

    // --- Linkable support ---
    @Override
    public String getLinkableText() {
        return getPagePromotableTitle();
    }

    // --- SeoHooks support ---

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
        return Utils.toNormalized(RichTextUtils.richTextToPlainText(getTitle()));
    }

    // --- Promotable implementation ---

    @Override
    public String getPagePromotableTitleFallback() {
        return getTitle();
    }

    @Override
    public String getPagePromotableDescriptionFallback() {
        return getDescription();
    }

    @Override
    public WebImageAsset getPagePromotableImageFallback() {
        return getCoverArt();
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

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return RichTextUtils.richTextToPlainText(getTitle());
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
        return getSeoTitle();
    }

    @Override
    public String getFeedLink(Site site) {
        return Permalink.getPermalink(site, this);
    }

    @Override
    public String getSuggestableText() {
        return Optional.ofNullable(RichTextUtils.richTextToPlainText(getTitle())).orElse("") + " "
                + Optional.ofNullable(getDescription())
                .map(RichTextUtils::stripRichTextElements)
                .map(RichTextUtils::richTextToPlainText)
                .orElse("");
    }

    @Override
    public String getSectionDisplayNameRichText() {
        return getTitle();
    }

    @Override
    public String getSectionDisplayNamePlainText() {
        return RichTextUtils.richTextToPlainText(getTitle());
    }

    public List<ModulePlacement> getContents() {
        return Optional.ofNullable(as(LandingCascadingData.class)
                        .getContent(as(Site.ObjectModification.class).getOwner()))
                .orElseGet(ArrayList::new);
    }

    @Override
    public String getPodcastPromotableTitle() {
        return getPagePromotableTitle();
    }

    @Override
    public String getPodcastPromotableDescription() {
        return getPagePromotableDescription();
    }

    @Override
    public WebImageAsset getPodcastPromotableImage() {
        return getPagePromotableImage();
    }

    // - AlphabeticallySortable

    @Override
    public String getAlphabeticallySortableIndexValue() {
        return RichTextUtils.richTextToPlainText(getPodcastPromotableTitle());
    }

    // ---FeedItem support ---
    @Override
    public List<? extends RssFeedItem> getRssFeedItems() {
        return Query.from(AbstractPodcastEpisodePage.class)
            .where(HasPodcastData.PODCAST_FIELD + " = ?", this)
            .and("rss.item.getRssFeedItemPubDate != missing")
            .sortDescending("rss.item.getRssFeedItemPubDate")
            .select(0, getNumberOfRssFeedItems())
            .getItems();
    }

    @Override
    public String getRssFeedTitleFallback() {

        return RichTextUtils.richTextToPlainText(getPagePromotableTitleFallback());
    }

    @Override
    public String getRssFeedDescriptionFallback() {

        return RichTextUtils.richTextToPlainText(getPagePromotableDescriptionFallback());
    }

    @Override
    public String getAppleRssTitleFallback() {

        return getRssFeedTitleFallback();
    }

    @Override
    public RssWebImage getAppleRssCoverArtFallback() {
        return (RssWebImage) getCoverArt();
    }
}
