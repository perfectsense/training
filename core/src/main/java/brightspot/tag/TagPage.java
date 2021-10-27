package brightspot.tag;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import brightspot.anchor.AnchorLinkable;
import brightspot.anchor.Anchorage;
import brightspot.cascading.CascadingPageElements;
import brightspot.image.WebImageAsset;
import brightspot.landing.LandingCascadingData;
import brightspot.landing.LandingPageElements;
import brightspot.module.ModulePlacement;
import brightspot.module.list.page.DynamicPageItemStream;
import brightspot.module.list.page.PageListModulePlacementInline;
import brightspot.page.ModulePageLead;
import brightspot.page.Page;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.permalink.DefaultPermalinkRule;
import brightspot.permalink.Permalink;
import brightspot.rss.DynamicFeedSource;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.seo.SeoWithFields;
import brightspot.share.Shareable;
import brightspot.site.DefaultSiteMapItem;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.feed.FeedItem;
import com.psddev.suggestions.SuggestionsInitField;

@ToolUi.FieldDisplayOrder({
    "displayName",
    "internalName",
    "description",
    "parent",
    "hidden",
    "lead",
    "landingCascading.content"
})
@SuggestionsInitField("displayName")
@ToolUi.IconName("local_offer")
@Recordable.DisplayName("Tag")
public class TagPage extends Content implements
    Anchorage,
    CascadingPageElements,
    DynamicFeedSource,
    DefaultSiteMapItem,
    LandingPageElements,
    Page,
    SeoWithFields,
    Shareable,
    Tag {

    @Required
    @ToolUi.CssClass("is-half")
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String displayName;

    @ToolUi.Placeholder(dynamicText = "${content.getDisplayNamePlainText()}", editable = true)
    private String internalName;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String description;

    @Where("_id != ?")
    private TagPage parent;

    @ToolUi.Note("Note: If enabled, this tag will be hidden from display on asset pages and modules")
    private boolean hidden;

    // @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    @ToolUi.Note("If a Lead is added, it will appear before the content.")
    private ModulePageLead lead;

    @ToolUi.Hidden
    @Ignored(false)
    public String getInternalName() {
        return ObjectUtils.firstNonNull(internalName, getDisplayNamePlainText());
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

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public TagPage getParent() {
        return parent;
    }

    public void setParent(TagPage parent) {
        this.parent = parent;
    }

    public ModulePageLead getLead() {
        return lead;
    }

    public void setLead(ModulePageLead lead) {
        this.lead = lead;
    }

    public String getDisplayNamePlainText() {
        return RichTextUtils.richTextToPlainText(getDisplayName());
    }

    public List<ModulePlacement> getContents() {
        return Optional.ofNullable(as(LandingCascadingData.class)
            .getContent(as(Site.ObjectModification.class).getOwner()))
            .orElseGet(this::defaultContents);
    }

    public List<ModulePlacement> defaultContents() {
        TagMatch tagMatch = new TagMatch();
        tagMatch.setIncludeCurrentTags(true);

        DynamicPageItemStream itemStream = new DynamicPageItemStream();
        itemStream.asQueryBuilderDynamicQueryModifier().setQueryBuilder(tagMatch);

        PageListModulePlacementInline listModule = new PageListModulePlacementInline();
        listModule.setItemStream(itemStream);

        List<ModulePlacement> contents = new ArrayList<>();
        contents.add(listModule);

        return contents;
    }

    // --- Recordable Support ---

    @Override
    public String getLabel() {
        return getInternalName();
    }

    // --- Linkable Support

    @Override
    public String getLinkableText() {
        return getDisplayName();
    }

    // --- SeoWithFields support ---

    @Override
    public String getSeoTitleFallback() {
        return getDisplayNamePlainText();
    }

    @Override
    public String getSeoDescriptionFallback() {
        return null;
    }

    // --- Tag Support ---

    @Override
    public String getTagDisplayNameRichText() {
        return getDisplayName();
    }

    @Override
    public String getTagDisplayNamePlainText() {
        return RichTextUtils.richTextToPlainText(getDisplayName());
    }

    @Override
    public boolean isHiddenTag() {
        return isHidden();
    }

    @Override
    public Tag getTagParent() {
        return getParent();
    }

    // --- Anchorable Support ---

    @Override
    public Set<AnchorLinkable> getAnchors() {

        // LinkedHashSet for order within the SectionPage
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

    // --- Shareable Support ---

    @Override
    public String getShareableTitleFallback() {
        return getSeoTitle();
    }

    @Override
    public String getShareableDescriptionFallback() {
        return getSeoDescription();
    }

    @Override
    public WebImageAsset getShareableImageFallback() {
        return Optional.ofNullable(getLead())
                .map(ModulePageLead::getModulePageLeadImage)
                .orElse(null);
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
        TagMatch tagMatch = new TagMatch();
        tagMatch.setIncludeCurrentTags(true);

        DynamicPageItemStream itemStream = new DynamicPageItemStream();
        itemStream.asQueryBuilderDynamicQueryModifier().setQueryBuilder(tagMatch);

        return getFeedFromDynamicStream(itemStream, getContents(), site);
    }

    @Override
    public String getFeedLanguage(Site site) {
        return getDynamicFeedLanguage(site);
    }

    // --- Directory.Item Support ---

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, DefaultPermalinkRule.class);
    }
}
