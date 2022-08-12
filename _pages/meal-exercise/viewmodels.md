---
title: ViewModels
grand_parent: Meal Exercise
parent: Backend Implementation
nav_order: 3
permalink: /meal-exercise/viewmodels.html
---

# ViewModels

ViewModels depend on view interfaces, so make sure you’ve already done the root styleguide work before continuing. Additionally, you’ll need to run the Gradle build for the frontend project: `./gradlew -p frontend` (or run a full Gradle build).


## MealPageViewModel

Complete example: <https://github.com/perfectsense/training/blob/exercise/meal/core/src/main/java/brightspot/meal/MealPageViewModel.java>

Create a file `MealPageViewModel.java` in the `core/src/main/java/brightspot/meal` directory. Again, we will go line by line through the file and explain the purpose of everything.

```java
public class MealPageViewModel extends AbstractContentPageViewModel<Meal>
```
When we made MealPage.json in the root styleguide we included ContentPage.json (`"_include": "/page/ContentPage.json",`); there is a corresponding abstract viewmodel class which supplies the fields of ContentPageView so we can extend it here to reduce code duplication. AbstractContentPageViewModel itself extends AbstractPageViewModel which is similar to it, but tied to PageView. All Page viewmodels should extend AbstractPageViewModel, either directly or through another viewmodel.

```java
implements
   MealPageView,
   PageEntryView {
```
MealPageView is the view interface generated from the MealPage.json/MealPage.hbs pair of files. Additionally, since Meal is a standalone page we also need to implement PageEntryView which is a marker interface that tells Brightspot we can use this viewmodel as the entrypoint for the view system when responding to a request. Without it, we’d get an error as the view system wouldn’t be able to find the viewmodel to use.

```java
@CurrentLocale
private Locale locale;
```
`@CurrentLocale` is an example of a viewmodel field annotation (more here: <https://www.brightspot.com/documentation/brightspot-cms-developer-guide/latest/annotations#field-annotations>). When the viewmodel is created, all fields with one of these annotations are automatically populated from the incoming request. In this case, whatever Locale we’re using for the current request will be populated into the locale field so we can access it elsewhere in the viewmodel. Be advised that these fields could be null depending on the specifics of the request. Fields are guaranteed to be populated before any of your viewmodel code runs.

```java
@Override
public Iterable<? extends MealPageViewCoursesField> getCourses() {
   return createViews(MealPageViewCoursesField.class, model.getCourses());
}
 
@Override
public Iterable<? extends MealPageViewLeadField> getLead() {
   return createViews(MealPageViewLeadField.class, model.getLead());
}
```
These are two examples of delegation to another viewmodel for rendering a field. In getCourses, we tell the view system to render a view corresponding to MealPageViewCoursesField for each course. In getLead, we tell the view system to render a view corresponding to MealPageViewLeadField for each lead. There is only ever one lead but Brightspot always assumes each view is a collection; if you pass in a single object it will handle it as if you passed in a collection of one element.

```java
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
```
These are two examples of rendering rich text. The main difference between them is the first uses buildHtml which generates paragraph elements; the second uses buildInlineHtml which leaves the original <br> tags rather than converting them into paragraphs.

Both methods require passing in the Record containing the rich text value as well as a method expression to obtain the value from the Record. This allows propagation of the Record’s database and reference resolution settings (e.g. skipping cache) to any Records contained in the rich text value. The last value hooks rich text rendering back into the view system, telling how to render any rich text elements it finds.

```java
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
```
This is an example of logic in a viewmodel. We first query for related Meals using the index provided by HasRecipes/HasRecipesData. We need to explicitly exclude the Meal we’re rendering, otherwise it’ll almost certainly be included in the results. The requirements didn’t specify how many meals to display so we limit it to a reasonable value (10); this would be a good thing to get confirmation of from the product manager.

Since it’s possible that there are no related meals, we check for that and return null if so; this saves a bit of time and also prevents rendering an empty list on the frontend (although most list modules will not render if they don’t have any content).

The frontend doesn’t want to render the Meals as-is; instead it’s more convenient to have them wrapped up in a list module (their styling is probably applied to that template). We create a one-off list module and item stream to contain the Meals we found and then pass that into the view system using createViews. Note the Localization.text call: here is where we need the locale value from earlier in the viewmodel so we can get the appropriate localization of the title.

```java
// --- PageView support ---
 
@Override
public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
   return null;
}
```
This is a method from PageView which we don’t need so we just return null.


## Related Meals title localization

We need to include a resource bundle for localization of the title. Localization requires a context; in our case we passed in `MealPageViewModel.class` so our resource bundle needs to be at the same path that class lives under, but under the `core/src/main/resources/` directory instead of `core/src/main/java/`. Create a file `core/src/main/resources/brightspot/meal/MealPageViewModelDefault_en.properties` with the following content:
```java
similarMealsTitle=Similar Meals
```
The Default in the filename is part of how Brightspot handles overrides for localization. If there was an existing file that we didn’t control we could override its values by creating `MealPageViewModelOverride_en.properties`. Since we control this class entirely there is no need for overrides so we name it `MealPageViewModelDefault_en.properties`. The `en` at the end signifies the locale, in this case English.


## MealCourseViewModel

Complete example: <https://github.com/perfectsense/training/blob/exercise/meal/core/src/main/java/brightspot/meal/MealCourseViewModel.java>

Create a file `MealCourseViewModel.java` in the `core/src/main/java/brightspot/meal` directory.

```java
public class MealCourseViewModel extends ViewModel<MealCourse> implements MealCourseView
```
We didn’t include another view in MealCourse.json so we can extend ViewModel directly. We still implement MealCourseView though.

```java
@Override
public Iterable<? extends MealCourseViewDescriptionField> getDescription() {
   return RichTextUtils.buildInlineHtml(
       model,
       MealCourse::getSummary,
       e -> createView(MealCourseViewDescriptionField.class, e));
}
 
@Override
public Iterable<? extends MealCourseViewDishesField> getDishes() {
   return createViews(MealCourseViewDishesField.class, model.getDishes());
}
 
@Override
public Iterable<? extends MealCourseViewNameField> getName() {
   return RichTextUtils.buildInlineHtml(
       model,
       MealCourse::getName,
       e -> createView(MealCourseViewNameField.class, e));
}
```
Nothing to add here beyond what we already covered in MealPageViewModel. `getDishes()` will end up delegating to RecipeViewModel.
