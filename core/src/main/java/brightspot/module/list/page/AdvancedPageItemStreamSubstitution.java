package brightspot.module.list.page;

import java.util.List;
import java.util.stream.Collectors;

import com.psddev.cms.ui.content.place.GroupedPlaceItem;
import com.psddev.cms.ui.content.place.Place;
import com.psddev.cms.ui.content.place.Placeable;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Substitution;

public class AdvancedPageItemStreamSubstitution extends AdvancedPageItemStream
    implements Substitution, GroupedPlaceItem {

    @Override
    public List<Place> getGroupedPlaceItemPlaces(Placeable source, Recordable target) {
        return getItems().stream()
            .filter(GroupedPlaceItem.class::isInstance)
            .flatMap(i -> ((GroupedPlaceItem) i).getGroupedPlaceItemPlaces(source, target).stream())
            .collect(Collectors.toList());
    }
}
