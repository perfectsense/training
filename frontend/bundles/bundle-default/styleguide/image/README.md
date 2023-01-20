ImageSizing
___________

This project follows the basic and documented setup for images in brightspot's documentation. However there are some extensions added to this toolkit, that should make theming with complex imagesize logic a breeze.

## Creating new crops

Nothing new here. To create a new crop in your theme's styleguide, in the file `_imageSizes.config.json` add a new entry.

Lets for example add the following. Lets create 4 crops each with a different set width and height rules

* Crop 1: 100x200
* Crop 2: 200X400
* Crop 3: Dimensions are fluid, but width is limited at 300
* Crop 4: Height of 500, but width is fluid

### Example
```
{
  "imageSizes": {
    "myCrop1": {
      "width": 100,
      "height": 200
    },
    "myCrop2": {
      "width": 200,
      "height": 400
    },
    "myCrop3": {
      "previewWidth": 300,
      "previewHeight": 400,
      "maximumWidth": 300,
    },
    "myCrop4": {
      "previewWidth": 100,
      "height": 500
    }
  }
}
```

## WebP and sourcesets for more optimal images for the crops

For each of the crops, the developer may assign which images can render an alternate image format of webp, as well "retinize" images for more optimal viewing in modern higher resolution screens

Lets take the example above of Crop1, and allow it to render a 2x pixel density image for retina screens, as well as give it the ability to load a webp image in browsers that support it.

```
{
  "imageSizes": {
    "myCrop1": {
      "width": 100,
      "height": 200,
      "srcsetDescriptors": [
        "1x", "2x"
      ],
      "alternateFormats": [
        "webp"
      ]
    }
```

As you can see for this we just added two properties `srcsetDescriptors` and `alternateFormats`

## Making Responsive Crops (basic method)

### Basic Method
Do skip to the advanced section below for more complex needs [at](#advanced-method). So the simple automatic way to make an alternate image is by suffixing your crop with the text `Alt`

By default the `Alt`ernate image will run at the mediaquery `(min-width: 768px)`.

* Lets use Crop1 to be 1000x1000 at that media query. (Look for the Alt suffixed crop)
```
{
  "imageSizes": {
    "myCrop1": {
      "width": 100,
      "height": 200
    },
    "myCrop1Alt": {
      "width": 1000,
      "height": 1000
    }
```

So as can be seen above, just by suffixing the name of the Crop, with the word `Alt` the image in the browser will automatically change to the much larger 1000x1000 Alt image when the media query is true.

### Advanced Method
This is not really a different method, but lets dive into how this works, and how it may be customized for more complex needs.

#### Understanding where the mediaquery and alt crops come from
Image.hbs is the template that powers all this. In that file you will find the following
```
{{#set imageSize=(fallback (get "imageSizeAlt") (concat (fallback (get "imageSize") "large-16x9") "Alt"))}}
    {{#set mediaQuery=(fallback (get "mediaQueryAlt") "(min-width: 768px)")}}
        {{include "/image/Source.hbs" this}}
    {{/set}}
{{/set}}
```

With the above in mind, look for the following:

* "imageSizeAlt"
* "mediaQueryAlt"
* "Alt"

The above file looks for an Alt suffixed version of the image... *With that in mind you could make as many breakpoints as you want by copying and pasting that, and making Alt2, Alt3, Alt4 variations each with an different default media query*

###### EXAMPLE
Lets add a tablet breakpoint, and this one will react to suffix `Alt2`
```
{{#set imageSize=(fallback (get "imageSizeAlt") (concat (fallback (get "imageSize") "large-16x9") "Alt"))}}
    {{#set mediaQuery=(fallback (get "mediaQueryAlt") "(min-width: 768px)")}}
        {{include "/image/Source.hbs" this}}
    {{/set}}
{{/set}}

{{!ABOVE IS THE EXISTING ALT IMAGE}}
{{!BELOW IS THE NEW ALT2 IMAGE, for a new different media query}}

{{#set imageSize=(fallback (get "imageSizeAlt2") (concat (fallback (get "imageSize") "large-16x9") "Alt2"))}}
    {{#set mediaQuery=(fallback (get "mediaQueryAlt2") "(min-width: 1440px)")}}
        {{include "/image/Source.hbs" this}}
    {{/set}}
{{/set}}
```

#### Putting it altogether into an unlikely rare extra complex scenario
Lets have a list, that does the following. This ridiculous example has everything.

* On mobile,
    * 1st Promo uses *myCrop1*
    * 2nd Promo uses *myCrop2*
    * Every other promo uses *myCrop3*
* On screens 768 to 1279 wide (well need a custom media query for this)
    * 1st Promo changes to *myCropAlt*
* On screens 1280 or wide
    * ALL PROMOS NOW use *myCropAlt*, EXCEPT the *1st* now uses the same crop it was using on mobile *myCrop1* (so we modify media query on this 1st one to only affect tablet still)

##### EXAMPLE of a list, where each Items is the promo
```
{{#each items}}
    {{#set imageSizeMobile="myCropAlt"}} {{!since all promos use the same Alt, I can just reuse one Alt crop instead of making 3 Alt Crops}}

        {{#if (eq @index 0)}}
            {{#set mediaQueryAlt="(min-width: 768px) and (max-width: 1279)"}}
                {{#set imageSize="myCrop1"}}{{this}}{{/set}}
            {{/set}}
        {{else}}
            {{#if (eq @index 1)}}
                {{#set imageSize="myCrop2"}}{{this}}{{/set}}
            {{else}}
                {{#set imageSize="myCrop3"}}{{this}}{{/set}}
            {{/if}}
        {{/if}}
    {{/set}}
{{/each}}
```

So the example above is to be honest ridiculous but it does how a list can be made with complex image crops per breakpoint. More complex layouts may still require adding Alt2, Alt3 or more Alt variations to the project however as shown earlier
