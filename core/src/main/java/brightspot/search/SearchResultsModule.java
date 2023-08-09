package brightspot.search;

import brightspot.itemstream.PathedOnlyQueryModifiableWithField;
import brightspot.itemstream.SiteItemsQueryModifiable;
import brightspot.l10n.LocaleDynamicQueryModifiable;
import brightspot.module.list.page.PagePromo;
import brightspot.module.search.AbstractSearchResultsModule;
import brightspot.promo.page.PagePromotable;
import brightspot.query.QueryBuilderDynamicQueryModifiable;
import brightspot.search.boost.BoostDynamicQueryModifiable;
import brightspot.search.boost.IndexBoostFieldProvider;
import brightspot.search.modifier.exclusion.SearchExclusionQueryModifiable;
import brightspot.util.NoUrlsWidget;
import com.psddev.dari.db.Recordable;

public class SearchResultsModule extends AbstractSearchResultsModule implements
    BoostDynamicQueryModifiable,
    LocaleDynamicQueryModifiable,
    NoUrlsWidget,
    PathedOnlyQueryModifiableWithField,
    QueryBuilderDynamicQueryModifiable,
    SearchExclusionQueryModifiable,
    SiteItemsQueryModifiable {

    // --- BoostDynamicQueryModifiable support ---

    @Override
    public SiteSearch getBoostedSiteSearch() {
        return this;
    }

    @Override
    public IndexBoostFieldProvider getIndexedFieldsProvider() {
        return new StandardIndexBoostFieldProvider();
    }

    // --- SiteSearch support ---

    @Override
    public Object transformSiteSearchResult(Object object) {
        return object instanceof Recordable && ((Recordable) object).isInstantiableTo(PagePromotable.class)
            ? PagePromo.fromPromotable(((Recordable) object).as(PagePromotable.class))
            : object;
    }
}
