---
title: Meal Model
grand_parent: Meal Exercise
parent: Backend Implementation
nav_order: 1
permalink: /meal-exercise/meal-model.html
---

# Meal Model

Complete example: <https://github.com/perfectsense/training/blob/exercise/meal/core/src/main/java/brightspot/meal/Meal.java>

Create a file `Meal.java` in the `core/src/main/java/brightspot/meal` directory. We will go line by line through the file and explain the purpose of everything.

```java
public class Meal extends Content
```
Extend Content as Meal is a content type that editors will create and interact with directly.

```java
implements
   CascadingPageElements,
   DefaultSiteMapItem,
   HasRecipes,
   HasSectionWithField,
   HasTagsWithField,
   HasUrlSlugWithField,
   Page,
   PagePromotableWithOverrides,
   SeoWithFields,
   Shareable {
```
There are several interfaces that are typically added to page content types. Since Meal is a page it should have them as well:
- CascadingPageElements
    - This adds the ability to override things like the header and footer on a per-page basis. It adds several fields which are all located on the Overrides tab
- DefaultSiteMapItem
    - This enables Meals to show up in the sitemap. It adds a few fields on the Overrides tab for configuring how it is handled
- HasRecipes
    - This will allow searching Meals by Recipe
- HasSectionWithField
    - This adds a Section field and index
- HasTagsWithFields
    - This adds a Tags field and index
- HasUrlSlugWithField
    - The URL slug is used to build the permalink for a piece of content. Typically it has a fallback value and then allows the editor to override when needed. The slug should be url-safe, so usually you pass the value to Utils.toNormalized to strip out spaces, etc.
- Page
    - This marks a content type as being page-level. It extends three other interfaces which provide functionality that pretty much all pages need:
        - Directory.Item
            - This allows auto-creation of permalinks
        - Linkable
            - This allows specifying the link url, which is almost always the permalink
        - Seo
            - This specifies meta values like title and description which are used for SEO optimization
- PagePromotableWithOverrides
    - PagePromotable is an important concept in Brightspot. It provides values (title, description, image, etc) which are used e.g. in a small card widget on the frontend to promote a piece of content. These values are typically shortened or altered to increase click through rates. PagePromotableWithOverrides extends PagePromotable and adds fields where editors can configure the promotable values
- SeoWithFields
    - Like PagePromotableWithOverrides, SeoWithFields extends the Seo interface (the same one used in Page above) and adds fields for configuring the Seo values
- Shareable
    - Shareable provides values for use with external social sharing like Facebook or Twitter. It has fields for configuring the values and also the ability to provide fallbacks

```java
@Indexed
@Required
@ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
private String title;
```
`@Indexed` tells Dari that the value of the title field should be indexed so it can be queried against.
`@Required` is a guideline for editors that they need to provide a value for the title field. If they don’t, when they try to save they will get a validation error. Even when a field is required, you should still perform null checks as being required is not a hard guarantee.
`@ToolUi.RichText` tells Brightspot that the string value of this field is rich text, which is essentially a custom XML format. You should always specify a toolbar for this annotation; there are four built-in options: Tiny, Small, Medium, and Large, and you can also make a custom one if needed. There is also an inline argument which defaults to true. When true, this field does not allow any block-level RichTextElements (like images or quotes). Since we don’t want any of that for a title, we leave it as the default value (i.e. `inline = true`).

```java
@DynamicPlaceholderMethod("getInternalNameFallback")
private String internalName;
```
Internal name is a common field to have. It allows configuring a name which makes it easier to find content in the CMS but isn’t intended for external use. Usually there is a default value so they don’t need to set it every time. `@DynamicPlaceholderMethod` tells Brightspot what method to call to get the placeholder to use; usually the placeholder shows the default value for the field. Later on we’ll use the internalName to make the CMS label which is the name for this piece of content in the CMS, e.g. in search results.

```java
private ArticleLead lead;
```
There are two lead interfaces in Brightspot: ArticleLead and ModulePageLead. ArticleLead allows image and video leads which are specified in the requirements.

```java
@ToolUi.RichText(inline = false, toolbar = MediumRichTextToolbar.class)
private String description;
```
Here we set inline to false as we want to allow more elements for the description. The Medium toolbar adds tables, among a few other things.

```java
@CollectionMinimum(1)
private List<MealCourse> courses;
```
We need to allow multiple MealCourses to be programmed, so we need a collection field. Brightspot supports both List and Set fields; since order is important we use a List. We require at least one course so we use `@CollectionMinimum` to specify that. MealCourse is a new content type which we haven’t created yet but will get to next.

```java
// --- Getters/setters ---
 
public String getTitle() {
   return title;
}
 
public void setTitle(String title) {
   this.title = title;
}
 
public String getInternalName() {
   return internalName;
}
 
public void setInternalName(String internalName) {
   this.internalName = internalName;
}
 
public ArticleLead getLead() {
   return lead;
}
 
public void setLead(ArticleLead lead) {
   this.lead = lead;
}
 
public String getDescription() {
   return description;
}
 
public void setDescription(String description) {
   this.description = description;
}
 
public List<MealCourse> getCourses() {
   if (courses == null) {
       courses = new ArrayList<>();
   }
   return courses;
}
 
public void setCourses(List<MealCourse> courses) {
   this.courses = courses;
}
```
Your IDE should be able to automatically generate getters and setters so you shouldn’t need to type them out manually ever. For List/Map/Set fields, never return null from the getter; instead return a linked empty value. If the auto-generated getters don’t do this, update the template in your IDE so you don’t have to think about it.

```java
// --- Fallbacks ---
 
private String getInternalNameFallback() {
   return RichTextUtils.richTextToPlainText(getTitle());
}
```
Internal name needs to be plain text, but we want it to default to the value of the title which is rich text. This method will convert rich text to plain. We’ll use this method both for the internalName placeholder as well as the label.

```java
// --- Directory.Item support ---
 
@Override
public String createPermalink(Site site) {
   return AbstractPermalinkRule.create(site, this, SectionPrefixPermalinkRule.class);
}
```
This method specifies the default permalink (editors can override if they want). AbstractPermalinkRule allows configuring the permalink generation logic in the CMS as well as provides default logic to use. Permalinks can depend on the site, which will be the site the editor is currently in while they’re creating the Meal.

```java
// --- HasRecipes support ---
 
@Override
public List<Recipe> getRecipes() {
   return getCourses()
       .stream()
       .flatMap(c -> c.getDishes().stream())
       .map(RecipeModulePlacementInline::getRecipe)
       .filter(Objects::nonNull)
       .collect(Collectors.toList());
}
```
We want to include all recipes from all meal courses in the index. Using a stream+flatMap is one approach you can use. The recipe value could be null (even though it’s required) so we need a null check in the stream.

```java
// --- HasUrlSlugWithField support ---
 
@Override
public String getUrlSlugFallback() {
   return Utils.toNormalized(RichTextUtils.richTextToPlainText(getTitle()));
}
```
URL slug is plain text, so we need to convert the rich text title. We also need to normalize it for use in a URL.

```java
// --- Linkable support ---
 
@Override
public String getLinkableText() {
   return getPagePromotableTitle();
}
```
Linkable provides two values: text and url. getLinkableUrl has a default implementation (the permalink) but we need to provide a value for getLinkableText. 

```java
// --- PagePromotableWithOverrides support ---
 
@Override
public String getPagePromotableTitleFallback() {
   return getTitle();
}
 
@Override
public String getPagePromotableDescriptionFallback() {
   return getDescription();
}
 
@Override
public WebImageAsset getPagePromotableImageFallback() {
   return Optional.ofNullable(getLead())
       .map(ArticleLead::getArticleLeadImage)
       .orElse(null);
}
```
These PagePromotable fallback methods have default implementations so be sure you remember to override them when necessary. No need to convert anything to plain text as title and description are rich text.

```java
// --- Recordable support ---
 
@Override
public String getLabel() {
   return MoreStringUtils.firstNonBlank(getInternalName(), this::getInternalNameFallback);
}
```
Whenever you have an internalName field you should always override getLabel and use its value. If you don’t override getLabel, Brightspot will choose a default field to use for the label, which may or not be very useful.

```java
// --- SeoWithFields support ---
 
@Override
public String getSeoTitleFallback() {
   return RichTextUtils.richTextToPlainText(getPagePromotableTitle());
}
 
@Override
public String getSeoDescriptionFallback() {
   return RichTextUtils.richTextToPlainText(getPagePromotableDescription());
}
```
Seo values are plain text so don’t forget to convert rich text values.

```java
// --- Shareable support ---
 
@Override
public String getShareableTitleFallback() {
   return RichTextUtils.richTextToPlainText(getPagePromotableTitleFallback());
}
 
@Override
public String getShareableDescriptionFallback() {
   return RichTextUtils.richTextToPlainText(getPagePromotableDescriptionFallback());
}
 
@Override
public WebImageAsset getShareableImageFallback() {
   return getPagePromotableImageFallback();
}
```
Just like with Seo values, Shareable values are plain text. For some reason it’s typical to use the PagePromotable fallback values here (e.g. `getPagePromotableTitleFallback()` rather than `getPagePromotableTitle()`).

