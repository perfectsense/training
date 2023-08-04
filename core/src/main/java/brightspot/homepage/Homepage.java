package brightspot.homepage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import brightspot.anchor.AnchorLinkable;
import brightspot.anchor.Anchorage;
import brightspot.cascading.CascadingPageElements;
import brightspot.module.HasModularSearchIndexFields;
import brightspot.module.ModulePlacement;
import brightspot.module.SharedModulePagePlacement;
import brightspot.module.SharedModulePlacementTypes;
import brightspot.page.Page;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.permalink.Permalink;
import brightspot.rss.DynamicFeedSource;
import brightspot.seo.SeoWithFields;
import brightspot.share.Shareable;
import brightspot.site.DefaultSiteMapItem;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ContentEditDrawerItem;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.content.place.GroupedPlace;
import com.psddev.cms.ui.content.place.Place;
import com.psddev.cms.ui.content.place.Placeable;
import com.psddev.cms.ui.content.place.PlaceableTarget;
import com.psddev.feed.FeedItem;
import com.psddev.theme.StyleEmbeddedContentCreator;

@ToolUi.IconName("home")
@ToolUi.FieldDisplayOrder({
    "seo.title",
    "seo.suppressSeoDisplayName",
    "seo.description",
    "seo.keywords",
    "seo.robots",
    "ampPage.ampDisabled"
})
public class Homepage extends Content implements
    Anchorage,
    CascadingPageElements,
    ContentEditDrawerItem,
    DynamicFeedSource,
    DefaultSiteMapItem,
    HasModularSearchIndexFields,
    Page,
    PlaceableTarget,
    SeoWithFields,
    Shareable {

    @Required
    private String internalName;

    @DisplayName("Content")
    @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    @Embedded
    @ToolUi.AddShared(sharedClass = SharedModulePagePlacement.class, field = "shared")
    @SharedModulePlacementTypes(types = SharedModulePagePlacement.class)
    private List<ModulePlacement> content;

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public List<ModulePlacement> getContent() {
        if (content == null) {
            content = new ArrayList<>();
        }
        return content;
    }

    public void setContent(List<ModulePlacement> content) {
        this.content = content;
    }

    @Override
    public Set<AnchorLinkable> getAnchors() {
        Set<AnchorLinkable> anchors = new LinkedHashSet<>();

        // adding the anchor(s) of the content
        getContent().stream()
            .map(Anchorage::getAnchorsForObject)
            .flatMap(Set::stream)
            .forEach(anchors::add);

        return anchors;
    }

    @Override
    public String getLinkableText() {
        return null;
    }

    @Override
    public String getFeedTitle() {
        return getSeoTitle();
    }

    @Override
    public String getFeedDescription() {
        String description = getSeoDescription();
        return description != null ? description : getFeedTitle();
    }

    @Override
    public List<FeedItem> getFeedItems(Site site) {
        return getFeedFromDynamicStream(null, getContent(), site);
    }

    @Override
    public String getFeedLink(Site site) {
        return Permalink.getPermalink(site, this);
    }

    @Override
    public String getFeedLanguage(Site site) {
        return getDynamicFeedLanguage(site);
    }

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, (s) -> "/");
    }

    // --- HasModularSearchIndexFields support ---

    @Override
    public Set<String> getModularSearchChildPaths() {
        return Collections.singleton("content");
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return getInternalName();
    }

    // --- SeoWithFields support ---

    @Override
    public String getSeoTitleFallback() {
        return null;
    }

    @Override
    public String getSeoDescriptionFallback() {
        return null;
    }

    // --- PlaceableTarget support ---

    @Override
    public List<Place> getPlaceableTargetPlaces(Placeable content) {
        return Collections.singletonList(
            new GroupedPlace(content, this, "Content", getContent())
        );
    }
}
