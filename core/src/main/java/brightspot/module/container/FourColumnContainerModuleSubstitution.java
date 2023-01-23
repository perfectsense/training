package brightspot.module.container;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Substitution;

/**
 * The purpose of this class is to provide drag and drop support for {@link FourColumnContainerModule}
 * from the CMS UI Shelf into {@link ContainerModulePlacementShared}.
 */
public class FourColumnContainerModuleSubstitution extends FourColumnContainerModule implements
        Interchangeable,
        Substitution {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        // Support the following types for Shelf drag and drop:
        // - ContainerModulePlacementShared
        if (target instanceof ContainerModulePlacementShared) {
            ContainerModulePlacementShared containerModulePlacementShared = (ContainerModulePlacementShared) target;
            containerModulePlacementShared.setShared(this);
            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        // Support the following types for Shelf drag and drop:
        // - ContainerModulePlacementShared
        return ImmutableList.of(
                getState().getTypeId(), // enable dragging and dropping as itself from the shelf
                ObjectType.getInstance(ContainerModulePlacementShared.class).getId()
        );
    }
}
