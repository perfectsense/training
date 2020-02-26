package brightspot.core.tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import brightspot.core.anchor.Anchorable;
import brightspot.core.anchor.Anchorage;
import brightspot.core.image.ImageOption;
import brightspot.core.lead.Lead;
import brightspot.core.link.Linkable;
import brightspot.core.listmodule.ItemStream;
import brightspot.core.listmodule.ListModuleItemStream;
import brightspot.core.module.ModuleType;
import brightspot.core.page.Page;
import brightspot.core.permalink.AbstractPermalinkRule;
import brightspot.core.permalink.DefaultPermalinkRule;
import brightspot.core.promo.PromotableWithOverrides;
import brightspot.core.readreceipt.Readable;
import brightspot.core.rss.DynamicFeedSource;
import brightspot.core.share.Shareable;
import brightspot.core.site.ExpressSiteMapItem;
import brightspot.core.taxon.ExpressTaxon;
import brightspot.core.tool.DirectoryItemUtils;
import brightspot.core.tool.RichTextUtils;
import brightspot.core.tool.SmallRichTextToolbar;
import brightspot.core.tool.TaxonUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.feed.FeedItem;
import com.psddev.theme.StyleEmbeddedContentCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Seo.TitleFields("getSeoTitle")
@Seo.DescriptionFields("getSeoDescription")
@ToolUi.FieldDisplayOrder({
    "promotable.promoTitle",
    "promotable.promoDescription",
    "promotable.promoImage",
    "shareable.shareTitle",
    "shareable.shareDescription",
    "shareable.shareImage",
    "page.layoutOption" })
@ToolUi.IconName("local_offer")
public class Tag extends Content implements
    Anchorage,
    Directory.Item,
    DynamicFeedSource,
    ExpressSiteMapItem,
    ExpressTaxon<Tag>,
    Linkable,
    Page,
    PromotableWithOverrides,
    Readable,
    Shareable,
    TagOrCurrentTags {

    public static final String PROMOTABLE_TYPE = "tag";
    private static final String PREVIOUS_PARENTS_EXTRA = "previousParents";

    private static final Logger LOGGER = LoggerFactory.getLogger(Tag.class);

    @DisplayName("Internal Name")
    @ToolUi.Placeholder(dynamicText = "${content.displayName}", editable = true)
    private String name;

    @Required
    @ToolUi.DisplayBefore("name")
    private String displayName;

    @ToolUi.Note("If enabled, the Display Name will not be shown on the frontend")
    private boolean hideDisplayName;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String description;

    @Indexed
    @Where("_id != ?")
    private Tag parent;

    @ToolUi.Note("Note: If enabled, this tag will be hidden from display on asset pages and modules")
    private boolean hidden;

    @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    @ToolUi.Note("If a Lead is added, it will appear before the content.")
    private Lead lead;

    @DisplayName("Content")
    @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    @ToolUi.Note("If Content is added, it will replace the dynamic results.")
    private List<ModuleType> contents;

    @Indexed
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

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Tag getParent() {
        return parent;
    }

    public void setParent(Tag parent) {
        this.parent = parent;
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

    @Override
    public List<Tag> getChildren() {
        return Query.from(Tag.class)
            .where("parent = ?", this)
            .selectAll();
    }

    @Override
    public boolean isRoot() {
        return getParent() == null;
    }

    @Override
    public String getLinkableText() {
        return getDisplayName();
    }

    @Override
    public Set<Tag> getParents() {
        return Optional.ofNullable(getParent()).map(Collections::singleton).orElse(null);
    }

    @Override
    protected void beforeCommit() {

        Tag tag = (Tag) Query.fromAll().where("_id = ?", this).noCache().first();

        // TODO: account for overlaid draft scenario on Tag?
        if (tag != null && !ObjectUtils.equals(getParent(), tag.getParent())) {
            getState().getExtras().put(PREVIOUS_PARENTS_EXTRA, Collections.singleton(getParent()));
        }
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
                .where(TaggableData.class.getName() + "/" + TaggableData.TAGS_AND_ANCESTORS_FIELD + " = ?", this),
            TaggableData.TAGS_AND_ANCESTORS_FIELD
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
    public Set<Anchorable> getAnchors() {

        // LinkedHashSet for order within the SectionPage
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

        itemStream.as(TagDynamicQueryModifier.class)
            .getTags()
            .add(Query.from(CurrentTags.class).first());

        return getFeedFromDynamicStream(itemStream, getContents(), site);
    }

    @Override
    public String getFeedLanguage(Site site) {
        return getDynamicFeedLanguage(site);
    }

    /**
     * Returns a {@code List} of Tags given a collection of tag names.
     *
     * @param names A collection ({@code List}, {@code Set}, etc) of Strings for matching Tag names against.
     * @return Tags list
     */
    public static List<Tag> findTags(Collection<String> names) {
        return Query.from(Tag.class).where("getName = ?", names).selectAll();
    }

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, DefaultPermalinkRule.class);
    }
}
