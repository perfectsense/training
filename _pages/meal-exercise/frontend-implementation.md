---
title: Frontend Implementation
parent: Meal Exercise
nav_order: 4
permalink: /meal-exercise/frontend-implementation.html
---

# Frontend Implementation

## Setup

Let’s start with building out the Front End to display the data that we created and added via the CMS and our Back End fields. We will work from our Style Guide which will handle the majority of our Front End work.

Inside the bundle directory (`frontend/bundles/bundle-default/styleguide`) we will create a folder named `meal` which will contain the following files:
- MealPage.hbs
- MealPage.json
- MealPage.properties
- MealCourse.hbs
- MealCourse.json
- MealCourse.properties

And then inside the `styles/default` folder, we create a folder named `meal` with less files:
- All.less
- MealCourse.less
- MealPage.less


## MealPage.json

Complete example: <https://github.com/perfectsense/training/blob/exercise/meal/frontend/bundles/bundle-default/styleguide/meal/MealPage.json>

This simulates the data from the CMS of what a Meal Page would look like. We use the text generator helpers for the text and include a generic Image.json, which creates a auto generated image for us. This data is created by looking at the root styleguide and making examples of the type of content that is in the View. For each item in the root styleguide with a `_group`, you can look into that group and pull an example out. For Leads, we use the Image and for RichTextElements, we just drop in some words. For PageList, we make a generic List with 3 items. 


## MealPage.hbs

Complete example: <https://github.com/perfectsense/training/blob/exercise/meal/frontend/bundles/bundle-default/styleguide/meal/MealPage.hbs>

This is where the HTML for the top level Meal Page will live. We reuse a lot of elements from Page, such as the header, footer, one column layout, the Page-headline, etc which are pre-styled for us as those are shared things across the whole site. Since Page only has a headline and subheadline and no description, we make a Meal Page specific description as it needs to be visually smaller than Page-subHeadline. Alternatively, we could use the Page-subHeadline HTML here and just override the CSS. Neither approach is wrong. 

Other things to note are the set imageSize which pulls the correct image from DIMS and the including of ListD for similarMeals. The include will take care of that section of the page for us, as we just reuse ListD rendering and styles there.


## MealCourse.json

Complete example: <https://github.com/perfectsense/training/blob/exercise/meal/frontend/bundles/bundle-default/styleguide/meal/MealCourse.json>

This simulates the data from the CMS of what a Meal Course will look like when it’s published. Same as MealPage, you can look at the root styleguide and see what data types the BE will be providing via the root styleguide view and make examples of that, so you can write your handlebars code locally without needing the CMS.


## MealCourse.hbs

Complete example: <https://github.com/perfectsense/training/blob/exercise/meal/frontend/bundles/bundle-default/styleguide/meal/MealCourse.hbs>

This is where the HTML that rendered a Meal Course lives. Each course has an array of dishes which are a Recipe Module. We will pull the title directly from that child element to create an accordion of Recipes for each course, but then delegate the rest of the rendering of the Recipe Module as it does by default. We null out the title though, as we display it above. We are using an existing `bsp-toggler` utility to help us with the accordion toggling. Since this is such a simple accordion, we can use this generic toggler JS custom element instead of having to write our own here.


## MealCourse.properties and MealPage.properties

Complete examples: <https://github.com/perfectsense/training/blob/exercise/meal/frontend/bundles/bundle-default/styleguide/meal/MealCourse.properties> and <https://github.com/perfectsense/training/blob/exercise/meal/frontend/bundles/bundle-default/styleguide/meal/MealPage.properties>

These properties files allows us to localize some of the section headings instead of needing create editorial fields for these. Note the use of these in the above handlebar files where we “hardcode” some text fields such as the words “Courses” and “Dishes”. If we needed to support different languages, we would make localized versions of these properties files with all the translations.


## All.less

Complete example: <https://github.com/perfectsense/training/blob/exercise/meal/frontend/bundles/bundle-default/styleguide/styles/default/meal/All.less>

We create these files as aggregation files so it’s easier to layer the includes vs having one GIANT include file. So this file just includes the Meal less files and then we include it later in the global `All.less`.


## MealPage.less

Complete example: <https://github.com/perfectsense/training/blob/exercise/meal/frontend/bundles/bundle-default/styleguide/styles/default/meal/MealPage.less>

Styling for our main MealPage. Content width comes from the data attributes of `data-modulewell` and `data-module` that are in the HTML. Those are concepts that exist in Go that allow all pages and modules to be the same widths and provide vertical spacing between modules. Some of the styles, such as Page-headline come from Page.less since we are just reusing those elements. Here we style things such as the individual area headings, the description and the lead. Note in the lead, instead of using the `data-modulewell` and `data-module` attributes, which not only provide width but vertical spacing, we use a mixin to provide the width. We don’t want extra vertical spacing that comes from using the module concepts here. Also note the media query we use. We use LESS variables for media queries.


## MealCourse.less

Complete example: <https://github.com/perfectsense/training/blob/exercise/meal/frontend/bundles/bundle-default/styleguide/styles/default/meal/MealCourse.less>

Styling for the MealCourse. Note the use of CSS vars for the border colors. We want to use such concepts to keep colors consistent. We also style the accordion that expands recipes here. The JS that we used in the handlebars sets and resets data attributes, so we style those here to show and hide each recipe, as well as flipping the chevron up and down. Writing CSS based on data attributes is how we want to style our JS written code rather than writing styles directly in JS. We get the styling of each Recipe from the RecipeModule itself as we reuse that element, but we do a slight override to get rid of it’s default border, as that would be too many borders. If there was a LOT of a difference in the rendering of this RecipeModule vs the original element, we would create another variation of RecipeModule to use in the MealCourse, but with this example, we do not need to go that far, as there are only 2 CSS overrides. We wouldn’t want like 40 here though.


## Root All.less

Complete example: <https://github.com/perfectsense/training/blob/exercise/meal/frontend/bundles/bundle-default/styleguide/styles/default/All.less#L77>

We also need to add an association for the styles we set into the bundle’s parent Less file. **NOTE THE DIFFERENCE IN LOCATION OF THIS FILE**. This adds all the styles we just created to the main CSS for the website. Don’t confuse this with the `All.less` file in the meal directory we created earlier: that one includes all the specific meal less files; this one includes all the `All.less` files across all features.


## Styleguide navigation

Complete example: <https://github.com/perfectsense/training/blob/exercise/meal/frontend/bundles/bundle-default/styleguide/_navigation.config.json>

Lastly in order to see it when we run our Style Guide locally we need to add it to our `_navigation.config.json` file. Now if we start our theme locally by running yarn server:styleguide we should see a new page on the left side labeled Meal that will show the page and modules we just created.

Building and redeploying our gradle build on our local docker server will allow us to use the CMS, put in Meal, Course, and Recipe data and see the results on the real FE.
