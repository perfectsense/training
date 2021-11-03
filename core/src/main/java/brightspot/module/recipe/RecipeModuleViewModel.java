package brightspot.module.recipe;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;

import brightspot.l10n.CurrentLocale;
import brightspot.recipe.Difficulty;
import brightspot.recipe.Recipe;
import brightspot.util.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.recipe.RecipeModuleView;
import com.psddev.styleguide.recipe.RecipeModuleViewDirectionsField;
import com.psddev.styleguide.recipe.RecipeModuleViewImageField;
import com.psddev.styleguide.recipe.RecipeModuleViewIngredientsField;
import com.psddev.styleguide.recipe.RecipeModuleViewTitleField;
import org.threeten.extra.AmountFormats;

public class RecipeModuleViewModel extends ViewModel<AbstractRecipeModule> implements
    RecipeModuleView {

    @CurrentLocale
    private Locale locale;

    private Recipe recipe;

    @Override
    protected boolean shouldCreate() {
        recipe = model.getRecipe();
        return recipe != null;
    }

    @Override
    public CharSequence getCookTime() {
        return formatDuration(recipe.getCookTime());
    }

    @Override
    public CharSequence getDifficulty() {
        return Optional.ofNullable(recipe.getDifficulty())
            .map(Difficulty::toString)
            .orElse(null);
    }

    @Override
    public Iterable<? extends RecipeModuleViewDirectionsField> getDirections() {
        return RichTextUtils.buildHtml(
            recipe,
            Recipe::getDirections,
            e -> createView(RecipeModuleViewDirectionsField.class, e));
    }

    @Override
    public Iterable<? extends RecipeModuleViewImageField> getImage() {
        return createViews(RecipeModuleViewImageField.class, model.getImage());
    }

    @Override
    public CharSequence getInactivePrepTime() {
        return formatDuration(recipe.getInactivePrepTime());
    }

    @Override
    public Iterable<? extends RecipeModuleViewIngredientsField> getIngredients() {
        return RichTextUtils.buildHtml(
            recipe,
            Recipe::getIngredients,
            e -> createView(RecipeModuleViewIngredientsField.class, e));
    }

    @Override
    public CharSequence getPrepTime() {
        return formatDuration(recipe.getPrepTime());
    }

    @Override
    public Iterable<? extends RecipeModuleViewTitleField> getTitle() {
        return RichTextUtils.buildInlineHtml(
            recipe,
            Recipe::getTitle,
            e -> createView(RecipeModuleViewTitleField.class, e));
    }

    @Override
    public CharSequence getTotalTime() {
        return formatDuration(recipe.getTotalTime());
    }

    private String formatDuration(Integer minutes) {
        if (minutes == null || minutes <= 0) {
            return null;
        }

        return AmountFormats.wordBased(Duration.of(minutes, ChronoUnit.MINUTES), locale);
    }
}
