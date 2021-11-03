package brightspot.recipe;

import java.util.List;

/**
 * Extension of {@link HasRecipes} adding an editorial field for recipes.
 *
 * @see HasRecipesWithFieldData
 */
public interface HasRecipesWithField extends HasRecipes {

    default HasRecipesWithFieldData asHasRecipesWithFieldData() {
        return as(HasRecipesWithFieldData.class);
    }

    // --- HasRecipes support ---

    @Override
    default List<Recipe> getRecipes() {
        return asHasRecipesWithFieldData().getRecipes();
    }
}
