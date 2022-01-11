package brightspot.meal;

import java.util.List;
import java.util.Locale;

import brightspot.l10n.CurrentLocale;
import brightspot.module.list.page.PageListModule;
import brightspot.module.list.page.SimplePageItemStream;
import brightspot.page.AbstractContentPageViewModel;
import brightspot.promo.page.PagePromotable;
import brightspot.recipe.HasRecipesData;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Localization;
import com.psddev.cms.view.PageEntryView;
import com.psddev.dari.db.Query;
import com.psddev.styleguide.meal.MealPageView;
import com.psddev.styleguide.meal.MealPageViewCoursesField;
import com.psddev.styleguide.meal.MealPageViewLeadField;
import com.psddev.styleguide.meal.MealPageViewMealDescriptionField;
import com.psddev.styleguide.meal.MealPageViewMealTitleField;
import com.psddev.styleguide.meal.MealPageViewSimilarMealsField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;

public class MealPageViewModel extends AbstractContentPageViewModel<Meal> implements
    MealPageView,
    PageEntryView {

    @CurrentLocale
    private Locale locale;

    // --- MealPageView support ---

    @Override
    public Iterable<? extends MealPageViewCoursesField> getCourses() {
        return createViews(MealPageViewCoursesField.class, model.getCourses());
    }

    @Override
    public Iterable<? extends MealPageViewLeadField> getLead() {
        return createViews(MealPageViewLeadField.class, model.getLead());
    }

    @Override
    public Iterable<? extends MealPageViewMealDescriptionField> getMealDescription() {
        return RichTextUtils.buildHtml(
            model,
            Meal::getDescription,
            e -> createView(MealPageViewMealDescriptionField.class, e));
    }

    @Override
    public Iterable<? extends MealPageViewMealTitleField> getMealTitle() {
        return RichTextUtils.buildInlineHtml(
            model,
            Meal::getTitle,
            e -> createView(MealPageViewMealTitleField.class, e));
    }

    @Override
    public Iterable<? extends MealPageViewSimilarMealsField> getSimilarMeals() {
        List<PagePromotable> similarMeals = Query.from(PagePromotable.class)
            .where("_type = ?", Meal.class)
            .and(HasRecipesData.class.getName() + "/" + HasRecipesData.RECIPES_FIELD + " = ?", model.getRecipes())
            .and("_id != ?", model)
            .sortDescending(Content.PUBLISH_DATE_FIELD)
            .select(0, 10)
            .getItems();

        if (similarMeals.isEmpty()) {
            return null;
        }

        SimplePageItemStream itemStream = new SimplePageItemStream();
        itemStream.setItems(similarMeals);

        PageListModule listModule = new PageListModule();
        listModule.setTitle(Localization.text(locale, MealPageViewModel.class, "similarMealsTitle"));
        listModule.setItemStream(itemStream);

        return createViews(MealPageViewSimilarMealsField.class, listModule);
    }

    // --- PageView support ---

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        return null;
    }
}
