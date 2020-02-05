# brightspot-theme-falcon

## Initial Setup: Git Subtree

To use this theme in another project, follow these steps in that project's directory:

1. Add the upstream "brightspot-theme-falcon" theme repo as a remote
_Note: This is not pushed to github; every developer planning on executing steps (2) or (3) will need to do this!_
```
$ git remote add -f brightspot-theme-falcon git@github.com:perfectsense/brightspot-theme-falcon.git
```

2. Initialize the ${PROJECT}-theme-falcon directory with the contents of that repo:
```
$ git subtree add --prefix themes/${PROJECT}-theme-falcon brightspot-theme-falcon master --squash
```

3. At a later date, pull changes from the upstream brightspot-theme-falcon theme repo into the project repo
```
git fetch brightspot-theme-falcon master
git subtree pull --prefix themes/${PROJECT}-theme-falcon brightspot-theme-falcon master --squash
```



## Contributing/Customizing the Theme

### Package Management
We use [Yarn](https://yarnpkg.com/) to manage JS packages, so you should make sure that's installed first. Most of us also use [Visual Studio Code](https://code.visualstudio.com/) as the IDE, and if you run into any issues, you're more likely to get help with troubleshooting if you use it. But if you're more comfortable using some other tool, feel free. Just make sure that you still follow all the code formatting rules.

To get ready for development, install all the JS packages locally by running `yarn`.

```console
user@host:brightspot-theme-falcon$ yarn
```

To work on the FE, you need to start Brightspot Styleguide by running `yarn server:styleguide`.

```console
user@host:brightspot-theme-frost$ yarn server:styleguide
```

Once it's running, you'll be able to access it at http://localhost:8080/_styleguide/index.html and whenever you make any changes to the JS or CSS, it will be automatically reflected on any of the pages that you have open in the browser. In case of JS changes, it usually does this by reloading the page, but CSS changes are made in place via [webpack HMR]. If you make HBS changed, you will need to refresh the page once the styleguide:ui task is done (https://webpack.js.org/concepts/hot-module-replacement/).

### Naming

We follow the spirit of the [BEM](http://getbem.com/naming/) naming convention, but the casing rules and delimiters are our own.

* Blocks should be in pascal case like Java class names e.g. `Page`
* Elements should be in camel case like Java field names, preceded by the block name and a hyphen e.g. `Page-header`

### Webpack

We use [webpack](https://webpack.js.org/) to bundle all front-end assets, and it's configured mostly at [webpack.common.js](webpack.common.js). Development-specific settings are at [webpack.dev.js](webpack.dev.js), which adds source maps and [webpack DevServer](https://webpack.js.org/configuration/dev-server/) support. Production-specific settings are at [webpack.prod.js](webpack.prod.js), which enables optimizations and splits out the CSS into a separate file.
 
### JS

* Use [ES2015](https://babeljs.io/docs/en/learn/). We use [Babel](https://babeljs.io/) to transpile the code to be compatible with older browsers or environments and the support matrix is specified in [.browserslistrc](.browserslistrc).
* Follow the [JavaScript Standard Style](https://standardjs.com/). We use [Prettier](https://prettier.io/) along with [ESLint](https://eslint.org/) for automatic formatting so that you don't have to do it manually. If you use Visual Studio Code, see the section on it [below](#visual-studio-code) on how to set it up. If not, you can run `yarn format` to format your code:

```console
user@host:brightspot-theme-frost$ yarn format
```

### CSS

* Use [Less](http://lesscss.org/)
* Try to use native CSS functionalities. This means using [CSS custom properties](https://developer.mozilla.org/en-US/docs/Web/CSS/--*) over [Less variables](http://lesscss.org/#variables).
* CSS vendor prefixes are automatically added via [Autoprefixer](https://github.com/postcss/autoprefixer) configured in [postcss.config.js](postcss.config.js) and using [.browserslistrc](.browserslistrc).

### Visual Studio Code

[Prettier extension](https://github.com/prettier/prettier-vscode) automatically formats the code so that you don't have to. Once you install it, it should just work via the settings in [.vscode/settings.json](.vscode/settings.json). But if you're having issues, let us know, and we can troubleshoot with you.


# Releases

1. Tag the release in git (probably on the `develop` branch) with a semantic version number like `v1.0.0`.
```
git tag v1.0.0
git push origin --tags
```
2. Merge the tagged commit into the `master` branch.
```
git checkout master
git pull
git merge v1.0.0
git push origin master:master

## Notes

This theme is based off of [brightspot-theme-dk](https://github.com/perfectsense/brightspot-theme-dk/blob/master/README.md)

The DK theme is the development toolkit, or starting point, for future themes. 
It's meant to be a non style opinionated theme that sets up some basic site front end. 
It includes things such as promos, lists, containers, some content types and shows development patterns and best practices that can be used to setup custom themes/sites. 
It also provides features/functionality that we do on pretty much every project, such as carousels, galleries, AMP. 
These included features are meant to be bare bones and should be added to/modified for your exact use case. 

