package brightspot.recipe;

import java.util.List;

import com.psddev.dari.db.Recordable;

/**
 * Content which can be associated to one or more recipes.
 *
 * @see HasRecipesData
 * @see HasRecipesWithField
 */
public interface HasRecipes extends Recordable {

    /**
     * @return non-null
     */
    List<Recipe> getRecipes();
}
