package brightspot.recipe;

import java.util.ArrayList;
import java.util.List;

import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("hasRecipes.")
public class HasRecipesWithFieldData extends Modification<HasRecipesWithField> {

    private List<Recipe> recipes;

    public List<Recipe> getRecipes() {
        if (recipes == null) {
            recipes = new ArrayList<>();
        }
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
