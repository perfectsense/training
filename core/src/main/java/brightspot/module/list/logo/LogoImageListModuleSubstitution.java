package brightspot.module.list.logo;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Substitution;

/**
 * The purpose of this class is to provide drag and drop support for {@link LogoImageListModule}
 * from the CMS UI Shelf into {@link LogoImageListModulePlacementShared}.
 */
public class LogoImageListModuleSubstitution extends LogoImageListModule implements
        Interchangeable,
        Substitution {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        // Support the following types for Shelf drag and drop:
        // - LogoImageListModulePlacementShared
        if (target instanceof LogoImageListModulePlacementShared) {
            LogoImageListModulePlacementShared logoImageListModulePlacementShared = (LogoImageListModulePlacementShared) target;
            logoImageListModulePlacementShared.setShared(this);
            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        // Support the following types for Shelf drag and drop:
        // - LogoImageListModulePlacementShared
        return ImmutableList.of(
                getState().getTypeId(), // enable dragging and dropping as itself from the shelf
                ObjectType.getInstance(LogoImageListModulePlacementShared.class).getId()
        );
    }
}
