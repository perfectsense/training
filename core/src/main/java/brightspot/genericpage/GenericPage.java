package brightspot.genericpage;

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
import brightspot.mediatype.HasMediaTypeWithOverride;
import brightspot.mediatype.MediaType;
import brightspot.module.HasModularSearchIndexFields;
import brightspot.module.ModulePlacement;
import brightspot.page.ModulePageLead;
import brightspot.page.Page;
import brightspot.page.PageHeading;
import brightspot.page.TypeSpecificCascadingPageElements;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.permalink.Permalink;
import brightspot.promo.page.PagePromotableWithOverrides;
import brightspot.rss.DynamicFeedSource;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.search.boost.HasSiteSearchBoostIndexes;
import brightspot.search.modifier.exclusion.SearchExcludable;
import brightspot.section.HasSectionWithField;
import brightspot.section.Section;
import brightspot.section.SectionPrefixPermalinkRule;
import brightspot.seo.SeoWithFields;
import brightspot.share.Shareable;
import brightspot.site.DefaultSiteMapItem;
import brightspot.util.MoreStringUtils;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ContentEditDrawerItem;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.feed.FeedItem;
import com.psddev.theme.StyleEmbeddedContentCreator;

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
@Recordable.DisplayName("Page")
@ToolUi.IconName("view_compact")
public class GenericPage extends Content implements
    Anchorage,
    CascadingPageElements,
    ContentEditDrawerItem,
    DynamicFeedSource,
    DefaultSiteMapItem,
    HasBreadcrumbs,
    HasMediaTypeWithOverride,
    HasModularSearchIndexFields,
    HasSectionWithField,
    HasSiteSearchBoostIndexes,
    Page,
    PagePromotableWithOverrides,
    SearchExcludable,
    SeoWithFields,
    Shareable,
    TypeSpecificCascadingPageElements {

    @Required
    @ToolUi.CssClass("is-half")
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String displayName;

    @ToolUi.Placeholder(dynamicText = "${content.getDisplayNamePlainText()}", editable = true)
    private String internalName;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String description;

    // @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    @ToolUi.Note("If a Lead is added, it will appear before the content.")
    private ModulePageLead lead = new PageHeading().as(ModulePageLead.class);

    @DisplayName("Contents")
    @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    @Embedded
    private List<ModulePlacement> contents;

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    /**
     * @return rich text
     */
    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
        if (contents == null) {
            contents = new ArrayList<>();
        }
        return contents;
    }

    public void setContents(List<ModulePlacement> contents) {
        this.contents = contents;
    }

    public String getDisplayNamePlainText() {
        return RichTextUtils.richTextToPlainText(getDisplayName());
    }

    @Override
    public String getLabel() {
        return MoreStringUtils.firstNonBlank(
            getInternalName(),
            this::getDisplayNamePlainText);
    }

    @Override
    public String getLinkableText() {
        return getDisplayName();
    }

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

    // --- HasBreadcrumbs support ---

    @Override
    public List<Section> getBreadcrumbs() {
        List<Section> ancestors = getSectionAncestors();
        Collections.reverse(ancestors);
        return ancestors;
    }

    // --- HasModularSearchIndexFields support ---

    @Override
    public Set<String> getModularSearchChildPaths() {
        return Collections.singleton("contents");
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

    // --- SeoWithFields support ---

    @Override
    public String getSeoTitleFallback() {
        return RichTextUtils.richTextToPlainText(getDisplayName());
    }

    @Override
    public String getSeoDescriptionFallback() {
        return null;
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
    public String getPagePromotableTitleFallback() {
        return this.getDisplayName();
    }

    public String getPagePromotableDescriptionFallback() {
        return this.getDescription();
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

    public List<FeedItem> getFeedItems(Site site) {
        return getFeedFromDynamicStream(null, getContents(), site);
    }

    @Override
    public String getFeedLanguage(Site site) {
        return getDynamicFeedLanguage(site);
    }

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, SectionPrefixPermalinkRule.class);
    }
}
