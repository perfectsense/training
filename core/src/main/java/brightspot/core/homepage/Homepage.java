package brightspot.core.homepage;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import brightspot.core.anchor.Anchorable;
import brightspot.core.anchor.Anchorage;
import brightspot.core.lead.Lead;
import brightspot.core.link.Linkable;
import brightspot.core.module.ModuleType;
import brightspot.core.page.Page;
import brightspot.core.permalink.AbstractPermalinkRule;
import brightspot.core.rss.DynamicFeedSource;
import brightspot.core.share.Shareable;
import brightspot.core.site.ExpressSiteMapItem;
import brightspot.core.tool.DirectoryItemUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.feed.FeedItem;
import com.psddev.theme.StyleEmbeddedContentCreator;

@ToolUi.IconName("home")
public class Homepage extends Content implements
    Anchorage,
    Directory.Item,
    DynamicFeedSource,
    ExpressSiteMapItem,
    Linkable,
    Page,
    Shareable {

    @Required
    private String internalName;

    @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    private Lead lead;

    @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    private List<ModuleType> content;

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public List<ModuleType> getContent() {
        if (content == null) {
            content = new ArrayList<>();
        }
        return content;
    }

    public void setContent(List<ModuleType> content) {
        this.content = content;
    }

    @Override
    public Set<Anchorable> getAnchors() {
        Set<Anchorable> anchors = new LinkedHashSet<>();

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
        return getInternalName();
    }

    @Override
    public String getFeedTitle() {
        return as(Seo.ObjectModification.class).findTitle();
    }

    @Override
    public String getFeedDescription() {
        String description = as(Seo.ObjectModification.class).findDescription();
        return description != null ? description : getFeedTitle();
    }

    @Override
    public List<FeedItem> getFeedItems(Site site) {
        return getFeedFromDynamicStream(null, getContent(), site);
    }

    @Override
    public String getFeedLink(Site site) {
        return DirectoryItemUtils.getCanonicalUrl(site, this);
    }

    @Override
    public String getFeedLanguage(Site site) {
        return getDynamicFeedLanguage(site);
    }

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, (s) -> "/");
    }
}
