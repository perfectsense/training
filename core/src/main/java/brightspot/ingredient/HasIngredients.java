package brightspot.ingredient;

import java.util.List;

import com.psddev.dari.db.Recordable;

public interface HasIngredients extends Recordable {

    /**
     * @return non-null
     */
    List<Ingredient> getIngredients();
}
