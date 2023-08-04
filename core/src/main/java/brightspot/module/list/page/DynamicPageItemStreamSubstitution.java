package brightspot.module.list.page;

import java.util.ArrayList;
import java.util.List;

import brightspot.itemstream.DynamicResult;
import brightspot.itemstream.MemoizationDynamicQueryModifiable;
import brightspot.itemstream.PathedOnlyQueryModifiableWithField;
import brightspot.itemstream.SiteItemsQueryModifiableWithField;
import brightspot.l10n.LocaleDynamicQueryModifiable;
import brightspot.promo.page.InternalPagePromoItem;
import brightspot.promo.page.PagePromotable;
import brightspot.promo.page.PagePromotableDynamicQueryModifiable;
import brightspot.sponsor.SponsorDynamicQueryModifiable;
import com.psddev.cms.db.ToolUser;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.cms.ui.content.place.GroupedPlaceItem;
import com.psddev.cms.ui.content.place.Place;
import com.psddev.cms.ui.content.place.Placeable;
import com.psddev.cms.ui.content.place.SelectablePlace;
import com.psddev.cms.ui.content.place.UnselectablePlace;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Substitution;
import com.psddev.dari.web.WebRequest;

import static com.psddev.dari.html.Nodes.*;

public class DynamicPageItemStreamSubstitution extends DynamicPageItemStream implements
    Substitution,
    GroupedPlaceItem,
    LocaleDynamicQueryModifiable,
    MemoizationDynamicQueryModifiable,
    PagePromotableDynamicQueryModifiable,
    PathedOnlyQueryModifiableWithField,
    SiteItemsQueryModifiableWithField,
    SponsorDynamicQueryModifiable {

    @Override
    public List<Place> getGroupedPlaceItemPlaces(Placeable source, Recordable target) {
        List<Place> places = new ArrayList<>();

        if (source instanceof PagePromotable) {
            ToolRequest tool = WebRequest.getCurrent().as(ToolRequest.class);
            List<PageItemStreamItem> pinned = getPinnedItems();
            places.add(new SelectablePlace(target, getState().getId().toString(), "New Pin", () ->
                pinned.add(createPromo((PagePromotable) source))));

            for (int i = 0, l = pinned.size(); i < l; ++i) {
                int j = i;
                PageItemStreamItem item = pinned.get(j);

                if (!(item instanceof DynamicResult)) {
                    places.add(new SelectablePlace(
                        target,
                        item.getState().getId().toString() + "+index" + j,
                        SPAN.with(tool.toHtml(item.getState().getType()), text(": "), tool.toHtml(item)),
                        () -> pinned.set(j, createPromo((PagePromotable) source))));
                }
            }
        }

        ToolUser user = WebRequest.getCurrent().as(ToolRequest.class).getCurrentUser();

        if (user != null) {
            getQuery(user.getCurrentSetSite(), target)
                .select(0, getItemsPerPage())
                .getItems()
                .stream()
                .map(State::getInstance)
                .map(State::getLabel)
                .map(UnselectablePlace::new)
                .forEach(places::add);
        }

        return places;
    }

    private PagePromo createPromo(PagePromotable promotable) {
        return ObjectUtils.build(
            new PagePromo(),
            pagePromo -> pagePromo.setItem(ObjectUtils.build(
                new InternalPagePromoItem(),
                l -> l.setItem(promotable))));
    }
}
