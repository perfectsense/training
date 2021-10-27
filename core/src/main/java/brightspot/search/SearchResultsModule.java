package brightspot.search;

import brightspot.itemstream.PathedOnlyQueryModifiableWithField;
import brightspot.l10n.LocaleDynamicQueryModifiable;
import brightspot.module.list.page.PagePromo;
import brightspot.module.search.AbstractSearchResultsModule;
import brightspot.promo.page.PagePromotable;
import brightspot.query.QueryBuilderDynamicQueryModifiable;
import brightspot.util.NoUrlsWidget;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Search Results")
public class SearchResultsModule extends AbstractSearchResultsModule implements
        LocaleDynamicQueryModifiable,
        NoUrlsWidget,
        PathedOnlyQueryModifiableWithField,
        QueryBuilderDynamicQueryModifiable {

    @Override
    public Object transformSiteSearchResult(Object object) {
        return object instanceof Recordable && ((Recordable) object).isInstantiableTo(PagePromotable.class)
                ? PagePromo.fromPromotable(((Recordable) object).as(PagePromotable.class))
                : object;
    }
}
