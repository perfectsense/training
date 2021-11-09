package brightspot.recipe.api;

import java.util.Map;
import java.util.Optional;

import brightspot.image.WebImage;
import brightspot.recipe.Difficulty;
import brightspot.recipe.Recipe;
import brightspot.util.SmartQuotesRichTextPreprocessor;
import com.psddev.cms.image.ImageSize;
import com.psddev.cms.rte.EditorialMarkupRichTextPreprocessor;
import com.psddev.cms.rte.LineBreakRichTextPreprocessor;
import com.psddev.cms.rte.RichTextViewBuilder;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.ImageAttributes;
import com.psddev.styleguide.recipe.RecipeModuleViewDirectionsField;
import com.psddev.styleguide.recipe.RecipeModuleViewIngredientsField;
import com.psddev.styleguide.recipe.RecipeModuleViewTitleField;

/**
 * ViewModel for Recipe for use with APIs.
 *
 * Not to be confused with {@link brightspot.recipe.RecipeViewModel} which is for use with Handlebars.
 */
@ViewInterface("Recipe")
public class RecipeApiViewModel extends ViewModel<Recipe> {

    /**
     * Cook time, in minutes.
     */
    public Integer getCookTime() {
        return model.getCookTime();
    }

    /**
     * @return one of EASY, INTERMEDIATE, EXPERT
     */
    public CharSequence getDifficulty() {
        return Optional.ofNullable(model.getDifficulty())
            .map(Difficulty::name)
            .orElse(null);
    }

    /**
     * @return HTML string
     */
    public CharSequence getDirections() {
        return new RichTextViewBuilder<RecipeModuleViewDirectionsField>(model, Recipe::getDirections)
            .addPreprocessor(new EditorialMarkupRichTextPreprocessor())
            .addPreprocessor(new LineBreakRichTextPreprocessor())
            .addPreprocessor(new SmartQuotesRichTextPreprocessor())
            .elementToView(e -> createView(RecipeModuleViewDirectionsField.class, e))
            .buildHtml();
    }

    /**
     * @return plain-text string
     */
    public String getImageAltText() {
        return Optional.ofNullable(model.getImage())
            .map(WebImage::getAltText)
            .orElse(null);
    }

    @ImageAttributes
    public Map<String, String> getImage() {
        return Optional.ofNullable(model.getImage())
            .map(WebImage::getFile)
            .map(ImageSize::getAttributes)
            .orElse(null);
    }

    /**
     * Inactive prep time, in minutes.
     */
    public Integer getInactivePrepTime() {
        return model.getInactivePrepTime();
    }

    /**
     * @return HTML string
     */
    public CharSequence getIngredients() {
        return new RichTextViewBuilder<>(model, Recipe::getIngredients)
            .addPreprocessor(new EditorialMarkupRichTextPreprocessor())
            .addPreprocessor(new LineBreakRichTextPreprocessor())
            .addPreprocessor(new SmartQuotesRichTextPreprocessor())
            .elementToView(e -> createView(RecipeModuleViewIngredientsField.class, e))
            .buildHtml();
    }

    /**
     * Prep time, in minutes.
     */
    public Integer getPrepTime() {
        return model.getPrepTime();
    }

    public Iterable<RecipeTagApiViewModel> getTags() {
        return createViews(RecipeTagApiViewModel.class, model.getRecipeTags());
    }

    /**
     * @return HTML string
     */
    public CharSequence getTitle() {
        return new RichTextViewBuilder<>(model, Recipe::getTitle)
            .addPreprocessor(new EditorialMarkupRichTextPreprocessor())
            .addPreprocessor(new SmartQuotesRichTextPreprocessor())
            .elementToView(e -> createView(RecipeModuleViewTitleField.class, e))
            .buildHtml();
    }

    /**
     * Total time, in minutes.
     */
    public Integer getTotalTime() {
        return model.getTotalTime();
    }
}
