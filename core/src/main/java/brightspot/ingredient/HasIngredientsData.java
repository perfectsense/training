package brightspot.ingredient;

import java.util.List;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.UnresolvedState;

@Recordable.FieldInternalNamePrefix(HasIngredientsData.FIELD_PREFIX)
public class HasIngredientsData extends Modification<HasIngredients> {

    static final String FIELD_PREFIX = "hasIngredients.";
    public static final String INGREDIENTS_FIELD = FIELD_PREFIX + "getIngredients";

    @Indexed
    @ToolUi.Hidden
    public List<Ingredient> getIngredients() {
        return UnresolvedState.resolveAndGet(getOriginalObject(), HasIngredients::getIngredients);
    }
}
