package brightspot.homepage;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import brightspot.anchor.AnchorLinkable;
import brightspot.anchor.Anchorage;
import brightspot.cascading.CascadingPageElements;
import brightspot.module.ModulePlacement;
import brightspot.page.ModulePageLead;
import brightspot.page.Page;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.permalink.Permalink;
import brightspot.rss.DynamicFeedSource;
import brightspot.seo.SeoWithFields;
import brightspot.share.Shareable;
import brightspot.site.DefaultSiteMapItem;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.feed.FeedItem;
import com.psddev.theme.StyleEmbeddedContentCreator;
import org.apache.commons.text.StringEscapeUtils;

@ToolUi.IconName("home")
public class Homepage extends Content implements
    Anchorage,
    CascadingPageElements,
    DynamicFeedSource,
    DefaultSiteMapItem,
    Page,
    SeoWithFields,
    Shareable {

    @Required
    private String internalName;

    // @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    @Embedded
    private ModulePageLead lead;

    @DisplayName("Contents")
    @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    @Embedded
    private List<ModulePlacement> content;

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public ModulePageLead getLead() {
        return lead;
    }

    public void setLead(ModulePageLead lead) {
        this.lead = lead;
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

        // adding the anchor(s) of the lead
        Optional.ofNullable(getLead())
            .map(Anchorage::getAnchorsForObject)
            .ifPresent(anchors::addAll);

        // adding the anchor(s) of the content
        getContent().stream()
            .map(Anchorage::getAnchorsForObject)
            .flatMap(Set::stream)
            .forEach(anchors::add);

        return anchors;
    }

    @Override
    public String getLinkableText() {
        return StringEscapeUtils.escapeHtml4(getInternalName());
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
}
