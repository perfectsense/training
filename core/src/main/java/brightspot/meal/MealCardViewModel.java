package brightspot.meal;

import brightspot.permalink.Permalink;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.meal.MealCardView;
import com.psddev.styleguide.meal.MealCardViewMediaField;
import com.psddev.styleguide.meal.MealCardViewTitleField;

public class MealCardViewModel extends ViewModel<Meal> implements MealCardView {

    @CurrentSite
    private Site site;

    @Override
    public Iterable<? extends MealCardViewMediaField> getMedia() {
        return createViews(MealCardViewMediaField.class, model.getImage());
    }

    @Override
    public Iterable<? extends MealCardViewTitleField> getTitle() {
        return RichTextUtils.buildInlineHtml(
            model,
            Meal::getTitle,
            e -> createView(MealCardViewTitleField.class, e));
    }

    @Override
    public CharSequence getUrl() {
        return Permalink.getPermalink(site, model);
    }
}
