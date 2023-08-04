package brightspot.module.promo.page;

import java.util.Collections;
import java.util.List;

import brightspot.promo.page.InternalPagePromoItem;
import brightspot.promo.page.PagePromotable;
import com.psddev.cms.ui.content.place.GroupedPlaceItem;
import com.psddev.cms.ui.content.place.Place;
import com.psddev.cms.ui.content.place.Placeable;
import com.psddev.cms.ui.content.place.SelectablePlace;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Substitution;

/**
 * The purpose of this class is for implementing {@link GroupedPlaceItem} to provide support for CMS post-publish
 * placement actions via {@link com.psddev.cms.ui.content.place.PlacePostPublishAction}.
 */
public class PagePromoModulePlacementInlineSubstitution extends PagePromoModulePlacementInline implements
    GroupedPlaceItem,
    Substitution {

    // --- GroupedPlaceItem support ---

    @Override
    public List<Place> getGroupedPlaceItemPlaces(Placeable source, Recordable target) {
        return source instanceof PagePromotable ? Collections.singletonList(
            new SelectablePlace(
                target,
                this,
                () -> {
                    setItem(ObjectUtils.build(
                        new InternalPagePromoItem(),
                        l -> l.setItem((PagePromotable) source)));
                    setTitleOverride(null);
                    setDescriptionOverride(null);
                    setImageOverride(null);
                    setCategoryOverride(null);
                }
            )
        ) : null;
    }
}
