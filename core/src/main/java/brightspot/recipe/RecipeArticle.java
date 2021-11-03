package brightspot.recipe;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import brightspot.article.AbstractRichTextArticle;
import com.psddev.cms.db.ToolUi;

/**
 * An article focused on a specific recipe.
 *
 * @see Recipe
 */

@ToolUi.FieldDisplayOrder({
    "headline",
    "subheadline",
    "hasUrlSlug.urlSlug",
    "recipe",
    "hasAuthorsWithField.authors",
    "lead",
    "body",
    "hasSectionWithField.section",
    "hasTags.tags",
    "embargoable.embargo"
})
public class RecipeArticle extends AbstractRichTextArticle implements
    HasRecipes {

    @Required
    private Recipe recipe;

    // --- Getters/setters ---

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    // --- HasRecipes support ---

    @Override
    public List<Recipe> getRecipes() {
        return Optional.ofNullable(getRecipe())
            .map(Collections::singletonList)
            .orElse(Collections.emptyList());
    }
}
