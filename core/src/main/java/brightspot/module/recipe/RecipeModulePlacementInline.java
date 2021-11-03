package brightspot.module.recipe;

import brightspot.module.InlineModulePlacement;
import brightspot.module.SharedModule;
import com.psddev.dari.db.Recordable;

/**
 * An inline (i.e. embedded and therefore not shared) Recipe module.
 *
 * @see RecipeModule
 */

@Recordable.DisplayName("Recipe")
public class RecipeModulePlacementInline extends AbstractRecipeModule implements InlineModulePlacement {

    @Override
    public Class<? extends SharedModule> getSharedModuleClass() {
        return RecipeModule.class;
    }
}
