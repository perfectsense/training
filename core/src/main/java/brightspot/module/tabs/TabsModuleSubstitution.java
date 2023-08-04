package brightspot.module.tabs;

import brightspot.module.container.ContainerModulePlacementExclusion;
import com.psddev.dari.util.Substitution;

/**
 * Substitution to prevent Tabs modules from being placed in a Container Module
 */
public class TabsModuleSubstitution extends TabsModule implements Substitution, ContainerModulePlacementExclusion {

}
