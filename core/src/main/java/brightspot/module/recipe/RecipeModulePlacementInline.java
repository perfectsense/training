package brightspot.module.recipe;

import brightspot.module.ModulePlacement;
import com.psddev.dari.db.Recordable;

/**
 * An inline (i.e. embedded and therefore not shared) Recipe module.
 *
 * @see RecipeModulePlacementShared
 */

@Recordable.DisplayName("Recipe")
public class RecipeModulePlacementInline extends AbstractRecipeModule implements ModulePlacement {

}
