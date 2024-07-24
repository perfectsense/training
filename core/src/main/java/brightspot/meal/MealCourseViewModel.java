package brightspot.meal;

import brightspot.util.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.meal.MealCourseView;
import com.psddev.styleguide.meal.MealCourseViewNameField;
import com.psddev.styleguide.meal.MealCourseViewRecipesField;
import com.psddev.styleguide.meal.MealCourseViewSummaryField;

public class MealCourseViewModel extends ViewModel<MealCourse> implements MealCourseView {

    @Override
    public Iterable<? extends MealCourseViewRecipesField> getRecipes() {
        return createViews(MealCourseViewRecipesField.class, model.getRecipes());
    }

    @Override
    public Iterable<? extends MealCourseViewNameField> getName() {
        return RichTextUtils.buildInlineHtml(
            model,
            MealCourse::getName,
            e -> createView(MealCourseViewNameField.class, e));
    }

    @Override
    public Iterable<? extends MealCourseViewSummaryField> getSummary() {
        return RichTextUtils.buildHtml(
            model,
            MealCourse::getSummary,
            e -> createView(MealCourseViewSummaryField.class, e));
    }
}
