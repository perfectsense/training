package brightspot.module.container;

import brightspot.module.tabs.TabsModulePlacementExclusion;
import com.psddev.dari.db.Record;
import com.psddev.dari.util.Substitution;
import com.psddev.dari.util.SubstitutionTarget;

/**
 * Substitution to prevent Container modules from being placed in a Tabs Module
 */
@SubstitutionTarget(AbstractContainerModule.class)
public class AbstractContainerModuleSubstitution extends Record implements TabsModulePlacementExclusion, Substitution {

}
