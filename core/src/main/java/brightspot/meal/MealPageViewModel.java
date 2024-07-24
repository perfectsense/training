package brightspot.meal;

import brightspot.page.AbstractPageViewModel;
import brightspot.util.RichTextUtils;
import com.psddev.cms.view.PageEntryView;
import com.psddev.styleguide.meal.MealPageView;
import com.psddev.styleguide.meal.MealPageViewCoursesField;
import com.psddev.styleguide.meal.MealPageViewDescriptionField;
import com.psddev.styleguide.meal.MealPageViewImageField;
import com.psddev.styleguide.meal.MealPageViewSimilarMealsField;
import com.psddev.styleguide.page.PageViewMainField;
import com.psddev.styleguide.page.PageViewPageHeadingField;
import com.psddev.styleguide.page.PageViewPageLeadField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;

public class MealPageViewModel extends AbstractPageViewModel<Meal> implements
    MealPageView,
    PageEntryView {

    @Override
    public Iterable<? extends MealPageViewCoursesField> getCourses() {
        return createViews(MealPageViewCoursesField.class, model.getCourses());
    }

    @Override
    public Iterable<? extends MealPageViewDescriptionField> getDescription() {
        return RichTextUtils.buildHtml(
            model,
            Meal::getDescription,
            e -> createView(MealPageViewDescriptionField.class, e));
    }

    @Override
    public CharSequence getDifficulty() {
        // TODO: we will add this in lesson 5
        return null;
    }

    @Override
    public Iterable<? extends MealPageViewImageField> getImage() {
        return createViews(MealPageViewImageField.class, model.getImage());
    }

    @Override
    public Iterable<? extends MealPageViewSimilarMealsField> getSimilarMeals() {
        // TODO: we will add this in lesson 5
        return null;
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
            Meal::getTitle,
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
}
