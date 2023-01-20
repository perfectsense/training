package brightspot.module.container;

import java.util.List;

import brightspot.module.ModulePlacement;
import brightspot.module.tabs.TabsModulePlacementInline;
import brightspot.module.tabs.TabsModulePlacementShared;
import com.psddev.dari.db.Alteration;

public class FourColumnContainerAlteration extends Alteration<AbstractFourColumnContainer> {

    @TypesExclude({ TabsModulePlacementInline.class, TabsModulePlacementShared.class,
            OneColumnContainerPlacementInline.class, TwoColumnContainerPlacementInline.class,
            ThreeColumnContainerPlacementInline.class, FourColumnContainerPlacementInline.class,
            ContainerModulePlacementShared.class })
    private List<ModulePlacement> columnOne;

    @TypesExclude({ TabsModulePlacementInline.class, TabsModulePlacementShared.class,
            OneColumnContainerPlacementInline.class, TwoColumnContainerPlacementInline.class,
            ThreeColumnContainerPlacementInline.class, FourColumnContainerPlacementInline.class,
            ContainerModulePlacementShared.class })
    private List<ModulePlacement> columnTwo;

    @TypesExclude({ TabsModulePlacementInline.class, TabsModulePlacementShared.class,
            OneColumnContainerPlacementInline.class, TwoColumnContainerPlacementInline.class,
            ThreeColumnContainerPlacementInline.class, FourColumnContainerPlacementInline.class,
            ContainerModulePlacementShared.class })
    private List<ModulePlacement> columnThree;

    @TypesExclude({ TabsModulePlacementInline.class, TabsModulePlacementShared.class,
            OneColumnContainerPlacementInline.class, TwoColumnContainerPlacementInline.class,
            ThreeColumnContainerPlacementInline.class, FourColumnContainerPlacementInline.class,
            ContainerModulePlacementShared.class })
    private List<ModulePlacement> columnFour;
}
