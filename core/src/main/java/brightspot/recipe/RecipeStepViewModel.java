package brightspot.recipe;

import brightspot.util.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.recipe.RecipeStepView;
import com.psddev.styleguide.recipe.RecipeStepViewDirectionsField;
import com.psddev.styleguide.recipe.RecipeStepViewHeadingField;
import com.psddev.styleguide.recipe.RecipeStepViewImageField;

public class RecipeStepViewModel extends ViewModel<RecipeStep> implements RecipeStepView {

    @Override
    public Iterable<? extends RecipeStepViewDirectionsField> getDirections() {
        return RichTextUtils.buildHtml(
            model,
            RecipeStep::getDirections,
            e -> createView(RecipeStepViewDirectionsField.class, e));
    }

    @Override
    public Iterable<? extends RecipeStepViewHeadingField> getHeading() {
        return RichTextUtils.buildInlineHtml(
            model,
            RecipeStep::getHeading,
            e -> createView(RecipeStepViewHeadingField.class, e));
    }

    @Override
    public Iterable<? extends RecipeStepViewImageField> getImage() {
        return createViews(RecipeStepViewImageField.class, model.getImage());
    }
}
