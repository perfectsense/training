package brightspot.module.recipe;

import brightspot.module.SharedModule;
import brightspot.util.NoUrlsWidget;
import com.psddev.cms.db.ToolUi;

/**
 * A Recipe module which is saved to the database and can therefore be shared among multiple placements.
 *
 * @see RecipeModulePlacementInline
 */

@ToolUi.FieldDisplayOrder({
    "internalName"
})
public class RecipeModule extends AbstractRecipeModule implements
    NoUrlsWidget,
    SharedModule {

    @Required
    private String internalName;

    // --- Getters/setters ---

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return getInternalName();
    }
}
