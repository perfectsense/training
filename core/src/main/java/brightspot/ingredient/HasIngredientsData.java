package brightspot.ingredient;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.UnresolvedState;

@Recordable.FieldInternalNamePrefix(HasIngredientsData.FIELD_PREFIX)
public class HasIngredientsData extends Modification<HasIngredients> {

    static final String FIELD_PREFIX = "hasIngredients.";
    public static final String INGREDIENTS_FIELD = FIELD_PREFIX + "getIngredients";
    public static final String INGREDIENT_NAMES_FIELD = FIELD_PREFIX + "getIngredientNames";

    @Indexed
    @ToolUi.Hidden
    public List<Ingredient> getIngredients() {
        return UnresolvedState.resolveAndGet(getOriginalObject(), HasIngredients::getIngredients);
    }

    @Indexed
    @ToolUi.Hidden
    public Set<String> getIngredientNames() {
        return getIngredients()
            .stream()
            .map(Ingredient::getNamePlainText)
            .collect(Collectors.toSet());
    }
}
