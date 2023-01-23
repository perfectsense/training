package brightspot.module.list.page;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import brightspot.rte.list.PageListRichTextElement;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.ContentEditDrawerItem;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.ui.content.place.GroupedPlace;
import com.psddev.cms.ui.content.place.GroupedPlaceItem;
import com.psddev.cms.ui.content.place.Place;
import com.psddev.cms.ui.content.place.Placeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Substitution;

/**
 * The purpose of the {@link Interchangeable} implementation on this class is to
 * provide drag and drop support for {@link PageListModule} from the CMS UI
 * Shelf into {@link PageListModulePlacementShared}.
 */
public class PageListModuleSubstitution extends PageListModule implements
        ContentEditDrawerItem,
        Interchangeable,
        GroupedPlaceItem,
        Substitution {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        // Support the following types for Shelf drag and drop:
        // - PageListModulePlacementShared
        if (target instanceof PageListModulePlacementShared) {
            PageListModulePlacementShared pageListModulePlacementShared = (PageListModulePlacementShared) target;
            pageListModulePlacementShared.setShared(this);
            return true;
        } else if (target instanceof PageListRichTextElement) {
            PageListRichTextElement pageListRichTextElement = (PageListRichTextElement) target;
            pageListRichTextElement.setList(this);
            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        // Support the following types for Shelf drag and drop:
        // - PageListModulePlacementShared
        return ImmutableList.of(
                getState().getTypeId(), // enable dragging and dropping as itself from the shelf
                ObjectType.getInstance(PageListModulePlacementShared.class).getId(),
                ObjectType.getInstance(PageListRichTextElement.class).getId()
        );
    }

    @Override
    public List<Place> getGroupedPlaceItemPlaces(Placeable source, Recordable target) {
        return Collections.singletonList(new GroupedPlace(source, target, "List: " + getLabel(), getItemStream()));
    }
}
