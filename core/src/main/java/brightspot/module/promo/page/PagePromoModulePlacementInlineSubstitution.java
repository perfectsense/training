package brightspot.module.promo.page;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import brightspot.module.ModulePlacement;
import brightspot.promo.page.InternalPagePromoItem;
import brightspot.promo.page.PagePromotable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Copyable;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.ui.LocalizationContext;
import com.psddev.cms.ui.ToolLocalization;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.cms.ui.content.place.GroupedPlaceItem;
import com.psddev.cms.ui.content.place.Place;
import com.psddev.cms.ui.content.place.Placeable;
import com.psddev.cms.ui.content.place.SelectablePlace;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Substitution;
import com.psddev.dari.web.WebRequest;

/**
 * The purpose of implementing {@link Interchangeable} and {@link Copyable} on this
 * class is to provide support for CMS embedded conversion between
 * {@link PagePromoModulePlacementInline} and {@link PagePromoModulePlacementShared}.
 *
 * The purpose of implementing {@link GroupedPlaceItem} on this class is to provide
 * support for CMS post-publish placement actions via {@link com.psddev.cms.ui.content.place.PlacePostPublishAction}.
 */
public class PagePromoModulePlacementInlineSubstitution extends PagePromoModulePlacementInline implements
        Copyable,
        GroupedPlaceItem,
        Interchangeable,
        Substitution {

    // --- Copyable support ---

    @Override
    public void onCopy(Object source) {
        if (source instanceof PagePromoModulePlacementShared) {
            getState().remove("internalName");
        }
    }

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        if (target instanceof PagePromoModulePlacementShared) {

            PagePromoModule sharedModule = Copyable.copy(ObjectType.getInstance(PagePromoModule.class), this);
            String inlineLabel = getLabel();
                        String sharedLabel = ToolLocalization.text(new LocalizationContext(
                    ModulePlacement.class,
                    ImmutableMap.of(
                            "label",
                            ObjectUtils.to(UUID.class, inlineLabel) != null
                                    ? ToolLocalization.text(this.getClass(), "label.untitled")
                                    : inlineLabel,
                            "date",
                            ToolLocalization.dateTime(new Date().getTime()))), "convertLabel");
            // Internal Name field of the shared module will be set to this inline module's label with a converted copy text suffix
            sharedModule.setInternalName(sharedLabel);

            // Publish converted module
            Content.Static.publish(
                    sharedModule,
                    WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite(),
                    WebRequest.getCurrent().as(ToolRequest.class).getCurrentUser());

            // Update the shared placement to reference the newly-published shared module
            PagePromoModulePlacementShared sharedPlacement = ((PagePromoModulePlacementShared) target);
            sharedPlacement.setShared(sharedModule);

            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        return ImmutableList.of(
                ObjectType.getInstance(PagePromoModulePlacementShared.class).getId()
        );
    }

    // --- GroupedPlaceItem support ---
    @Override
    public List<Place> getGroupedPlaceItemPlaces(Placeable source, Recordable target) {
        return source instanceof PagePromotable ? Collections.singletonList(
                new SelectablePlace(
                        target,
                        this,
                        () -> {
                            setItem(ObjectUtils.build(new InternalPagePromoItem(), l -> l.setItem((PagePromotable) source)));
                            setTitleOverride(null);
                            setDescriptionOverride(null);
                            setImageOverride(null);
                            setCategoryOverride(null);
                        }
                )
        ) : null;
    }
}
