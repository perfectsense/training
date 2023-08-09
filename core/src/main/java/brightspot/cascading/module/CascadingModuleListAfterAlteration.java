package brightspot.cascading.module;

import java.util.List;

import brightspot.module.ModulePlacement;
import brightspot.module.SharedModulePagePlacement;
import brightspot.module.SharedModulePlacementTypes;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Alteration;

public class CascadingModuleListAfterAlteration extends Alteration<CascadingModuleListAfter> {

    @ToolUi.AddShared(sharedClass = SharedModulePagePlacement.class, field = "shared")
    @SharedModulePlacementTypes(types = SharedModulePagePlacement.class)
    private List<ModulePlacement> items;
}
