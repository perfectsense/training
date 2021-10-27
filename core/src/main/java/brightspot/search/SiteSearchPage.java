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
import brightspot.query.QueryBuilderDynamicQueryModifiable;
import brightspot.search.boost.BoostDynamicQueryModifiable;
import brightspot.search.stopwords.StopWordsDynamicQueryModifiable;
import brightspot.seo.SeoWithFields;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Recordable;

public class SiteSearchPage extends AbstractSiteSearchPage implements
        BoostDynamicQueryModifiable,
        Directory.Item,
        Linkable,
        LocaleDynamicQueryModifiable,
        NavigationSearch,
        Page,
        PathedOnlyQueryModifiableWithField,
        QueryBuilderDynamicQueryModifiable,
        SeoWithFields,
        SiteItemsQueryModifiable,
        StopWordsDynamicQueryModifiable {

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

    // --- BoostDynamicQueryModifiable support ---

    @Override
    public SiteSearch getBoostedSiteSearch() {
        return this;
    }
}
