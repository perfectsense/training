package brightspot.core.pkg;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import brightspot.core.anchor.Anchorable;
import brightspot.core.anchor.Anchorage;
import brightspot.core.image.ImageOption;
import brightspot.core.lead.Lead;
import brightspot.core.link.Linkable;
import brightspot.core.module.ModuleType;
import brightspot.core.page.Page;
import brightspot.core.page.TypeSpecificCascadingPageElements;
import brightspot.core.permalink.AbstractPermalinkRule;
import brightspot.core.permalink.DefaultPermalinkRule;
import brightspot.core.promo.PromotableWithOverrides;
import brightspot.core.readreceipt.Readable;
import brightspot.core.share.Shareable;
import brightspot.core.site.ExpressSiteMapItem;
import brightspot.core.video.VideoPageElements;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.theme.StyleEmbeddedContentCreator;

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
@ToolUi.IconName("view_compact")
public class Package extends Content implements
    Anchorage,
    Directory.Item,
    ExpressSiteMapItem,
    Linkable,
    PackagePageElements,
    PackageOrCurrentPackage,
    Page,
    PromotableWithOverrides,
    Readable,
    Shareable,
    TypeSpecificCascadingPageElements,
    VideoPageElements {

    public static final String PROMOTABLE_TYPE = "package";

    @DisplayName("Internal Name")
    @ToolUi.Placeholder(dynamicText = "${content.displayName}", editable = true)
    private String name;

    @Required
    @ToolUi.DisplayBefore("name")
    private String displayName;

    @ToolUi.Note("If enabled, the Display Name will not be shown on the frontend")
    private boolean hideDisplayName;

    @Embedded
    private PackageBanner banner;

    @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    @ToolUi.Note("If a Lead is added, it will appear before the content.")
    private Lead lead;

    @DisplayName("Content")
    @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    @ToolUi.Note("If Content is added, it will replace the dynamic results.")
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

    public PackageBanner getBanner() {
        return banner;
    }

    public void setBanner(PackageBanner banner) {
        this.banner = banner;
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
    public String getLinkableText() {
        return getDisplayName();
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
    public String getLabel() {
        return getName();
    }

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, DefaultPermalinkRule.class);
    }
}
