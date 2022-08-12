---
title: Overview and Requirements
parent: Meal Exercise
nav_order: 1
permalink: /meal-exercise/overview.html
---

# Overview and Requirements

You have been tasked with building a new feature "Meal" which will enable combining several recipes together. This exercise will cover all the  backend and frontend work needed to implement this feature.

## Backend Requirements

These are analogous to what you would receive in e.g. a Jira ticket.

- As an editor (i.e. a CMS user) I can group recipes into meals
- Meals have one or more courses
- Courses have one or more recipes
    - Allow overriding recipe image
- Meals are standalone pages
- Meals can be searched by Recipe
- Meal fields:
    - Title
    - Internal Name (defaults to Title)
    - Lead (image or video)
    - Description (rich text allowing standard rich text elements)
    - Courses
    - Section
    - Tags
    - URL Slug
        - Uses the Title for the fallback
- Meal also needs standard overrides:
    - SEO
        - Fallback to Promotable fields
    - Above/Aside/Below/etc.
    - Promo
        - Promo Title: fallback to Title
        - Promo Description: fallback to Description
        - Promo Image: fallback to Lead image
    - Share
        - Fallback to Promotable fields
- When rendered on the frontend, include a list of related Meals
    - A Meal is related if it shares at least one Recipe with the Meal being rendered
    - Related Meals should be ordered by publication date, most recent first


## Frontend Requirements

Render a Meal Page with a title, description, image, along with a list of course and dishes, which contain an accordion of recipes, so users can see each recipe for each dish independently

You can use [this screenshot]({{site.baseurl}}/assets/images/meal-exercise-test-content/meal-full-screenshot.png) as your design.


## Interpreting the Backend Requirements

Typically requirements are written from a non-technical perspective so you ofen need to take some time to translate them into hard technical requirements. Here is an example of what those might look like; note that this process is at least a little subjective, and often requires working with your product manager to ensure you're building what the customer wants.

### As an editor (i.e. a CMS user) I can group recipes into meals
Meal is a new content type. Since it is meant to be created directly by editors it should extend Content.

### Meals have one or more courses
Meal will have a List field for courses

### Courses have one or more recipes; Allow overriding recipe image
The Courses field cannot be a direct reference to a Recipe as the editor needs to be able to override the image for each Recipe. Instead, we need a new content type MealCourse which will be embedded within the Meal content type. It will have a reference to a Recipe as well as an Image field for overriding the recipe’s image.

### Meals are standalone pages
There are several interfaces that should usually be added to Page content types. For now add DefaultSiteMapItem and Page. Although Page itself doesn’t have any methods, it extends a few other interfaces which do have methods you’ll need to implement.

### Meals can be searched by Recipe
Implement HasRecipes to provide this functionality. In the public List<Recipe> getRecipes() method you’ll need to collect all the recipes from all the courses into a single list (a Stream with flatMap is a good technique to use for this).

### Meal fields
As a general rule, any text field which will be visible on the frontend should be rich text. In the absence of any more specific requirement, use the TinyRichTextToolbar for these fields. Title should use the tiny toolbar while Description should use the Medium one and should be inline = false to allow multiple paragraphs and block-level rich text elements like images.

Internal Name is not for use on the frontend so it should be plain text. Add a placeholder annotation to display the Title if it is not set (see the Recipe exercise for an example of how to do this). You’ll need a method like getInternalNameFallback to convert the rich text Title into plain text.

Lead should use the ArticleLead content type.

Courses will be a List<MealCourse> field.

No need to add a Section field; instead implement HasSectionWithField which provides the Section field through a Modification.

No need to add a Tags field; instead implement HasTagsWithField which provides the Tags field through a Modification.

No need to add a URL Slug field; instead implement HasUrlSlugWithField which provides the URL Slug field through a Modification. For the fallback, convert the Title using Utils.toNormalized; also be sure to convert the Title to plain text first.
 
### Meal Overrides
SEO overrides come from SeoWithFields. You’ll need to implement the getSeoTitleFallback and getSeoDescriptionFallback methods. SEO values need to be plain text so you’ll need to convert the rich text PagePromotable values to plain text in those methods.

Above/Beside/Below/etc. come from the CascadingPageElements interface.

Promo is referring to PagePromotable. Implement PagePromotableWithOverrides to get the override fields. Fallback methods have default implementations on that interface but be sure you override them. They are rich text values and so are the fallbacks so no need to convert to plain text.

Share overrides come from Shareable. As with PagePromotable the fallback methods have default implementations that you will need to override. Additionally, share values are plain text so you’ll need to convert the rich text PagePromotable values to plain text.

### Related Meals

Since there is no requirement for editorially configuring the list of related meals, we can implement this in the viewmodel.


