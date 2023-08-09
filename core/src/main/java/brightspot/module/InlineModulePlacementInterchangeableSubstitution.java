package brightspot.module;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import brightspot.module.container.SharedModuleContainerPlacement;
import brightspot.module.tabs.SharedModuleTabPlacement;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Copyable;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.ui.LocalizationContext;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Substitution;
import com.psddev.dari.util.SubstitutionTarget;
import com.psddev.dari.web.WebRequest;

import static com.psddev.cms.ui.ToolLocalization.*;

@SubstitutionTarget(InlineModulePlacement.class)
public class InlineModulePlacementInterchangeableSubstitution extends Record
    implements Interchangeable, Copyable, Substitution {

    @Override
    public void onCopy(Object source) {
        if (source instanceof SharedModulePlacement) {
            /*
             * We can't use "this" to call methods directly due to java.lang.IncompatibleClassChangeError errors.
             * Instead, call methods on "self" which is a copy of "this" that is cast to the correct type.
             */
            InlineModulePlacement self = ((InlineModulePlacement) this);
            self.getState().remove("internalName");
        }
    }

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        if (target instanceof SharedModulePlacement) {

            /*
             * We can't use "this" to call methods directly due to java.lang.IncompatibleClassChangeError errors.
             * Instead, call methods on "self" which is a copy of "this" that is cast to the correct type.
             */
            InlineModulePlacement self = ((InlineModulePlacement) this);
            SharedModule sharedModule = Copyable.copy(ObjectType.getInstance(self.as(InlineModulePlacement.class)
                .getSharedModuleClass()), self);
            String inlineLabel = self.getLabel();
            String sharedLabel = text(new LocalizationContext(ModulePlacement.class, ImmutableMap.of(
                "label",
                ObjectUtils.to(UUID.class, inlineLabel) != null || inlineLabel == null ? text(
                    self.getClass(),
                    "label.untitled") : inlineLabel,
                "date",
                dateTime(new Date().getTime()))), "convertLabel");

            // Internal Name field of the shared module will be set to this inline module's label with a converted copy text suffix
            sharedModule.getState().put("internalName", sharedLabel);

            // Publish converted module
            Content.Static.publish(
                sharedModule,
                WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite(),
                WebRequest.getCurrent().as(ToolRequest.class).getCurrentUser());

            // Update the shared placement to reference the newly-published shared module
            SharedModulePlacement sharedPlacement = ((SharedModulePlacement) target);
            sharedPlacement.setSharedModule(sharedModule);

            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {
        ObjectType type = ObjectType.getInstance(recordable.as(InlineModulePlacement.class)
            .getSharedModuleClass());
        if (!WebRequest.getCurrent().as(ToolRequest.class).hasPermission("type/" + type.getId() + "/create")) {
            return Collections.emptyList();
        }

        return ImmutableList.of(
            ObjectType.getInstance(SharedModulePagePlacement.class).getId(),
            ObjectType.getInstance(SharedModuleTabPlacement.class).getId(),
            ObjectType.getInstance(SharedModuleContainerPlacement.class).getId()
        );
    }
}
