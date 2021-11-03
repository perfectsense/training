package brightspot.recipe;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;

import brightspot.l10n.CurrentLocale;
import brightspot.util.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.recipe.RecipeModuleView;
import com.psddev.styleguide.recipe.RecipeModuleViewDirectionsField;
import com.psddev.styleguide.recipe.RecipeModuleViewImageField;
import com.psddev.styleguide.recipe.RecipeModuleViewIngredientsField;
import com.psddev.styleguide.recipe.RecipeModuleViewTitleField;
import org.threeten.extra.AmountFormats;

public class RecipeViewModel extends ViewModel<Recipe> implements RecipeModuleView {

    @CurrentLocale
    private Locale locale;

    @Override
    public CharSequence getCookTime() {
        return formatDuration(model.getCookTime());
    }

    @Override
    public CharSequence getDifficulty() {
        return Optional.ofNullable(model.getDifficulty())
            .map(Difficulty::toString)
            .orElse(null);
    }

    @Override
    public Iterable<? extends RecipeModuleViewDirectionsField> getDirections() {
        return RichTextUtils.buildHtml(
            model,
            Recipe::getDirections,
            e -> createView(RecipeModuleViewDirectionsField.class, e));
    }

    @Override
    public Iterable<? extends RecipeModuleViewImageField> getImage() {
        return createViews(RecipeModuleViewImageField.class, model.getImage());
    }

    @Override
    public CharSequence getInactivePrepTime() {
        return formatDuration(model.getInactivePrepTime());
    }

    @Override
    public Iterable<? extends RecipeModuleViewIngredientsField> getIngredients() {
        return RichTextUtils.buildHtml(
            model,
            Recipe::getIngredients,
            e -> createView(RecipeModuleViewIngredientsField.class, e));
    }

    @Override
    public CharSequence getPrepTime() {
        return formatDuration(model.getPrepTime());
    }

    @Override
    public Iterable<? extends RecipeModuleViewTitleField> getTitle() {
        return RichTextUtils.buildInlineHtml(
            model,
            Recipe::getTitle,
            e -> createView(RecipeModuleViewTitleField.class, e));
    }

    @Override
    public CharSequence getTotalTime() {
        return formatDuration(model.getTotalTime());
    }

    private String formatDuration(Integer minutes) {
        if (minutes == null) {
            return null;
        }

        return AmountFormats.wordBased(Duration.of(minutes, ChronoUnit.MINUTES), locale);
    }
}
