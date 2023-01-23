package brightspot.blog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import brightspot.anchor.AnchorLinkable;
import brightspot.anchor.Anchorage;
import brightspot.breadcrumbs.HasBreadcrumbs;
import brightspot.cascading.CascadingPageElements;
import brightspot.image.WebImageAsset;
import brightspot.landing.LandingCascadingData;
import brightspot.landing.LandingPageElements;
import brightspot.mediatype.HasMediaTypeWithOverride;
import brightspot.mediatype.MediaType;
import brightspot.module.ModulePlacement;
import brightspot.module.list.page.DynamicPageItemStream;
import brightspot.page.ModulePageLead;
import brightspot.page.Page;
import brightspot.page.TypeSpecificCascadingPageElements;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.permalink.DefaultPermalinkRule;
import brightspot.permalink.Permalink;
import brightspot.promo.page.PagePromotableWithOverrides;
import brightspot.rss.DynamicFeedSource;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.search.boost.HasSiteSearchBoostIndexes;
import brightspot.search.modifier.exclusion.SearchExcludable;
import brightspot.section.HasSectionWithField;
import brightspot.section.Section;
import brightspot.seo.SeoWithFields;
import brightspot.share.Shareable;
import brightspot.site.DefaultSiteMapItem;
import brightspot.util.MoreStringUtils;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.form.DynamicPlaceholderMethod;
import com.psddev.cms.ui.form.EditablePlaceholder;
import com.psddev.dari.db.Recordable;
import com.psddev.feed.FeedItem;

@Recordable.DisplayName("Blog")
@ToolUi.FieldDisplayOrder({
        "displayName",
        "internalName",
        "description",
        "lead",
        "landingCascading.content",
        "hasSectionWithField.section",
        "seo.title",
        "seo.suppressSeoDisplayName",
        "seo.description",
        "seo.keywords",
        "seo.robots",
        "ampPage.ampDisabled"
})
public class BlogPage extends Content implements
        Anchorage,
        Blog,
        CascadingPageElements,
        DynamicFeedSource,
        DefaultSiteMapItem,
        HasBreadcrumbs,
        HasMediaTypeWithOverride,
        HasSectionWithField,
        HasSiteSearchBoostIndexes,
        LandingPageElements,
        Page,
        PagePromotableWithOverrides,
        SearchExcludable,
        Section,
        SeoWithFields,
        Shareable,
        TypeSpecificCascadingPageElements {

    @Required
    @Indexed
    @ToolUi.CssClass("is-half")
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String displayName;

    @EditablePlaceholder
    @DynamicPlaceholderMethod("getDisplayNamePlainText")
    private String internalName;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String description;

    @ToolUi.Note("If a Lead is added, it will appear before the content.")
    private ModulePageLead lead;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ModulePageLead getLead() {
        return lead;
    }

    public void setLead(ModulePageLead lead) {
        this.lead = lead;
    }

    public List<ModulePlacement> getContents() {
        return Optional.ofNullable(as(LandingCascadingData.class)
                .getContent(as(Site.ObjectModification.class).getOwner()))
                .orElseGet(ArrayList::new);
    }

    public String getDisplayNamePlainText() {
        return RichTextUtils.richTextToPlainText(getDisplayName());
    }

    // --- HasSiteSearchBoostIndexes support ---

    @Override
    public String getSiteSearchBoostTitle() {
        return getDisplayName();
    }

    @Override
    public String getSiteSearchBoostDescription() {
        return getDescription();
    }

    // --- Linkable support ---

    @Override
    public String getLinkableText() {
        return getDisplayName();
    }

    // --- SeoWithFields support ---

    @Override
    public String getSeoTitleFallback() {
        return RichTextUtils.richTextToPlainText(getDisplayName());
    }

    @Override
    public String getSeoDescriptionFallback() {
        return RichTextUtils.richTextToPlainText(getPagePromotableDescription());
    }

    // --- Directory.Item support ---

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, DefaultPermalinkRule.class);
    }

    // --- FeedSource support ---

    @Override
    public List<FeedItem> getFeedItems(Site site) {
        BlogMatch blogMatch = new BlogMatch();
        blogMatch.setIncludeCurrentBlog(true);

        DynamicPageItemStream itemStream = new DynamicPageItemStream();
        itemStream.asQueryBuilderDynamicQueryModifier().setQueryBuilder(blogMatch);

        return getFeedFromDynamicStream(itemStream, getContents(), site);
    }

    @Override
    public String getFeedLanguage(Site site) {
        return getDynamicFeedLanguage(site);
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

    /* Shareable Implementation */

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

    /* Promotable Implementation */

    @Override
    public String getPagePromotableType() {
        return Optional.ofNullable(getPrimaryMediaType())
                .map(MediaType::getIconName)
                .orElse(null);
    }

    @Override
    public String getPagePromotableTitleFallback() {
        return this.getDisplayName();
    }

    @Override
    public String getPagePromotableDescriptionFallback() {
        return this.getDescription();
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

    // --- Anchorable Support ---

    @Override
    public Set<AnchorLinkable> getAnchors() {
        // LinkedHashSet to maintain order of items
        Set<AnchorLinkable> anchors = new LinkedHashSet<>();

        // adding the anchor(s) of the lead
        Optional.ofNullable(getLead())
                .map(Anchorage::getAnchorsForObject)
                .ifPresent(anchors::addAll);

        // adding the anchor(s) of the content
        getContents().stream()
                .map(Anchorage::getAnchorsForObject)
                .flatMap(Set::stream)
                .forEach(anchors::add);

        return anchors;
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return MoreStringUtils.firstNonBlank(
            getInternalName(),
            this::getDisplayNamePlainText);
    }

    // --- Section Support ---

    @Override
    public String getSectionDisplayNameRichText() {
        return getDisplayName();
    }

    @Override
    public String getSectionDisplayNamePlainText() {
        return RichTextUtils.richTextToPlainText(getDisplayName());
    }
}
