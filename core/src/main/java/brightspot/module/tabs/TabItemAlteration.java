package brightspot.module.tabs;

import java.util.List;

import brightspot.module.ModulePlacement;
import brightspot.module.container.ContainerModulePlacementShared;
import brightspot.module.container.FourColumnContainerPlacementInline;
import brightspot.module.container.OneColumnContainerPlacementInline;
import brightspot.module.container.ThreeColumnContainerPlacementInline;
import brightspot.module.container.TwoColumnContainerPlacementInline;
import com.psddev.dari.db.Alteration;

public class TabItemAlteration extends Alteration<TabItem> {

    @TypesExclude({ TabsModulePlacementInline.class, TabsModulePlacementShared.class,
            OneColumnContainerPlacementInline.class, TwoColumnContainerPlacementInline.class,
            ThreeColumnContainerPlacementInline.class, FourColumnContainerPlacementInline.class,
            ContainerModulePlacementShared.class })
    private List<ModulePlacement> tabContent;
}
