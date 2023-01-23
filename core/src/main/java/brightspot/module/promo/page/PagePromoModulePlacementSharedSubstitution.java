package brightspot.module.promo.page;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Copyable;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.Substitution;

/**
 * The purpose of this class is to provide support for CMS embedded conversion between
 * {@link PagePromoModulePlacementShared} and {@link PagePromoModulePlacementInline}.
 */
public class PagePromoModulePlacementSharedSubstitution extends PagePromoModulePlacementShared implements
        Interchangeable,
        Substitution {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        if (getShared() == null) {
            return false;
        }

        if (target instanceof PagePromoModulePlacementInline) {

            PagePromoModule sharedModule = getShared();
            State targetState = State.getInstance(target);
            targetState.putAll(State.getInstance(Copyable.copy(targetState.getType(), sharedModule)).getSimpleValues());

            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        return ImmutableList.of(
                ObjectType.getInstance(PagePromoModulePlacementInline.class).getId()
        );
    }
}
