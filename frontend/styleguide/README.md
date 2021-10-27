# Hierarchy of a Page

Each of these elements can be individually overridden and reused:

* `head`
  * `language`
  * `charset`
  * `styles`
  * `extraStyles`
  * `viewport`
  * `title`
  * `description`
  * `keywords`
  * `canonicalLink`
  * `meta`
  * `scripts`
  * `extraScripts`

* `body`
  * `header`
    * `hat`
    * `logo`
    * `navigation`
    * `social`
    * `search`
  * `above`
  * `main`
  * `below`
  * `footer`
    * `logo`
    * `navigation`
    * `social`
    * `disclaimer`

## ContentPage (overrides Page)

* `main`
  * `breadcrumbs`
  * `mainContent`
  * `tags`
  * `actions`
  * `comments`

## CreativeWorkPage (overrides ContentPage)

* `datePublished`
* `datePublishedISO`
* `dateModified`
* `dateModifiedISO`
* `headline`
* `subHeadline`
* `byline`

## ArticlePage (overrides CreativeWorkPage)

* `lead`
* `articleBody`

# Examples

I want to wrap the article headline in `<div>` instead of `<h1>`:

```hbs
{{#block "ArticlePage" override="_SourceArticlePage.hbs"}}
    {{#element "headline"}}
        <div class="{{elementName}}">{{this}}</div>
    {{/element}}
{{/block}}
```

I want to hide breadcrumbs and show share bar before the main article display:

```hbs
{{#block "ArticlePage" override="_SourceArticlePage.hbs"}}
    {{#element "main" noWith=true}}
        {{element "actions"}}
        {{element "mainContent"}}
        {{element "tags"}}
        {{element "comments"}}
    {{/element}}
{{/block}}
```

I want to customize the navigation in an article page:

```hbs
{{#block "ArticlePage" override="_SourceArticlePage.hbs"}}
    {{#element "navigation"}}
        {{!-- CUSTOM NAVIGATION CODE HERE --}}
    {{/element}}
{{/block}}
```

I need to put some custom markup in article page header:

```hbs
{{#block "ArticlePage" override="_SourceArticlePage.hbs"}}
    {{#element "header" noWith=true}}
        <header>
            {{element "hat"}}

            <div class="LogoAndNavigation">
                {{element "logo"}}
                {{element "navigation"}}
            </div>

            <div class="SocialAndSearch">
                {{element "social"}}
                {{element "search"}}
            </div>
        </header>
    {{/element}}
{{/block}}
```

# Writing Javascript for Brightspot Express Styleguide

By default, Brightspot Express styleguide compiles Javascript as ES2015 with styleguide/All.js as an entry point.

## Javascript modules

Javascript modules are ES2015 classes which pass a DOM element into the constructor, like this:

```JavaScript
class MyModule {
  constructor (el, options) {
    // do stuff
  }
}
```

## Javascript plugins

Javascript modules are typically instantiated as plugins bound to DOM elements in All.js using the PluginRegistry.

```JavaScript
// DOM elements with the class name 'MyModule' will be instantiated
// on document.readyState = 'interactive' by default
plugins.register(MyModule, '.MyModule')
```

All options:

```JavaScript
plugins.register(<Module>, <selector>, <readyState|resolveFunction>, <options>)
```

  * **Module** - ES2015 class with element and options passed to the constructor.
  ```JavaScript
  class MyModule {
    constructor (el, options) {
      // do stuff
    }
  }
  ```
  * **selector** - CSS selector to find DOM elements to instantiate module
  * **readyState|resolveFunction** - strings `interactive` or `complete` (see [document.readyState documentation](https://developer.mozilla.org/en-US/docs/Web/API/Document/readyState)) or a resolver function which loads the plugin after a promise resolves:
  ```JavaScript
  plugins.register(MyModule, '.MyModule', (resolve, reject, el) => {
    // wait two seconds before loading
    setTimeout(resolve, 2000)
  })
  ```
  * **options** - an object literal to pass load options through JavaScript. It is preferable to set data attributes on elements and to use parent and child classes to manage configuration, so use this judiciously.

Pre-baked load resolver functions:

  * **loadWhenVisible** - don't load a plugin until the element is visible
  ```JavaScript
  import { plugins, loadWhenVisible } from './PluginRegistry.js'
  plugins.register(MyModule, '.MyModule', loadWhenVisible)
  ```
  * **loadWhenLazy** - only apply to elements added after window.load
  ```JavaScript
  import { plugins, loadWhenVisible } from './PluginRegistry.js'
  plugins.register(MyModule, '.MyModule', loadWhenLazy)
  ```
  * **loadWhenScriptsReady** - load after external scripts are loaded. This is useful for external mapping/graphing/etc APIs that are not needed on every page load
  ```JavaScript
  // graph module requiring underscore and plotly
  // only attempts to load these scripts when the module appears
  // on the page
  import { plugins, loadWhenScriptsReady } from './PluginRegistry.js'
  plugins.register(MyGraph, '.MyGraph', loadWhenScriptsReady([
    'https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js',
    'https://cdn.plot.ly/plotly-latest.min.js'
  ]))
  ```

There is more detailed documentation on loading plugins written in JSDoc format inside PluginRegistry.js.

It's also possible for a Javascript module to use another Javascript module:

```JavaScript
import MyModule2 from './MyModule2.js'

export default class MyModule {
  constructor (el) {
    let self = this
    this.m2instances = []
    el.querySelectorAll('.MyModule2').forEach((m2el) => {
      self.m2instances.push(new MyModule2(m2el))
    })
  }
}
```

And it's now possible to load multiple Modules on a single element through the registry:
```JavaScript
import MyModule1 from './MyModule1.js'
import MyModule2 from './MyModule2.js'
plugins.register(MyModule1, '.MyModule')
plugins.register(MyModule2, '.MyModule')
```

You can see which modules are loaded on an element by looking at the `data-bsp-plugin` attribute. However, if the minifier/uglifier on your project is configured to remove function names, you will see a less-readable randomly-generated name.
```html
<div class="SomeElement" data-bsp-plugin="Module51868 Module9113">...</div>
```
To fix this, make sure the minify/uglify task for your project is configured in gulpfile.js to skip minifying function names (classes are still just functions in Javascript and are affected by this option). This code works for [gulp-uglify](https://github.com/terinjokes/gulp-uglify), and there is usually a similar option in other minifiers.
```
uglify({mangle: { keep_fnames: true} })

## Javascript conventions

It is possible to use any transpiler, entry point or Javascript framework you want by overriding the the `styleguide.task.js()` task in gulpfile.js at the project root. However, without a very specific reason to do otherwise, it is recommended to use what is built into Express and to follow these conventions:

*   Prefer using native Javascript to jQuery, as ES2015 has robust support for many features which would have required jQuery in older browsers. Examples:
    * Use `document.querySelector('.MyModule')` instead of `$('.MyModule')`
    * Use `Object.assign` instead of `$.extend`
    * Use `el.addEventListener('click', ...)` instead of `$(el).on('click', ...)`
    * Use `fetch` instead of `$.ajax`
    * And many more, see [You Might Not Need jQuery](http://youmightnotneedjquery.com/) for additional examples
*   Pass options to your plugin by writing them as individual data attributes instead of as a JSON blob inside a single attribute as was done with bsp-utils in the past.
    ```html
    <!-- Don't do this (bsp-utils way) -->
    <div class="MyModule" data-my-module-options='
      {
        "option1" : "value1",
        "option2" : false,
        "option3" : 3,
        "option4" : true,
        "option5" : {
          "key1" : "val1"
        }
      }
    '>...</div>

    <!-- Do this instead -->
    <div class="MyModule" data-option-1="value1" data-option-2="false" data-option-3="3" data-option-4="true" data-option-5='{ "option5" : { "key1" : "val1" } }'>...</div>
    ```
    This facilitates CSS styling.
*   At the moment, options management is not opinionated as it was in bsp-utils, and you can either work directly with `el.dataset` or use getters/setters to do typecasting on the fly:
    ```JavaScript
    class MyModule {
      option1 = ''
      option2 = true
      option3 = 1

      /** typecasting with getter/setter examples */
      get option4 () {
        return JSON.parse(this.el.dataset.option4)
      }
      set option4 (newVal) {
        this.el.dataset.option4 = JSON.stringify(newVal)
      }
      get option5 () {
        if (this.el.dataset.option5 === 'true') {
          return true
        } else {
          return false
        }
      }
      set option5 (newVal) {
        this.el.dataset.option5 = newVal // saved as string even if you pass a bool
      }

      constructor (el) {
        Object.assign(this, el.dataset)
        // if you passed the HTML element from the previous example:
        console.log(this.option1) // string 'value1'
        console.log(this.option2) // string 'false'
        console.log(this.option3) // string '3'
        console.log(this.option4) // bool true
        console.log(this.option5) // JSON object
      }
    }
    ```
*   Do not expose methods to classes when unnecessary. Methods inside ES2015 modules are not exported unless you do so explicitly. Private methods should have an underscore prefix. It is possible to use call/apply or bind operators (when supported by the build) to create private methods.
    ```JavaScript
    // exposed for import elsewhere
    export default class MyModule {
      option1 = 'value1'
      constructor (el) {
        // execute addEvents with call, works same as this.addEvents() would
        _addEvents.call(this)

        // or when supported in the project build, you can
        // use bind operators
        this::_addEvents()
      }
    }
    // not exposed for import elsewhere
    _addEvents () {
      console.log(this.option1) // 'value1'
    }
    ```
*   Where you would have used a defaults object in the past in a bsp-utils plugin, use constants instead and export them so they can be shared between modules:
    ```JavaScript
    export const MY_MODULE_VALUE1 = 2
    export default class MyModule {
      value1 = MY_MODULE_VALUE1
      constructor (el, options) {
        // do stuff
      }
    }
    ```
*   You may need polyfills for Object.assign and Promise for projects that need to support older versions of IE. The most ideal way to do that is to use [babel polyfill](https://babeljs.io/docs/usage/polyfill/)
