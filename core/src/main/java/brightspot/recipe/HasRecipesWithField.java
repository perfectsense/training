package brightspot.recipe;

import java.util.List;

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
