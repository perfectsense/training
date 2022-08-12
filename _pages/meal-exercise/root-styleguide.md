---
title: Root Styleguide
parent: Meal Exercise
nav_order: 2
permalink: /meal-exercise/root-styleguide.html
---

# Root Styleguide Implementation

It’s unusual to get requirements specifically for the root styleguide. Instead, you need to infer them from the backend and frontend requirements.

We’ll need two new views: MealPage and MealCourse. Create a new directory `meal` in the root styleguide (under `frontend/styleguide/`) to hold the json and template files.


## MealPage

**Note: make sure you’re starting off of the correct branch. You need to be on the `exercise/recipe` branch.**

Create two files in the meal directory you just created: `MealPage.hbs` and `MealPage.json`. Leave the .hbs file empty: it only needs to exist for the build to work, and is otherwise ignored (the actual template will live in the frontend bundle which we’ll get to later on). The .json file will specify the fields and their types that will make up the MealPage view.
 
Since this will be a page, the view should extend ContentPage or Page. The requirements mention Section and Tags so it probably makes more sense to extend ContentPage, which adds Breadcrumbs (related to Sections) and Tags to Page. In the root styleguide, inheritance relationships are signified by the `_include` key, e.g. `"_include": "/page/ContentPage.json",`. This will result in a view interface which extends ContentPageView.

The `_template` key links this file to the view it specifies. The name of the hbs file will be used for the Java view interface name; in this case it will be MealPageView since we’re using MealPage.hbs.

The remaining keys each specify a single field in the view. Each needs its own `_include` or `_template` key. `_include` is used when multiple types are needed: e.g. for lead, we need to support image and video leads; the Leads.json file in the `_group` directory lists both of those, so they will both be supported in the lead field. `_template` is used when only one type is required.

```json
{
 "_template": "MealPage.hbs",
 "_include": "/page/ContentPage.json",
 
 "lead": {
   "_include": "/_group/Leads.json",
   "_key": 1
 },
 
 "mealTitle": {
   "_include": "/_group/RichTextElementsTiny.json"
 },
 
 "mealDescription": {
   "_include": "/_group/RichTextElementsMedium.json"
 },
 
 "courses": {
   "_template": "MealCourse.hbs"
},
 
 "similarMeals": {
   "_template": "/page/list/PageList.hbs"
 }
}
```


## MealCourse

Create two more files in the `frontend/styleguide/meal` directory: `MealCourse.hbs` and `MealCourse.json`. As with MealPage, leave the .hbs file empty. MealCourse is a much simpler view than MealPage was; there is no need to extend another view. The fields in the view are a direct analog of the fields in the model.

```json
{
 "_template": "MealCourse.hbs",
 
 "name": {
   "_include": "/_group/RichTextElementsTiny.json"
 },
 
 "description": {
   "_include": "/_group/RichTextElementsSmall.json"
 },
 
 "dishes": {
   "_template": "/recipe/RecipeModule.hbs"
 }
}
```
