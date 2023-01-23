package brightspot.module.container;

import java.util.List;

import brightspot.module.ModulePlacement;
import brightspot.module.tabs.TabsModulePlacementInline;
import brightspot.module.tabs.TabsModulePlacementShared;
import com.psddev.dari.db.Alteration;

public class OneColumnContainerAlteration extends Alteration<AbstractOneColumnContainer> {

    @TypesExclude({ TabsModulePlacementInline.class, TabsModulePlacementShared.class,
            OneColumnContainerPlacementInline.class, TwoColumnContainerPlacementInline.class,
            ThreeColumnContainerPlacementInline.class, FourColumnContainerPlacementInline.class,
            ContainerModulePlacementShared.class })
    private List<ModulePlacement> columnOne;
}
