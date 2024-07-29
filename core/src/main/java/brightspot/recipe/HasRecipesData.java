package brightspot.recipe;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.UnresolvedState;

@Recordable.FieldInternalNamePrefix(HasRecipesData.FIELD_PREFIX)
public class HasRecipesData extends Modification<HasRecipes> {

    static final String FIELD_PREFIX = "hasRecipes.";
    public static final String RECIPES_FIELD = FIELD_PREFIX + "getRecipes";
    public static final String RECIPE_TITLES_FIELD = FIELD_PREFIX + "getRecipeTitles";

    @Indexed
    @ToolUi.Hidden
    public List<Recipe> getRecipes() {
        return UnresolvedState.resolveAndGet(getOriginalObject(), HasRecipes::getRecipes);
    }

    @Indexed
    @ToolUi.Hidden
    public Set<String> getRecipeTitles() {
        return getRecipes()
            .stream()
            .map(Recipe::getTitlePlainText)
            .collect(Collectors.toSet());
    }
}
