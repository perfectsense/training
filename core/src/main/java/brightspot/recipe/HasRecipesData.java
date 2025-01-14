package brightspot.recipe;

import java.util.List;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.UnresolvedState;

@Recordable.FieldInternalNamePrefix(HasRecipesData.FIELD_PREFIX)
public class HasRecipesData extends Modification<HasRecipes> {

    static final String FIELD_PREFIX = "hasRecipes.";
    public static final String RECIPES_FIELD = FIELD_PREFIX + "getRecipes";

    @Indexed
    @ToolUi.Hidden
    public List<Recipe> getRecipes() {
        return UnresolvedState.resolveAndGet(getOriginalObject(), HasRecipes::getRecipes);
    }
}
