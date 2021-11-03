package brightspot.recipe;

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
public class RecipeArticle extends AbstractRichTextArticle {

    @Required
    private Recipe recipe;

    // --- Getters/setters ---

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
