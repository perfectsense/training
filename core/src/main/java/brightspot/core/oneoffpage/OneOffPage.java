package brightspot.core.oneoffpage;

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
import brightspot.core.module.ModuleType;
import brightspot.core.page.Page;
import brightspot.core.permalink.AbstractPermalinkRule;
import brightspot.core.pkg.Packageable;
import brightspot.core.promo.PromotableWithOverrides;
import brightspot.core.readreceipt.Readable;
import brightspot.core.rss.DynamicFeedSource;
import brightspot.core.section.Section;
import brightspot.core.section.SectionPrefixPermalinkRule;
import brightspot.core.section.Sectionable;
import brightspot.core.share.Shareable;
import brightspot.core.site.ExpressSiteMapItem;
import brightspot.core.tool.DirectoryItemUtils;
import brightspot.core.tool.SmallRichTextToolbar;
import brightspot.corporate.tag.CorporateTaggable;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.Taxon;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.feed.FeedItem;
import com.psddev.theme.StyleEmbeddedContentCreator;

@Recordable.DisplayName("Page")
@Seo.TitleFields("getSeoTitle")
@Seo.DescriptionFields("getSeoDescription")
@ToolUi.FieldDisplayOrder({
    "displayName",
    "name",
    "hideDisplayName",
    "description",
    "lead",
    "contents",
    "sectionable.section",
    "packageable.pkg",
    "promotable.promoTitle",
    "promotable.promoDescription",
    "promotable.promoImage",
    "shareable.shareTitle",
    "shareable.shareDescription",
    "shareable.shareImage",
    "page.layoutOption" })
@ToolUi.IconName("view_compact")
public class OneOffPage extends Content implements
    Anchorage,
    Directory.Item,
    CorporateTaggable,
    DynamicFeedSource,
    ExpressSiteMapItem,
    Hierarchy,
    Linkable,
    Packageable,
    Page,
    PromotableWithOverrides,
    Shareable,
    Readable,
    Sectionable {

    public static final String PROMOTABLE_TYPE = "oneOffPage";

    @DisplayName("Internal Name")
    @ToolUi.Placeholder(dynamicText = "${content.getDisplayName()}", editable = true)
    private String name;

    @Required
    @ToolUi.DisplayBefore("name")
    private String displayName;

    @ToolUi.Note("If enabled, the Display Name will not be shown on the frontend")
    private boolean hideDisplayName;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String description;

    @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    private Lead lead;

    @DisplayName("Content")
    @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    private List<ModuleType> contents;

    public String getName() {
        return ObjectUtils.firstNonNull(name, getDisplayName());
    }

    public void setName(String name) {
        this.name = name;
    }

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
    public String getLabel() {
        return getName();
    }

    @Ignored(false)
    @ToolUi.Hidden
    public String getSeoTitle() {
        return getDisplayName();
    }

    @Ignored(false)
    @ToolUi.Hidden
    public String getSeoDescription() {
        return null;
    }

    @Override
    public Hierarchy getParent() {
        return asSectionableData().getSection();
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
        return this.getDisplayName();
    }

    public String getPromotableDescriptionFallback() {
        return this.getDescription();
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
