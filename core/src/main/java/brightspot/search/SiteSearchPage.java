package brightspot.search;

import brightspot.itemstream.PathedOnlyQueryModifiableWithField;
import brightspot.itemstream.SiteItemsQueryModifiable;
import brightspot.l10n.LocaleDynamicQueryModifiable;
import brightspot.link.Linkable;
import brightspot.module.list.page.PagePromo;
import brightspot.page.Page;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.permalink.Permalink;
import brightspot.promo.page.PagePromotable;
import brightspot.query.HideIncludeCurrentFields;
import brightspot.query.QueryBuilderDynamicQueryModifiable;
import brightspot.search.boost.BoostDynamicQueryModifiable;
import brightspot.search.boost.BoostDynamicQueryModification;
import brightspot.search.boost.IndexBoostFieldProvider;
import brightspot.search.boost.StandardBoostConfiguration;
import brightspot.search.modifier.exclusion.SearchExclusionQueryModifiable;
import brightspot.search.stopwords.StopWordsDynamicQueryModifiable;
import brightspot.seo.SeoWithFields;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.ContentLifecycle;
import com.psddev.dari.db.Recordable;

@ToolUi.FieldDisplayOrder({
    "seo.title",
    "seo.suppressSeoDisplayName",
    "seo.description",
    "seo.keywords",
    "seo.robots",
    "ampPage.ampDisabled"
})
public class SiteSearchPage extends AbstractSiteSearchPage implements
    BoostDynamicQueryModifiable,
    ContentLifecycle,
    Directory.Item,
    HideIncludeCurrentFields,
    Linkable,
    LocaleDynamicQueryModifiable,
    NavigationSearch,
    Page,
    PathedOnlyQueryModifiableWithField,
    QueryBuilderDynamicQueryModifiable,
    SearchExclusionQueryModifiable,
    SeoWithFields,
    SiteItemsQueryModifiable,
    StopWordsDynamicQueryModifiable {

    // --- BoostDynamicQueryModifiable support ---

    @Override
    public SiteSearch getBoostedSiteSearch() {
        return this;
    }

    @Override
    public IndexBoostFieldProvider getIndexedFieldsProvider() {
        return new StandardIndexBoostFieldProvider();
    }

    // --- Directory.Item support ---

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, (s) -> "/search");
    }

    // --- Linkable support ---

    @Override
    public String getLinkableText() {
        return getTitle();
    }

    // --- NavigationSearch support ---

    @Override
    public CharSequence getSearchAction(Site site) {
        return Permalink.getPermalink(site, this);
    }

    // --- SeoWithFields support ---

    @Override
    public String getSeoTitleFallback() {
        return RichTextUtils.richTextToPlainText(getTitle());
    }

    @Override
    public String getSeoDescriptionFallback() {
        return null;
    }

    // --- SiteSearch support ---

    @Override
    public Object transformSiteSearchResult(Object object) {
        return object instanceof Recordable && ((Recordable) object).isInstantiableTo(PagePromotable.class)
            ? PagePromo.fromPromotable(((Recordable) object).as(PagePromotable.class))
            : object;
    }

    // --- ContentLifecycle support ---

    // Defaults site search page boosts to the standard configuration
    @Override
    public void onNew() {
        if (this.as(BoostDynamicQueryModification.class).getBoostConfiguration() == null) {
            this.as(BoostDynamicQueryModification.class).setBoostConfiguration(new StandardBoostConfiguration());
        }
    }
}
