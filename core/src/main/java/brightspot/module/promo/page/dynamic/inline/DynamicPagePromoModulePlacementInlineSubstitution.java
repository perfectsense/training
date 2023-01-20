package brightspot.module.promo.page.dynamic.inline;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import brightspot.itemstream.MemoizationDynamicQueryModifiable;
import brightspot.module.ModulePlacement;
import brightspot.module.promo.page.dynamic.shared.DynamicPagePromoModulePlacementShared;
import brightspot.module.promo.page.dynamic.shared.DynamicPagePromoModuleShared;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Copyable;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.ui.LocalizationContext;
import com.psddev.cms.ui.ToolLocalization;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Substitution;
import com.psddev.dari.web.WebRequest;

/**
 * The purpose of this class is to provide support for CMS embedded conversion between
 * {@link DynamicPagePromoModulePlacementInline} and {@link DynamicPagePromoModulePlacementShared}.
 */
public class DynamicPagePromoModulePlacementInlineSubstitution extends DynamicPagePromoModulePlacementInline implements
    Copyable,
    Interchangeable,
    MemoizationDynamicQueryModifiable,
    Substitution {

    // --- Copyable support ---

    @Override
    public void onCopy(Object source) {
        if (source instanceof DynamicPagePromoModulePlacementShared) {
            getState().remove("internalName");
        }
    }

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        if (target instanceof DynamicPagePromoModulePlacementShared) {

            DynamicPagePromoModuleShared sharedModule = Copyable.copy(ObjectType.getInstance(
                    DynamicPagePromoModuleShared.class), this);
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
            DynamicPagePromoModulePlacementShared sharedPlacement = ((DynamicPagePromoModulePlacementShared) target);
            sharedPlacement.setShared(sharedModule);

            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        return ImmutableList.of(
                ObjectType.getInstance(DynamicPagePromoModulePlacementShared.class).getId()
        );
    }
}
