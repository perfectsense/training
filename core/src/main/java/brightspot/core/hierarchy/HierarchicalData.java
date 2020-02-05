package brightspot.core.hierarchy;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import brightspot.core.tool.TaxonUtils;
import com.google.common.collect.Lists;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Recordable.FieldInternalNamePrefix(HierarchicalData.FIELD_PREFIX)
public class HierarchicalData extends Modification<Hierarchical> {

    public static final String INTERNAL_NAME = "brightspot.core.hierarchy.HierarchicalData";
    public static final String FIELD_PREFIX = "hierarchical.";
    public static final String ANCESTORS_FIELD = FIELD_PREFIX + "getAncestors";
    public static final String PREVIOUS_PARENTS = "previousParents";

    @ToolUi.Hidden
    @ToolUi.Filterable
    @Indexed
    @DisplayName("Ancestors")
    public Set<? extends Hierarchical> getAncestors() {

        return Optional.ofNullable(this.as(Hierarchical.class))
            .map(TaxonUtils::getHierarchicalAncestors)
            .orElse(null);
    }

    /**
     * Returns the {@link List} of {@link Hierarchical Hierarchicals} representing the ancestors (or "Breadcrumbs") of
     * the {@link Hierarchical} in order from the most distant to closest ancestor.
     *
     * @return a {@link List} of {@link Hierarchical Hierarchicals} or an empty {@link List}.
     */
    public List<Hierarchical> getBreadcrumbs() {

        return Optional.ofNullable(this.as(Hierarchical.class))
            .map(s -> TaxonUtils.getHierarchicalAncestors(s, Hierarchical::getHierarchicalParent))
            .map(Lists::reverse)
            .orElseGet(Collections::emptyList);
    }

    @Override
    protected void beforeCommit() {

        Object previousVersion = Query.fromAll()
            .where("_id = ?", this)
            .noCache()
            .first();

        if (previousVersion instanceof Recordable
            && ((Recordable) previousVersion).isInstantiableTo(Hierarchical.class)) {

            Hierarchical hierarchical = ((Recordable) previousVersion).as(Hierarchical.class);
            Hierarchical thisHierarchical = this.as(Hierarchical.class);

            // TODO: account for overlaid draft scenario on Tag?
            if (hierarchical != null
                && thisHierarchical != null
                && !ObjectUtils
                .equals(thisHierarchical.getHierarchicalParent(), hierarchical.getHierarchicalParent())) {
                getState().getExtras()
                    .put(PREVIOUS_PARENTS, Collections.singleton(thisHierarchical.getHierarchicalParent()));
            }
        }
    }

    @Override
    protected void afterDelete() {

        recalculateTagsAndAncestors();
    }

    @Override
    protected void afterSave() {

        if (getState().getExtras().containsKey(PREVIOUS_PARENTS)) {
            recalculateTagsAndAncestors();
        }
    }

    private void recalculateTagsAndAncestors() {

        TaxonUtils.recalculateMethodByQuery(
            Query.fromAll()
                .where(
                    HierarchicalData.class.getName() + "/" + HierarchicalData.ANCESTORS_FIELD + " = ?",
                    this),
            HierarchicalData.ANCESTORS_FIELD
        );
    }
}
