package brightspot.module.promo.page.dynamic.shared;

import java.util.List;
import java.util.UUID;

import brightspot.itemstream.MemoizationDynamicQueryModifiable;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Substitution;

/**
 * The purpose of the {@link Interchangeable} implementation on this class is to
 * provide drag and drop support for {@link DynamicPagePromoModuleShared} from the CMS UI
 * Shelf into {@link DynamicPagePromoModulePlacementShared}.
 */
public class DynamicPagePromoModuleSharedSubstitution extends DynamicPagePromoModuleShared implements
    Interchangeable,
    MemoizationDynamicQueryModifiable,
    Substitution {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        // Support the following types for Shelf drag and drop:
        // - SearchResultsModulePlacementShared
        if (target instanceof DynamicPagePromoModulePlacementShared) {
            DynamicPagePromoModulePlacementShared dynamicPagePromoModulePlacementShared = (DynamicPagePromoModulePlacementShared) target;
            dynamicPagePromoModulePlacementShared.setShared(this);
            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        // Support the following types for Shelf drag and drop:
        // - DynamicPagePromoModulePlacementShared
        return ImmutableList.of(
                getState().getTypeId(), // enable dragging and dropping as itself from the shelf
                ObjectType.getInstance(DynamicPagePromoModulePlacementShared.class).getId()
        );
    }
}
