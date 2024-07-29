package brightspot.recipe;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;

import brightspot.difficulty.Difficulty;
import brightspot.l10n.CurrentLocale;
import brightspot.page.AbstractPageViewModel;
import brightspot.util.RichTextUtils;
import com.psddev.cms.view.PageEntryView;
import com.psddev.styleguide.page.PageViewMainField;
import com.psddev.styleguide.page.PageViewPageHeadingField;
import com.psddev.styleguide.page.PageViewPageLeadField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;
import com.psddev.styleguide.recipe.RecipePageView;
import com.psddev.styleguide.recipe.RecipePageViewDescriptionField;
import com.psddev.styleguide.recipe.RecipePageViewImageField;
import com.psddev.styleguide.recipe.RecipePageViewIngredientsField;
import com.psddev.styleguide.recipe.RecipePageViewSimilarRecipesField;
import com.psddev.styleguide.recipe.RecipePageViewStepsField;
import org.threeten.extra.AmountFormats;

public class RecipePageViewModel extends AbstractPageViewModel<Recipe> implements
    PageEntryView,
    RecipePageView {

    @CurrentLocale
    private Locale locale;

    @Override
    public CharSequence getCookTime() {
        return formatDuration(model.getCookTime());
    }

    @Override
    public Iterable<? extends RecipePageViewDescriptionField> getDescription() {
        return RichTextUtils.buildHtml(
            model,
            Recipe::getDescription,
            e -> createView(RecipePageViewDescriptionField.class, e));
    }

    @Override
    public CharSequence getDifficulty() {
        return Optional.ofNullable(model.getDifficulty())
            .map(Difficulty::toString)
            .orElse(null);
    }

    @Override
    public Iterable<? extends RecipePageViewImageField> getImage() {
        return createViews(RecipePageViewImageField.class, model.getImage());
    }

    @Override
    public CharSequence getInactivePrepTime() {
        return formatDuration(model.getInactivePrepTime());
    }

    @Override
    public Iterable<? extends RecipePageViewIngredientsField> getIngredients() {
        return createViews(RecipePageViewIngredientsField.class, model.getRecipeIngredients());
    }

    @Override
    public CharSequence getPrepTime() {
        return formatDuration(model.getPrepTime());
    }

    @Override
    public Iterable<? extends RecipePageViewSimilarRecipesField> getSimilarRecipes() {
        // TODO: we will add this in lesson 5
        return null;
    }

    @Override
    public Iterable<? extends RecipePageViewStepsField> getSteps() {
        return createViews(RecipePageViewStepsField.class, model.getSteps());
    }

    @Override
    public CharSequence getTotalTime() {
        return formatDuration(model.getTotalTime());
    }

    // --- PageView support ---

    @Override
    public Iterable<? extends PageViewMainField> getMain() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewPageHeadingField> getPageHeading() {
        return RichTextUtils.buildInlineHtml(
            model,
            Recipe::getTitle,
            e -> createView(PageViewPageHeadingField.class, e));
    }

    @Override
    public Iterable<? extends PageViewPageLeadField> getPageLead() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        return null;
    }

    // --- Utility ---

    private String formatDuration(Integer minutes) {
        if (minutes == null || minutes <= 0) {
            return null;
        }

        return AmountFormats.wordBased(Duration.of(minutes, ChronoUnit.MINUTES), locale);
    }
}
