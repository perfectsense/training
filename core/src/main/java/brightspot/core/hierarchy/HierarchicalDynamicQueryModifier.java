package brightspot.core.hierarchy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import brightspot.core.listmodule.DynamicQueryModifier;
import brightspot.core.tool.TaxonUtils;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

public interface HierarchicalDynamicQueryModifier extends DynamicQueryModifier {

    String HIERARCHICAL_CLUSTER = "Hierarchy";

    default <T extends Recordable, S extends Hierarchical> void updateQueryWithHierarchicalQuery(
        Query<?> query,
        Object mainObject,
        Collection<? super S> ancestors,
        Class<T> closestAncestorType,
        Class<S> ancestorType) {

        if (query == null || mainObject == null || ObjectUtils.isBlank(ancestors)) {

            return;
        }

        // The ancestors to use for filtering
        Set<S> ancestorSet = new HashSet<S>();

        // Adding all the actual ancestors (not the ClosestAncestorSingleton) to the set
        ancestors.stream()
            .filter(ancestorType::isInstance)
            .map(ancestorType::cast)
            .forEach(ancestorSet::add);

        // Finding the ClosestAncestor Singleton
        boolean useCurrent = ancestors.parallelStream()
            .filter(closestAncestorType::isInstance)
            .map(closestAncestorType::cast)
            .findAny()
            .isPresent();

        if (useCurrent) {

            S closestAncestor = this.getClosestAncestor(ancestorType, mainObject);

            if (closestAncestor != null) {

                ancestorSet.add(closestAncestor);
            }
        }

        if (ObjectUtils.isBlank(ancestorSet) && useCurrent) {

            // Cause empty set to be returned (IDs are never null)
            // ONLY IF
            // - no ancestors were provided
            // - the ClosestAncestor Singleton was provided
            // - no closest ancestor was found
            query.where("id = null");

        } else {

            this.addHierarchicalQuery(query, mainObject, ancestorSet);
        }
    }

    default <T extends Hierarchical> T getClosestAncestor(Class<T> ancestorType, Object mainObject) {
        T current = null;

        if (ancestorType != null && mainObject != null && mainObject instanceof Recordable) {

            Recordable recordable = (Recordable) mainObject;

            if (recordable.isInstantiableTo(ancestorType)) {

                current = recordable.as(ancestorType);

            } else if (recordable.isInstantiableTo(Hierarchical.class)) {

                current = TaxonUtils.getClosestAncestorOfType(recordable.as(Hierarchical.class), ancestorType);
            }
        }

        return current;
    }

    default <T extends Hierarchical> void addHierarchicalQuery(
        Query<?> query, Object mainObject,
        Set<T> ancestors) {

        query.where(HierarchicalData.class.getName() + "/" + HierarchicalData.ANCESTORS_FIELD
            + " = ?", ancestors);

        if (mainObject instanceof Recordable) {

            // Filter current object
            query.and("id != " + ((Recordable) mainObject).getState().getId());
        }
    }
}
