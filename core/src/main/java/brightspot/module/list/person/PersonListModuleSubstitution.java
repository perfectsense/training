package brightspot.module.list.person;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Substitution;

/**
 * The purpose of the {@link Interchangeable} implementation on this class is to
 * provide drag and drop support for {@link PersonListModule} from the CMS UI
 * Shelf into {@link PersonListModulePlacementShared}.
 */
public class PersonListModuleSubstitution extends PersonListModule implements
        Interchangeable,
        Substitution {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        // Support the following types for Shelf drag and drop:
        // - PersonListModulePlacementShared
        if (target instanceof PersonListModulePlacementShared) {
            PersonListModulePlacementShared personListModulePlacementShared = (PersonListModulePlacementShared) target;
            personListModulePlacementShared.setShared(this);
            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        // Support the following types for Shelf drag and drop:
        // - PersonListModulePlacementShared
        return ImmutableList.of(
                getState().getTypeId(), // enable dragging and dropping as itself from the shelf
                ObjectType.getInstance(PersonListModulePlacementShared.class).getId()
        );
    }
}
