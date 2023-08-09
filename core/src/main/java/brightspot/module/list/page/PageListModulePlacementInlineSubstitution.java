package brightspot.module.list.page;

import java.util.Collections;
import java.util.List;

import com.psddev.cms.ui.content.place.GroupedPlace;
import com.psddev.cms.ui.content.place.GroupedPlaceItem;
import com.psddev.cms.ui.content.place.Place;
import com.psddev.cms.ui.content.place.Placeable;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Substitution;

/**
 * The purpose of this class is to provide support for Post Publish placement actions in the CMS.
 */
public class PageListModulePlacementInlineSubstitution extends PageListModulePlacementInline implements
    GroupedPlaceItem,
    Substitution {

    @Override
    public List<Place> getGroupedPlaceItemPlaces(Placeable source, Recordable target) {
        return Collections.singletonList(new GroupedPlace(source, target, "List: " + getLabel(), getItemStream()));
    }
}
