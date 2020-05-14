package brightspot.core.tool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import brightspot.core.hierarchy.Hierarchical;
import brightspot.core.taxon.ExpressTaxon;
import com.google.common.base.Preconditions;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.UnresolvedState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TaxonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxonUtils.class);

    private TaxonUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <T extends ExpressTaxon> Set<T> getTaxonsAndAncestors(Iterable<T> expressTaxons) {
        return getTaxonsAndAncestors(expressTaxons, t -> (Set<T>) t.getParents());
    }

    public static <T> Set<T> getTaxonsAndAncestors(Iterable<T> taxons, Function<T, Set<T>> getParents) {

        Set<T> taxonsAndAncestors = new HashSet<>();

        StreamSupport.stream(taxons.spliterator(), false)
            .map(UnresolvedState::resolve)
            .filter(Objects::nonNull)
            .forEach(t -> {
                taxonsAndAncestors.add(t);
                taxonsAndAncestors.addAll(TaxonUtils.getAncestors(t, getParents));
            });

        return taxonsAndAncestors;
    }

    public static <T> List<T> getAncestors(T taxon, Function<T, Set<T>> getParents) {

        List<T> ancestors = new ArrayList<>();
        Set<T> processed = new HashSet<>();
        Set<T> pending = new HashSet<>();

        Optional.ofNullable(getParents)
            .map(parents -> parents.apply(taxon))
            .ifPresent(parents -> parents.stream()
                .filter(Objects::nonNull)
                .forEach(pending::add));

        ancestors.addAll(pending);

        while (!pending.isEmpty()) {
            T ancestor = pending.iterator().next();
            pending.remove(ancestor);

            Optional.ofNullable(getParents)
                .map(parents -> parents.apply(ancestor))
                .ifPresent(parents -> parents.stream()
                    .filter(Objects::nonNull)
                    .filter(p -> !processed.contains(p))
                    .forEach(p -> {
                        pending.add(p);
                        ancestors.add(p);
                    })
                );

            processed.add(ancestor);
        }

        return ancestors;
    }

    public static Set<Hierarchical> getHierarchicalAncestors(Hierarchical hierarchical) {

        Set<Hierarchical> ancestors = new LinkedHashSet<>();

        Optional.ofNullable(hierarchical)
            .map(Hierarchical::getHierarchicalParent)
            .map(UnresolvedState::resolve)
            .ifPresent(parent -> {
                ancestors.add(parent);
                ancestors.addAll(TaxonUtils.getHierarchicalAncestors(parent, Hierarchical::getHierarchicalParent));
            });

        return ancestors;
    }

    public static List<Hierarchical> getHierarchicalAncestors(
        Hierarchical hierarchical,
        Function<Hierarchical, Hierarchical> getParent) {

        List<Hierarchical> ancestors = new ArrayList<>();
        Set<Hierarchical> processed = new LinkedHashSet<>();
        Set<Hierarchical> pending = new LinkedHashSet<>();

        Optional.ofNullable(getParent)
            .map(parent -> parent.apply(hierarchical))
            .ifPresent(pending::add);

        ancestors.addAll(pending);

        while (!pending.isEmpty()) {
            Hierarchical ancestor = pending.iterator().next();
            pending.remove(ancestor);

            Optional.ofNullable(getParent)
                .map(parent -> parent.apply(ancestor))
                .filter(p -> !processed.contains(p))
                .ifPresent(p -> {
                    pending.add(p);
                    ancestors.add(p);
                });

            processed.add(ancestor);
        }

        return ancestors;
    }

    public static <T extends Hierarchical> T getClosestAncestorOfType(Hierarchical descendant, Class<T> type) {

        return getHierarchicalAncestors(descendant, Hierarchical::getHierarchicalParent)
            .stream()
            .filter(Objects::nonNull)
            .filter(ancestor -> ancestor.isInstantiableTo(type))
            .map(ancestor -> ancestor.as(type))
            .findFirst()
            .orElse(null);
    }

    /**
     * @deprecated replaced by {@link #recalculateMethodByQuery(Query, String)}
     */
    @Deprecated
    public static <T extends Recordable> void recalculate(Class<T> clazz, Query query, String methodName) {

        Preconditions.checkNotNull(clazz);
        Preconditions.checkNotNull(query);
        Preconditions.checkNotNull(methodName);

        String className = clazz.getName();

        Database db = Database.Static.getDefault();
        String taskName = "recalculate " + className + " objects";
        Consumer<String> logger = LOGGER::info;

        Function<T, Boolean> processor = recordable -> {

            recordable.getState().getType().getMethod(methodName).recalculate(recordable.getState());

            return true;

        };

        int numProcessors = 5;
        int numWriters = 5;
        int batchSize = 200;
        boolean commitEventually = true;
        Runnable finished = null;

        LOGGER.info("Creating Task to index " + className + " objects");
        TaskUtils.asyncProcessQuery(
            db,
            taskName,
            logger,
            clazz,
            query,
            processor,
            null,
            numProcessors,
            numWriters,
            batchSize,
            commitEventually,
            finished);
    }

    public static void recalculateMethodByQuery(Query<Object> query, String methodName) {

        Preconditions.checkNotNull(query);
        Preconditions.checkNotNull(methodName);

        new TaxonRecalculation(query, methodName).saveImmediately();
    }
}
