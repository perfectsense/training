# Common Theme Compatibility Test Failures

If you're running the Theme Compatibility Test for the first time, the number of errors may seem overwhelming. That's
normal. The entire theme is checked for compatibility even if errors are detected, and one incompatibility usually
produces multiple error messages. "Conflicting Parents" and "Cyclic Ancestry" are the exception to this rule -- they do
short circuit the rest of the compatibility test. So, resolve those first, then start at the top of this document (those
are the easiest) and work your way down.

Tip: When working through a long list of errors, quickly rerun _only_ ThemeCompatibilityTest like so:

```bash
./gradlew test --tests brightspot.frontend.ThemeCompatibilityTest
```

A couple of important definitions to understand the error messages and how to fix them:

##### View Key: The value of the `"_template"` or `"_view"` key in a JSON file _after_ being processed by the styleguide build.
If the template is a style of another template, the other template is the View Key. Example:

```json
{
  "_template": "/core/article/Article.hbs"
}
```

`core/article/Article.hbs` is the View Key.

**/core/promo/PromoA.json**
```json
{
  "_template": "/core/promo/PromoA.hbs"
}
```

**_config.json**
```json
{
  "styles": {
    "/core/promo/Promo.hbs": {
      "templates": [
        {
          "displayName": "Promo A",
          "example": "/core/promo/PromoA.json"
        }
      ]
    }
  }
}
```

`core/promo/Promo.hbs` is the View Key. All JSON files with the same View Key are combined into one JSON map, and the View
Key is the name of that map. The View Key and its JSON map define the contract with the root styleguide and backend.

##### Parent View Key: The View Key of the JSON file in the `"_include"` key of a JSON file.
Example:

**/core/article/Article.json**
```json
{
  "_include": "/core/page/CreativeWork.json",
  "_template": "Article.hbs"
}
```

**/core/page/CreativeWork.json**
```json
{
  "_template": "/core/page/CreativeWork.hbs"
}
```

`core/page/CreativeWork.hbs` is the Parent View Key of `core/article/Article.hbs`

### Theme [my-theme-name] is not compatible with the root styleguide!

This error indicates there are other errors that have been printed to stdout/err. Details on those errors can be found below.

### The contents of a _include must be a Map or List!

Error message:
```
    styleguide/blog/blogpost/BlogPostPage.json has 2 errors:
        The contents of a _include must be a Map or List!
        The contents of a _include must be a Map or List!
```

This error means the filename specified in the `"_include"` key does not exist. Usually the styleguide JS build will
catch this error, but there is one special case for the root styleguide that will cause virtually every _include to fail:
in versions of brightspot-gradle-plugins prior to 4.1.11, the value of "root.theme.buildDir" was detected incorrectly,
so none of the paths can be resolved properly.

To fix this error, either upgrade brightspot-gradle-plugin to 4.1.11 or newer _or_ add this line to your
site/build.gradle in the "test" section:

```groovy
test {
    systemProperties.put('theme.root.buildDir', "${buildDir}/../../frontend/build/styleguide")
}
```

### java.lang.OutOfMemoryError: GC overhead limit exceeded

Give gradle more memory. For example, in site/build.gradle:
```groovy
test {
    maxHeapSize = "4g"
}
```

### Must specify the view via the _view key or _template key

Error message:
```
    openair/_OpenAirModuleEndpoint.json has 1 error:
        Must specify the view via the _view key or _template key! at (line=1, col=2, offset=1)
```

In this case, `_OpenAirModuleEndpoint.json` is not meant to power a View Key at all - it's a mock API endpoint.

This can be handled in one of two ways depending on your preference:

1) Rename the JSON file so that it contains two `.` characters. For example `OpenAirModuleEndpoint.api.json`. CodeGen
ignores these files by default.

2) Move the JSON file to the `/styleguide/_resource` directory. This directory is ignored by default.

### Handlebars file does not exist in the root styleguide

Error message:
```
    [core/carousel/CarouselSlide.hbs] does not exist in the root styleguide!
      at
        core/carousel/Carousel.json at (line=5, col=6, offset=94)
        examples/Article-Gallery-Lead.json at (line=7, col=8, offset=154)
        core/carousel/CarouselSlide.json at (line=1, col=2, offset=1)
```

This can be addressed in multiple ways depending on the intent.

1) **CarouselSlide is intended to be a new View Key.** Add CarouselSlide.hbs and CarouselSlide.json to the root
styleguide. This will require new backend ViewModel development and may or may not affect the editorial experience in
the CMS.

2) **CarouselSlide is not intended to be a View Key; it is intended to be a _style_ of a View Key, GallerySlide.** Add
the appropriate JSON to `_config.json` to register CarouselSlide as a style of GallerySlide. This will affect the
editorial experience in the CMS, as editors will need to select the CarouselSlide style of GallerySlide.

3) **CarouselSlide is only intended to be used as an `{{include}}`.** Either delete `CarouselSlide.json` and all references to
it _or_ modify it so that it uses the correct View Key as its `"_template"`. For example:

Before:
```json
{
    "_template": "CarouselSlide.hbs",
    "title": "My Carousel"
}
```

After:
```json
{
    "_template": "GallerySlide.hbs",
    "_styledTemplate": "CarouselSlide.hbs",
    "title": "My Carousel"
}
```

Note that the value of `"_styledTemplate"` will not be evaluated in styleguide, so you can't just use `{{render slides}}`
to see CarouselSlide.hbs. This is consistent with the behavior of the theme system in Brightspot - you have to do the
`{{include}}` yourself. `"_styledTemplate"` is just a note to yourself and future developers to indicate the purpose of
the JSON file.

4) **Another variation of this error** is the case of a page that exists _only_ in styleguide and is not intended to be
rendered at all in Brightspot. For example, "TypographyPage.json" / "TypographyPage.hbs":

**/global/TypographyPage.json**:
```json
{
  "_include": "/core/page/Page.json",
  "_hidden": false,
  "_template": "/global/TypographyPage.hbs"
}
```

**_pages.config.json**:
```json
{
  "pages": [
    {
      "name": "Globals",
      "pages": [
        {
          "name": "Typography",
          "content": "/global/TypographyPage.html"
        }
      ]
    }
  ]
}
```

This is intended to be rendered in the theme styleguide with the understanding that it does not actually exist in the
root styleguide and will not be rendered by Brightspot. In this case, move the JSON file to the `styleguide/_resource`
directory. Make sure you also update any references to this file such as `_pages.config.json`.

### Handlebars file contains fields that do not exist in the root styleguide

Error message:
```
    [core/promo/Promo.hbs] contains field(s) that do not exist in the root styleguide:
      [brandColor] at:
        core/promo/PromoA.json at (line=9, col=11, offset=254)
```

**PromoA.json**:
```json
{
  "_template": "PromoA.hbs",
  "brandColor": "#dc143c"
}
```

This can be addressed in multiple ways depending on the intent.

1) **`brandColor` is not used in the template `PromoA.hbs`.** Delete it from all JSON locations listed.

2) **`brandColor` is intended to be a frontend field.** If `brandColor` is only used by a subset of the styles of
`Promo`, it may be appropriate to create it as a frontend field. Add the appropriate JSON to `_config.json` to register
it as a field of `PromoA.hbs`.

3) **`brandColor` is intended to be a backend field.** If `brandColor` is used by all or almost all styles of `Promo`,
it may be appropriate to create it as a backend field. Add it to `core/promo/Promo.json` in the root styleguide.

A different instance of this error message lists other seemingly unrelated JSON locations:
```
    [event/EventPage.hbs] contains field(s) that do not exist in the root styleguide:
      [todayDate] at:
        core/page/Page.json at (line=153, col=14, offset=3940)
      [eventTime] at:
        examples/EventPage.json at (line=30, col=14, offset=674)
      [quickLinks] at:
        core/page/Page.json at (line=30, col=15, offset=976)
      [firstScripts] at:
        core/page/Page.json at (line=8, col=17, offset=155)
```

In this case it's likely that the fields in `Page.json` are repeated many times for other View Keys, as `Page.json` is a
very commonly included file. Review the other decendants of `Page.hbs` to determine the appropriate action.

### Field accepts types that are incompatible with the root styleguide

Error message:
```
    [core/video/VideoPage.hbs], field [player] accepts types that are incompatible with the root styleguide:
      [brightcove/BrightcoveVideoPlayer.hbs] at:
        examples/VideoPage-Brightcove.json at (line=4, col=14, offset=104)
      Acceptable types:
        [core/video/HTML5VideoPlayer.hbs]
        [elemental/AwsElementalVideoPlayer.hbs]
        [jwplayer/JWVideoPlayer.hbs]
        [kaltura/KalturaVideoPlayer.hbs]
        [mpx/MPXVideoPlayer.hbs]
        [vimeo/VimeoVideoPlayer.hbs]
        [youtube/YouTubeVideoPlayer.hbs]
```

This error indicates that a View Key was used in a field for which the root styleguide doesn't have an example.

Again, the appropriate action depends on the intent.

In this case, the error was accompanied by another Brightcove related error:

```
    [brightcove/BrightcoveVideoPlayer.hbs] does not exist in the root styleguide!
      at
        brightcove/BrightcoveVideoModule.json at (line=4, col=14, offset=99)
        brightcove/BrightcoveVideoPlayer.json at (line=1, col=2, offset=1)
        examples/Article-Video-Lead.json at (line=5, col=16, offset=115)
        examples/VideoPage-Brightcove.json at (line=4, col=14, offset=104)
```

The appropriate action, then, is to add BrightcoveVideoPlayer.hbs and its usages to the root styleguide.

Here's another common instance of this error:

```
    [core/list/List.hbs], field [items] accepts types that are incompatible with the root styleguide:
      [core/promo/PromoC.hbs] at:
        core/list/ListA.json at (line=5, col=10, offset=118)
        core/list/ListB.json at (line=5, col=10, offset=125)
        core/list/ListC.json at (line=5, col=10, offset=118)
        core/list/ListLoadMore.json at (line=5, col=10, offset=118)
        core/list/ListTags.json at (line=5, col=10, offset=118)
      Acceptable types:
        [...]
        [core/promo/Promo.hbs]
        [...]
```

This error is also accompanied by the error `[core/promo/PromoC.hbs] does not exist in the root styleguide!`, but in
this case the appropriate fix is to register `PromoC.hbs` as a style of `Promo.hbs` (See the "Handlebars file does not
exist in the root styleguide" section above for more details).

### Conflicting Parents

Error message:
```
    Error: [blog/blogpost/BlogPostPage.hbs] has conflicting parents:
      core/article/ArticlePage.hbs at
        blog/blogpost/BlogPostPage.json at (line=1, col=2, offset=1)
      core/page/Page.hbs at
        examples/BlogPostPage.json at (line=1, col=2, offset=1)
```

A View Key must have a consistent Parent View Key. The remedy for this error is to ensure all JSON files that share a
View Key also `"_include"` the same parent JSON file to establish compatible Parent View Key relationships.

### Cyclic Ancestry

Error message:
```
    Exception: Cyclic ancestry detected: [core/article/ArticlePage.hbs, core/page/CreativeWorkPage.hbs, core/page/ContentPage.hbs, core/page/Page.hbs]
    Error: [blog/blogpost/BlogPostPage.hbs] has cyclic ancestry:
      core/article/ArticlePage.hbs at
        blog/blogpost/BlogPostPage.json at (line=1, col=2, offset=1)
        examples/BlogPostPage.json at (line=1, col=2, offset=1)
      core/page/CreativeWorkPage.hbs at
        core/article/ArticlePage.json at (line=1, col=2, offset=1)
      core/page/ContentPage.hbs at
        core/page/CreativeWorkPage.json at (line=1, col=2, offset=1)
      core/page/Page.hbs at
        examples/PeoplePage.json at (line=1, col=2, offset=1)
        examples/EventFormPage.json at (line=1, col=2, offset=1)
        core/page/ContentPage.json at (line=1, col=2, offset=1)
        core/page/OneOffPage.json at (line=1, col=2, offset=1)
        examples/EventFormPage2.json at (line=1, col=2, offset=1)
        examples/OneOffPage.json at (line=1, col=2, offset=1)
```

View Key ancestry must not result in an infinite loop. For example:

```
CreativeWorkPage's parent is ContentPage
ContentPage's parent is Page
Page's parent is CreativeWorkPage
```

This error is usually caused by Conflicting Parents, so fix all of those errors first. If you've addressed all of the
Conflicting Parents errors and still can't determine the cause, remember to check the listed files in the `build`
directory, as the build process modifies the JSON files and something may stand out that isn't immediately obvious when
looking in the `styleguide` directory.
