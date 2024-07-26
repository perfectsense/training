package brightspot.recipe;

import java.util.List;

import com.psddev.dari.db.Recordable;

public interface HasRecipes extends Recordable {

    /**
     * @return non-null
     */
    List<Recipe> getRecipes();
}
