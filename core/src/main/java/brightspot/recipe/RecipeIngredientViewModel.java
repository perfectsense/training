package brightspot.recipe;

import brightspot.util.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.recipe.RecipeIngredientView;
import com.psddev.styleguide.recipe.RecipeIngredientViewIngredientField;

public class RecipeIngredientViewModel extends ViewModel<RecipeIngredient> implements RecipeIngredientView {

    @Override
    public Iterable<? extends RecipeIngredientViewIngredientField> getIngredient() {
        return RichTextUtils.buildInlineHtml(
            model,
            RecipeIngredient::toDescription,
            e -> createView(RecipeIngredientViewIngredientField.class, e));
    }
}
