package brightspot.core.section;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import brightspot.core.anchor.Anchorable;
import brightspot.core.anchor.Anchorage;
import brightspot.core.hierarchy.Hierarchy;
import brightspot.core.image.ImageOption;
import brightspot.core.lead.Lead;
import brightspot.core.link.Linkable;
import brightspot.core.listmodule.ItemStream;
import brightspot.core.listmodule.ListModuleItemStream;
import brightspot.core.module.ModuleType;
import brightspot.core.oneoffpage.OneOffPage;
import brightspot.core.page.Page;
import brightspot.core.permalink.AbstractPermalinkRule;
import brightspot.core.promo.PromotableWithOverrides;
import brightspot.core.readreceipt.Readable;
import brightspot.core.rss.DynamicFeedSource;
import brightspot.core.share.Shareable;
import brightspot.core.site.ExpressSiteMapItem;
import brightspot.core.tool.DirectoryItemUtils;
import brightspot.core.tool.RichTextUtils;
import brightspot.core.tool.SmallRichTextToolbar;
import brightspot.core.tool.TaxonUtils;
import brightspot.core.video.VideoPageElements;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Localization;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.Taxon;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;
import com.psddev.feed.FeedItem;
import com.psddev.theme.StyleEmbeddedContentCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Recordable.TypePostProcessorClasses(SectionPostProcessor.class)
@Seo.TitleFields("getSeoTitle")
@Seo.DescriptionFields("getSeoDescription")
@ToolUi.FieldDisplayOrder({
    "displayName", "name", "hideDisplayName", "description", "parent",
    "sectionCascading.sectionNavigation", "lead", "promotable.promoTitle", "promotable.promoDescription",
    "promotable.promoImage", "shareable.shareTitle", "shareable.shareDescription", "shareable.shareImage",
    "page.layoutOption" })
@ToolUi.IconName("view_compact")
public class Section extends Content implements
        Anchorage,
        Directory.Item,
        DynamicFeedSource,
        ExpressSiteMapItem,
        Linkable,
        Hierarchy,
        Page,
        PromotableWithOverrides,
        Readable,
        SectionOrCurrentSection,
        SectionPageElements,
        Shareable,
        VideoPageElements {

    public static final String PROMOTABLE_TYPE = "section";
    private static final String PREVIOUS_PARENTS_EXTRA = "previousParents";

    private static final Logger LOGGER = LoggerFactory.getLogger(Section.class);

    @DisplayName("Internal Name")
    @ToolUi.Placeholder(dynamicText = "${content.displayName}", editable = true)
    private String name;

    @Required
    @Indexed
    @ToolUi.DisplayBefore("name")
    private String displayName;

    @ToolUi.NoteHtml("<span data-dynamic-html='${content.hideDisplayNameNoteHtml}'></span>")
    private boolean hideDisplayName;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String description;

    @Indexed
    @Where("_id != ?")
    @Types({ Section.class, OneOffPage.class })
    // @ToolUi.DropDown TODO: BSP-1515
    private Hierarchy parent;

    @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    @ToolUi.NoteHtml("<span data-dynamic-html='${content.leadNoteHtml}'></span>")
    private Lead lead;

    @DisplayName("Content")
    @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    @ToolUi.NoteHtml("<span data-dynamic-html='${content.contentsNoteHtml}'></span>")
    private List<ModuleType> contents;

    public String getHideDisplayNameNoteHtml() {
        return Localization.currentUserText(Section.class, "message.hideDisplayNameNote",
            "If enabled, the Display Name will not be shown on the frontend.");
    }

    public String getLeadNoteHtml() {
        return Localization.currentUserText(Section.class, "message.leadNote",
            "If a Lead is added, it will appear before the content.");
    }

    public String getContentsNoteHtml() {
        return Localization.currentUserText(Section.class, "message.contentsNote",
            "If Content is added, it will replace the dynamic results.");
    }

    @Override
    public Hierarchy getParent() {
        return parent;
    }

    public void setParent(Hierarchy parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Ignored(false)
    @ToolUi.Hidden
    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isHideDisplayName() {
        return hideDisplayName;
    }

    public void setHideDisplayName(boolean hideDisplayName) {
        this.hideDisplayName = hideDisplayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public List<ModuleType> getContents() {
        if (contents == null) {
            contents = new ArrayList<>();
        }
        return contents;
    }

    public void setContents(List<ModuleType> contents) {
        this.contents = contents;
    }

    @Override
    public boolean isRoot() {
        return getParent() == null;
    }

    @Override
    public Collection<? extends Taxon> getChildren() {
        return Query.from(Section.class).where("parent = ?", this).selectAll();
    }

    @Override
    public Hierarchy getHierarchicalParent() {
        return getParent();
    }

    @Override
    public String getLinkableText() {
        return getDisplayName();
    }

    @Override
    public Set<Anchorable> getAnchors() {

        // LinkedHashSet to maintain order of items
        Set<Anchorable> anchors = new LinkedHashSet<>();

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

    @Override
    protected void afterDelete() {

        recalculateTagsAndAncestors();
    }

    @Override
    protected void afterSave() {

        if (getState().getExtras().containsKey(PREVIOUS_PARENTS_EXTRA)) {
            recalculateTagsAndAncestors();
        }
    }

    private void recalculateTagsAndAncestors() {

        TaxonUtils.recalculateMethodByQuery(
            Query.fromAll()
                .where(
                    SectionableData.class.getName() + "/" + SectionableData.SECTION_AND_ANCESTORS_FIELD + " = ?",
                    this),
            SectionableData.SECTION_AND_ANCESTORS_FIELD
        );
    }

    @Ignored(false)
    @ToolUi.Hidden
    public String getSeoTitle() {
        return getDisplayName();
    }

    @Ignored(false)
    @ToolUi.Hidden
    public String getSeoDescription() {
        return getPromotableDescriptionFallback();
    }

    @Override
    public String getLabel() {
        String name = getName();
        return !StringUtils.isBlank(name) ? name : getDisplayName();
    }

    /* Shareable Implementation */

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

    /* Promotable Implementation */

    @Override
    public String getPromotableTitleFallback() {
        return getDisplayName();
    }

    @Override
    public String getPromotableDescriptionFallback() {
        return RichTextUtils.richTextToPlainText(getDescription());
    }

    @Override
    public ImageOption getPromotableImageFallback() {
        return Optional.ofNullable(getLead())
            .map(Lead::getLeadImage)
            .orElse(getContents().stream().filter(e -> e.getModuleTypeImage() != null)
                .findFirst()
                .map(ModuleType::getModuleTypeImage)
                .orElse(null));
    }

    @Override
    public String getPromotableType() {
        return PROMOTABLE_TYPE;
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
        return DirectoryItemUtils.getCanonicalUrl(site, this);
    }

    @Override
    public List<FeedItem> getFeedItems(Site site) {
        ItemStream itemStream = ListModuleItemStream.createDynamic();

        itemStream.as(SectionDynamicQueryModifier.class)
            .getSections()
            .add(Query.from(CurrentSection.class).first());

        return getFeedFromDynamicStream(itemStream, getContents(), site);
    }

    @Override
    public String getFeedLanguage(Site site) {
        return getDynamicFeedLanguage(site);
    }

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, this::createPathString);
    }

    private String createPathString(Site site) {

        String relativeCanonicalUrl = DirectoryItemUtils.getRelativeCanonicalUrl(site, this);

        if (!StringUtils.isBlank(relativeCanonicalUrl)) {
            return relativeCanonicalUrl;
        }

        String sectionSegment = StringUtils.toNormalized(getDisplayName());

        if (StringUtils.isBlank(sectionSegment)) {
            return null;
        }

        Hierarchy parentSection = getParent();

        return StringUtils.ensureEnd(StringUtils.ensureStart(
                Optional.ofNullable(parentSection)
                        .filter(Directory.Item.class::isInstance)
                        .map(Directory.Item.class::cast)
                        .map(s -> s.createPermalink(site))
                        .orElse(""),
                "/"
        ), "/") + sectionSegment;
    }
}
