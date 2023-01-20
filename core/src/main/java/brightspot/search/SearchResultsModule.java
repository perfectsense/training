package brightspot.search;

import java.util.List;
import java.util.UUID;

import brightspot.itemstream.PathedOnlyQueryModifiableWithField;
import brightspot.l10n.LocaleDynamicQueryModifiable;
import brightspot.module.list.page.PagePromo;
import brightspot.module.search.AbstractSearchResultsModule;
import brightspot.module.search.SearchResultsModulePlacementShared;
import brightspot.promo.page.PagePromotable;
import brightspot.query.QueryBuilderDynamicQueryModifiable;
import brightspot.search.modifier.exclusion.SearchExclusionQueryModifiable;
import brightspot.util.NoUrlsWidget;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;

/**
 * The purpose of the {@link Interchangeable} implementation on this class is to
 * provide drag and drop support for {@link SearchResultsModule} from the CMS UI
 * Shelf into {@link SearchResultsModulePlacementShared}.
 */
@Recordable.DisplayName("Search Results")
public class SearchResultsModule extends AbstractSearchResultsModule implements
        Interchangeable,
        LocaleDynamicQueryModifiable,
        NoUrlsWidget,
        PathedOnlyQueryModifiableWithField,
        SearchExclusionQueryModifiable,
        QueryBuilderDynamicQueryModifiable {

    @Override
    public Object transformSiteSearchResult(Object object) {
        return object instanceof Recordable && ((Recordable) object).isInstantiableTo(PagePromotable.class)
                ? PagePromo.fromPromotable(((Recordable) object).as(PagePromotable.class))
                : object;
    }

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        // Support the following types for Shelf drag and drop:
        // - SearchResultsModulePlacementShared
        if (target instanceof SearchResultsModulePlacementShared) {
            SearchResultsModulePlacementShared searchResultsModulePlacementShared = (SearchResultsModulePlacementShared) target;
            searchResultsModulePlacementShared.setShared(this);
            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        // Support the following types for Shelf drag and drop:
        // - SearchResultsModulePlacementShared
        return ImmutableList.of(
                getState().getTypeId(), // enable dragging and dropping as itself from the shelf
                ObjectType.getInstance(SearchResultsModulePlacementShared.class).getId()
        );
    }
}
