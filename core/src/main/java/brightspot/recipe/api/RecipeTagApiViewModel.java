package brightspot.recipe.api;

import brightspot.recipe.RecipeTag;
import brightspot.util.RichTextUtils;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewModel;

@ViewInterface("RecipeTag")
public class RecipeTagApiViewModel extends ViewModel<RecipeTag> {

    public String getName() {
        return RichTextUtils.richTextToPlainText(model.getName());
    }
}
