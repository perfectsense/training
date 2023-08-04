package brightspot.module.list.page;

import java.util.Collections;
import java.util.List;

import com.psddev.cms.db.ContentEditDrawerItem;
import com.psddev.cms.ui.content.place.GroupedPlace;
import com.psddev.cms.ui.content.place.GroupedPlaceItem;
import com.psddev.cms.ui.content.place.Place;
import com.psddev.cms.ui.content.place.Placeable;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Substitution;

/**
 * There are two purposes of this class: 1. Support dragging and dropping of Shared Page List Module to Rich Text
 * Element 2. Support Post Publish Action placement of content.
 */
public class PageListModuleSubstitution extends PageListModule implements
    ContentEditDrawerItem,
    GroupedPlaceItem,
    Substitution {

    @Override
    public List<Place> getGroupedPlaceItemPlaces(Placeable source, Recordable target) {
        return Collections.singletonList(new GroupedPlace(source, target, "List: " + getLabel(), getItemStream()));
    }
}
