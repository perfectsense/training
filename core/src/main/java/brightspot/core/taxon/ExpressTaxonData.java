package brightspot.core.taxon;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.psddev.cms.db.Taxon;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;

public class ExpressTaxonData extends Modification<ExpressTaxon> {

    private static final String PREVIOUS_PARENTS = "express.core.taxon.previousparents";

    @Override
    protected void beforeCommit() {
        persistPreviousParents(getOriginalObject());
    }

    @Override
    protected void afterSave() {
        updateParentsAfterSave(getOriginalObject());
    }

    @Override
    protected void afterDelete() {
        updateParentsAndChildrenAfterDelete(getOriginalObject());
    }

    protected static void persistPreviousParents(ExpressTaxon taxon) {
        Set<? extends ExpressTaxon<?>> currentParents = Optional.ofNullable(taxon.getParents())
            .orElseGet(Collections::emptySet);

        ExpressTaxon previous = Query.from(ExpressTaxon.class).noCache().where("id = ?", taxon).first();
        Set<? extends ExpressTaxon<?>> previousParents = Optional.ofNullable(previous)
            .map(ExpressTaxon::getParents)
            .orElseGet(Collections::emptySet);

        if (!previousParents.equals(currentParents)) {
            taxon.getState().getExtras().put(PREVIOUS_PARENTS, previousParents);
        } else {
            taxon.getState().getExtras().put(PREVIOUS_PARENTS, Collections.emptySet());
        }
    }

    protected static void updateParentsAfterSave(ExpressTaxon taxon) {
        Set<? extends ExpressTaxon<?>> previousParents = (Set<? extends ExpressTaxon<?>>) taxon.getState()
            .getExtra(PREVIOUS_PARENTS);

        if (previousParents != null && !previousParents.equals(taxon.getParents())) {
            Optional.ofNullable(taxon.getParents())
                .map(parents -> (Set<? extends ExpressTaxon<?>>) parents)
                .orElseGet(Collections::emptySet)
                .stream()
                .filter(t -> t.as(Taxon.Data.class).isChildrenEmpty() && t.getChildren().size() == 1)
                .map(Recordable::getState)
                .forEach(State::saveImmediately);

            previousParents.stream()
                .filter(t -> !t.as(Taxon.Data.class).isChildrenEmpty() && t.getChildren().isEmpty())
                .map(Recordable::getState)
                .forEach(State::saveImmediately);
        }
    }

    protected static void updateParentsAndChildrenAfterDelete(ExpressTaxon<?> taxon) {
        Collection<? extends ExpressTaxon> previousChildren = Optional.ofNullable(taxon.getChildren())
            .orElseGet(Collections::emptySet)
            .stream()
            .filter(ExpressTaxon.class::isInstance)
            .map(ExpressTaxon.class::cast)
            .collect(Collectors.toSet());

        Optional.ofNullable(taxon.getParents())
            .map(parents -> (Set<? extends ExpressTaxon<?>>) parents)
            .orElseGet(Collections::emptySet)
            .stream()
            .filter(t -> !t.as(Taxon.Data.class).isChildrenEmpty() && t.getChildren().isEmpty())
            .map(Recordable::getState)
            .forEach(State::saveImmediately);

        previousChildren.stream()
            .filter(t -> {
                Set<? extends ExpressTaxon<?>> parents = t.getParents();
                return parents == null || parents.isEmpty() || (parents.contains(taxon) && parents.size() - 1 == 0);
            })
            .map(Recordable::getState).forEach(State::saveImmediately);

    }
}
