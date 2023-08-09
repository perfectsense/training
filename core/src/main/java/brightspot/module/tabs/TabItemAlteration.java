package brightspot.module.tabs;

import java.util.List;

import brightspot.module.ModulePlacement;
import brightspot.module.SharedModulePlacementTypes;
import brightspot.module.container.FourColumnContainerPlacementInline;
import brightspot.module.container.OneColumnContainerPlacementInline;
import brightspot.module.container.ThreeColumnContainerPlacementInline;
import brightspot.module.container.TwoColumnContainerPlacementInline;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Alteration;

public class TabItemAlteration extends Alteration<TabItem> {

    @TypesExclude({
        TabsModulePlacementInline.class, OneColumnContainerPlacementInline.class,
        TwoColumnContainerPlacementInline.class, ThreeColumnContainerPlacementInline.class,
        FourColumnContainerPlacementInline.class })
    @SharedModulePlacementTypes(types = SharedModuleTabPlacement.class)
    @ToolUi.AddShared(sharedClass = SharedModuleTabPlacement.class, field = "shared")
    private List<ModulePlacement> tabContent;
}
