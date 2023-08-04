package brightspot.section;

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
import brightspot.module.HasModularSearchIndexFields;
import brightspot.module.ModulePlacement;
import brightspot.module.list.page.DynamicPageItemStream;
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
import brightspot.seo.SeoWithFields;
import brightspot.share.Shareable;
import brightspot.site.DefaultSiteMapItem;
import brightspot.sort.publishdate.NewestPublishDate;
import brightspot.taxon.TaxonParentExtension;
import brightspot.urlslug.HasUrlSlug;
import brightspot.util.MoreStringUtils;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ContentEditDrawerItem;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.Taxon;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.ToolLocalization;
import com.psddev.cms.ui.content.place.GroupedPlace;
import com.psddev.cms.ui.content.place.Place;
import com.psddev.cms.ui.content.place.Placeable;
import com.psddev.cms.ui.content.place.PlaceableTarget;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Utils;
import com.psddev.feed.FeedItem;
import com.psddev.suggestions.SuggestionsInitField;

@ToolUi.FieldDisplayOrder({
    "displayName",
    "internalName",
    "description",
    "parent",
    "sectionCascading.sectionNavigation",
    "lead",
    "landingCascading.content",
    "seo.title",
    "seo.suppressSeoDisplayName",
    "seo.description",
    "seo.keywords",
    "seo.robots",
    "ampPage.ampDisabled"
})
@Recordable.DisplayName("Section")
@SuggestionsInitField("displayName")
@ToolUi.IconName("category")
public class SectionPage extends Content implements
    Anchorage,
    CascadingPageElements,
    ContentEditDrawerItem,
    DynamicFeedSource,
    DefaultSiteMapItem,
    HasBreadcrumbs,
    HasModularSearchIndexFields,
    HasSiteSearchBoostIndexes,
    HasUrlSlug,
    LandingPageElements,
    Page,
    PagePromotableWithOverrides,
    PlaceableTarget,
    SearchExcludable,
    Section,
    SectionPageElements,
    SeoWithFields,
    Shareable,
    TaxonParentExtension,
    TypeSpecificCascadingPageElements {

    @Required
    @Indexed
    @ToolUi.CssClass("is-half")
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String displayName;

    @ToolUi.Placeholder(dynamicText = "${content.getSectionDisplayNamePlainText()}", editable = true)
    private String internalName;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String description;

    @Where("_id != ?")
    private Section parent;

    // @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    @ToolUi.NoteHtml("<span data-dynamic-html='${content.leadNoteHtml}'></span>")
    private ModulePageLead lead = new PageHeading().as(ModulePageLead.class);

    public String getLeadNoteHtml() {
        return ToolLocalization.text(
            Section.class,
            "message.leadNote",
            "If a Lead is added, it will appear before the content.");
    }

    public String getContentsNoteHtml() {
        return ToolLocalization.text(
            Section.class,
            "message.contentsNote",
            "If Content is added, it will replace the dynamic results.");
    }

    public Section getParent() {
        return parent;
    }

    public void setParent(Section parent) {
        this.parent = parent;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    /**
     * @return rich text
     */
    @Ignored(false)
    @ToolUi.Hidden
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
        return Optional.ofNullable(as(LandingCascadingData.class)
                .getContent(as(Site.ObjectModification.class).getOwner()))
            .orElseGet(ArrayList::new);
    }

    // --- HasBreadcrumbs support ---

    @Override
    public List<Section> getBreadcrumbs() {
        List<Section> ancestors = getSectionAncestors();
        Collections.reverse(ancestors);
        return ancestors;
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

    // --- Linkable Support ---

    @Override
    public String getLinkableText() {
        return getDisplayName();
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

    // --- HasModularSearchIndexFields support ---

    @Override
    public Set<String> getModularSearchChildPaths() {
        // ignore inherited contents
        return Collections.singleton("landingCascading.content/items");
    }

    // --- HasUrlSlug support ---

    @Override
    public String getUrlSlug() {
        return Utils.toNormalized(getSectionDisplayNamePlainText());
    }

    // --- SeoWithFields support ---

    @Override
    public String getSeoTitleFallback() {
        return RichTextUtils.richTextToPlainText(getDisplayName());
    }

    @Override
    public String getSeoDescriptionFallback() {
        return getPagePromotableDescriptionFallback();
    }

    // --- Recordable Support ---

    @Override
    public String getLabel() {
        return MoreStringUtils.firstNonBlank(
            getInternalName(),
            this::getSectionDisplayNamePlainText);
    }

    // --- Section support ---

    @Override
    public String getSectionDisplayNameRichText() {
        return getDisplayName();
    }

    @Override
    public String getSectionDisplayNamePlainText() {
        return RichTextUtils.richTextToPlainText(getDisplayName());
    }

    @Override
    public Section getSectionParent() {
        return getParent();
    }

    // --- Shareable Support ---

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

    // --- TaxonForNow support ---

    @Override
    public TaxonParentExtension getTaxonParent() {
        return Optional.ofNullable(getParent())
            .filter(TaxonParentExtension.class::isInstance)
            .map(TaxonParentExtension.class::cast)
            .orElse(null);
    }

    @Override
    public Query<? extends Taxon> getTaxonChildrenQuery() {
        return Query.from(Taxon.class)
            .where(
                HasSectionData.class.getName() + "/" + HasSectionData.PARENT_FIELD + " = ?",
                this)
            .and("cms.content.draft = missing OR cms.content.draft = true")
            .and("cms.content.trashed = missing OR cms.content.trashed = true")
            .and("cms.workflow.currentState = missing OR cms.workflow.currentState != missing");
    }

    // --- Promotable Support ---

    @Override
    public String getPagePromotableTitleFallback() {
        return getDisplayName();
    }

    @Override
    public String getPagePromotableDescriptionFallback() {
        return getDescription();
    }

    // --- FeedElement Support ---

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

    // --- FeedSource Support ---

    @Override
    public List<FeedItem> getFeedItems(Site site) {
        AllSectionMatch sectionMatch = new AllSectionMatch();
        sectionMatch.setIncludeCurrentSection(true);

        DynamicPageItemStream itemStream = new DynamicPageItemStream();
        itemStream.asQueryBuilderDynamicQueryModifier().setQueryBuilder(sectionMatch);

        itemStream.setSort(new NewestPublishDate());

        return getFeedFromDynamicStream(itemStream, getContents(), site);
    }

    @Override
    public String getFeedLanguage(Site site) {
        return getDynamicFeedLanguage(site);
    }

    // --- Directory.Item Support ---

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, SectionPrefixPermalinkRule.class);
    }

    @Override
    public List<Place> getPlaceableTargetPlaces(Placeable content) {
        return Collections.singletonList(
            new GroupedPlace(content, this, "Content", getContents())
        );
    }
}
