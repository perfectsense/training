# brightspot-theme-core

## What is Go?

The Go theme is a fully completed style theme compatible with out of the box Brightspot. It is meant to be a completed and QA'd site with all standard content types working correctly. It has been developed with opinionated CSS/JS/HTML patterns and is meant to use as is (with optional color and font choices) for new sites, or can be modified for slight design changes if need be. Rather than modifying it extensively for a site that doesn't conform to the design pattern present in Go, it would be better to start with a new theme instead

## Contributing/Customizing the Theme

TODO

## Package Management
We use [Yarn](https://yarnpkg.com/) to manage JS packages, so you should make sure that's installed first. Most of us also use [Visual Studio Code](https://code.visualstudio.com/) as the IDE, and if you run into any issues, you're more likely to get help with troubleshooting if you use it. But if you're more comfortable using some other tool, feel free. Just make sure that you still follow all the code formatting rules.

To get ready for development, install all the JS packages locally by running `yarn`.

```console
user@host:brightspot-theme-core$ yarn
```

To work on the FE, you need to start Brightspot Styleguide by running `yarn server:styleguide`.

```console
user@host:brightspot-theme-core$ yarn server:styleguide
```

Once it's running, you'll be able to access it at http://localhost:8080/_styleguide/index.html and whenever you make any changes to the JS or CSS, it will be automatically reflected on any of the pages that you have open in the browser. In case of JS changes, it usually does this by reloading the page, but CSS changes are made in place via [webpack HMR]. If you make HBS changed, you will need to refresh the page once the styleguide:ui task is done (https://webpack.js.org/concepts/hot-module-replacement/).

## Naming

We follow the spirit of the [BEM](http://getbem.com/naming/) naming convention, but the casing rules and delimiters are our own.

* Blocks should be in pascal case like Java class names e.g. `Page`
* Elements should be in camel case like Java field names, preceded by the block name and a hyphen e.g. `Page-header`
* Modifiers should use data atttibutes rather than classes. We prefer that as it allows for easier JS hooks if need be

## Webpack

We use [webpack](https://webpack.js.org/) to bundle all front-end assets, and it's configured mostly at [webpack.common.js](webpack.common.js). Development-specific settings are at [webpack.dev.js](webpack.dev.js), which adds source maps and [webpack DevServer](https://webpack.js.org/configuration/dev-server/) support. Production-specific settings are at [webpack.prod.js](webpack.prod.js), which enables optimizations and splits out the CSS into a separate file as well as creating chunks for dynamic JS importing.

If you're needing to manually upload an override theme to the CMS, you need to use the webpack.prod-no-code-splitting.js config, which will disable dynamic JS importing in favor of one combined All.min.js. You can do that by running `yarn build:no-code-splitting`

## JS

* Use [ES2015](https://babeljs.io/docs/en/learn/). We use [Babel](https://babeljs.io/) to transpile the code to be compatible with older browsers or environments and the support matrix is specified in [.browserslistrc](.browserslistrc).
* Follow the [JavaScript Standard Style](https://standardjs.com/). We use [Prettier](https://prettier.io/) along with [ESLint](https://eslint.org/) for automatic formatting so that you don't have to do it manually. If you use Visual Studio Code, see the section on it [below](#visual-studio-code) on how to set it up. If not, you can run `yarn format` to format your code:

```console
user@host:brightspot-theme-core$ yarn format
```

## CSS

* Use [Less](http://lesscss.org/)
* Try to use native CSS functionalities. This means using [CSS custom properties](https://developer.mozilla.org/en-US/docs/Web/CSS/--*) over [Less variables](http://lesscss.org/#variables).
* CSS vendor prefixes are automatically added via [Autoprefixer](https://github.com/postcss/autoprefixer) configured in [postcss.config.js](postcss.config.js) and using [.browserslistrc](.browserslistrc).

## Icons

Icons are provided by [Phosphor Icons](https://phosphoricons.com/). Read about how to configure and use them [here](./phosphor-icons/README.md)

## Visual Studio Code

[Prettier extension](https://github.com/prettier/prettier-vscode) automatically formats the code so that you don't have to. Once you install it, it should just work via the settings in [.vscode/settings.json](.vscode/settings.json). But if you're having issues, let us know, and we can troubleshoot with you.

## Notes

This theme is based off of [brightspot-theme-dk](https://github.com/perfectsense/brightspot-theme-dk/blob/master/README.md)
