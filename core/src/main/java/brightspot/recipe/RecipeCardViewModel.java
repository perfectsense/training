package brightspot.recipe;

import brightspot.permalink.Permalink;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.recipe.RecipeCardView;
import com.psddev.styleguide.recipe.RecipeCardViewMediaField;
import com.psddev.styleguide.recipe.RecipeCardViewTitleField;

public class RecipeCardViewModel extends ViewModel<Recipe> implements RecipeCardView {

    @CurrentSite
    private Site site;

    @Override
    public Iterable<? extends RecipeCardViewMediaField> getMedia() {
        return createViews(RecipeCardViewMediaField.class, model.getImage());
    }

    @Override
    public Iterable<? extends RecipeCardViewTitleField> getTitle() {
        return RichTextUtils.buildInlineHtml(
            model,
            Recipe::getTitle,
            e -> createView(RecipeCardViewTitleField.class, e));
    }

    @Override
    public CharSequence getUrl() {
        return Permalink.getPermalink(site, model);
    }
}
