package brightspot.module.list.page;

import java.util.ArrayList;
import java.util.List;

import brightspot.promo.page.PagePromotable;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.cms.ui.content.place.GroupedPlaceItem;
import com.psddev.cms.ui.content.place.Place;
import com.psddev.cms.ui.content.place.Placeable;
import com.psddev.cms.ui.content.place.SelectablePlace;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Substitution;
import com.psddev.dari.web.WebRequest;

import static com.psddev.dari.html.Nodes.*;

public class SimplePageItemStreamSubstitution extends SimplePageItemStream implements Substitution, GroupedPlaceItem {

    @Override
    public List<Place> getGroupedPlaceItemPlaces(Placeable source, Recordable target) {
        if (source instanceof PagePromotable) {
            ToolRequest tool = WebRequest.getCurrent().as(ToolRequest.class);
            List<Place> places = new ArrayList<>();

            for (int i = 0, l = getItems().size(); i < l; ++i) {
                int j = i;
                PagePromotable item = getItems().get(j);

                places.add(new SelectablePlace(
                        target,
                        item.getState().getId().toString() + "+index" + j,
                        SPAN.with(tool.toHtml(item.getState().getType()), text(": "), tool.toHtml(item)),
                        () -> getItems().set(j, (PagePromotable) source)));
            }

            return places;

        } else {
            return null;
        }
    }
}
