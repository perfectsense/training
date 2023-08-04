package brightspot.search;

import java.util.Optional;

import brightspot.search.boost.BoostDynamicQueryModification;
import com.psddev.cms.ui.ToolLocalization;
import com.psddev.cms.ui.form.DynamicNote;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.Recordable;

/**
 * {@link DynamicNote} class that warns about lack of {@link Boost} applicability on a {@link SiteSearchPage} unless
 * sorting is done by {@link Relevance}.
 */
class SiteSearchPageBoostDynamicNote implements DynamicNote {

    @Override
    public Object get(Recordable recordable, ObjectField field) {
        boolean isRelevanceSorting = Optional.ofNullable(recordable.as(AbstractSiteSearchPage.class).getSorting())
            .map(SiteSearchSorting::getSorts)
            .map(sorts -> sorts.stream().anyMatch(sort -> Relevance.class.equals(sort.getSortBy().getClass())))
            .orElse(false);
        if (recordable.as(BoostDynamicQueryModification.class).getBoostConfiguration() != null && !isRelevanceSorting) {
            return ToolLocalization.text(SiteSearchPage.class, "note.boostWarning");
        }

        return null;
    }
}
