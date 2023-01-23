package brightspot.module.list.page;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Copyable;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.ui.content.place.GroupedPlaceItem;
import com.psddev.cms.ui.content.place.Place;
import com.psddev.cms.ui.content.place.Placeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.Substitution;

/**
 * The purpose of this class is to provide support for CMS embedded conversion between
 * {@link PageListModulePlacementShared} and {@link PageListModulePlacementInline}.
 */
public class PageListModulePlacementSharedSubstitution extends PageListModulePlacementShared implements
        Interchangeable,
        GroupedPlaceItem,
        Substitution {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        if (getShared() == null) {
            return false;
        }

        if (target instanceof PageListModulePlacementInline) {

            PageListModule sharedModule = getShared();
            State targetState = State.getInstance(target);
            targetState.putAll(State.getInstance(Copyable.copy(targetState.getType(), sharedModule)).getSimpleValues());

            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        return ImmutableList.of(
                ObjectType.getInstance(PageListModulePlacementInline.class).getId()
        );
    }

    public List<Place> getGroupedPlaceItemPlaces(Placeable source, Recordable target) {
        return Optional.ofNullable(getShared())
                .map(sharedModule -> ((GroupedPlaceItem) sharedModule).getGroupedPlaceItemPlaces(source, target))
                .orElse(Collections.emptyList());
    }
}
