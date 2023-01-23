package brightspot.module.list.stat;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Substitution;

/**
 * The purpose of the {@link Interchangeable} implementation on this class is to
 * provide drag and drop support for {@link StatListModule} from the CMS UI
 * Shelf into {@link StatListModulePlacementShared}.
 */
public class StatListModuleSubstitution extends StatListModule implements
        Interchangeable,
        Substitution {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        // Support the following types for Shelf drag and drop:
        // - StatListModulePlacementShared
        if (target instanceof StatListModulePlacementShared) {
            StatListModulePlacementShared statListModulePlacementShared = (StatListModulePlacementShared) target;
            statListModulePlacementShared.setShared(this);
            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        // Support the following types for Shelf drag and drop:
        // - StatListModulePlacementShared
        return ImmutableList.of(
                getState().getTypeId(), // enable dragging and dropping as itself from the shelf
                ObjectType.getInstance(StatListModulePlacementShared.class).getId()
        );
    }
}
