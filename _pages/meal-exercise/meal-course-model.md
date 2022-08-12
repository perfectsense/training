---
title: Meal Course Model
grand_parent: Meal Exercise
parent: Backend Implementation
nav_order: 2
permalink: /meal-exercise/meal-course-model.html
---

# Meal Course Model

Complete example: <https://github.com/perfectsense/training/blob/exercise/meal/core/src/main/java/brightspot/meal/MealCourse.java>

Create a file `MealCourse.java` in the `core/src/main/java/brightspot/meal` directory. Again, we will go line by line through the file and explain the purpose of everything.

```java
@Recordable.DisplayName("Course")
@Recordable.Embedded
public class MealCourse extends Record
```
MealCourse is only needed in the context of a Meal, so there is no need for it to extend Content. Additionally, `@Recordable.Embedded` tells Brightspot that MealCourse objects will not be saved to the database independently; instead they live within another record that itself gets saved (a Meal in our case). MealCourse is a good name to use in code, but it’s redundant in the CMS so we use `@Recordable.DisplayName("Course")` to shorten it.

```java
@Required
@ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
private String name;
 
@ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
private String summary;
 
@CollectionMinimum(1)
private List<RecipeModulePlacementInline> dishes;
 
// --- Getters/setters ---
 
public String getName() {
   return name;
}
 
public void setName(String name) {
   this.name = name;
}
 
public String getSummary() {
   return summary;
}
 
public void setSummary(String summary) {
   this.summary = summary;
}
 
public List<RecipeModulePlacementInline> getDishes() {
   if (dishes == null) {
       dishes = new ArrayList<>();
   }
   return dishes;
}
 
public void setDishes(List<RecipeModulePlacementInline> dishes) {
   this.dishes = dishes;
}
```
Nothing new to add here: the same points apply from Meal re: rich text and collection fields. RecipeModulePlacementInline allows selection of a Recipe and overriding its image so it makes sense to use it here rather than coding up a new class to do the same thing.

```java
// --- Recordable support ---
 
@Override
public String getLabel() {
   return RichTextUtils.richTextToPlainText(getName());
}
```
We could probably get away without overriding this as Brightspot will almost certainly use the name for the label by default, but since it’s rich text it’s a good idea to convert it to plain text for use with the label.
