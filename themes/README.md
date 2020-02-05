# Themes

The project comes with a `default` theme.

## Creating a new theme
### To create a new theme named `test`:

1. `./createtheme.sh --name={rootArtifactId}-theme-test --theme=brightspot-theme-dk`
2. `cd training-theme-test`
3. `yarn`
4. `yarn build`
5. `yarn server:styleguide`

### To add the `test` theme to the gradle build:

Add the theme as a module to the `settings.gradle`
```
include(':training-themes:training-theme-test')
project(':training-themes:training-theme-test').projectDir = file('themes/training-theme-test')
```

Add the theme to `site/build.gradle` dependencies
```
compile project(':training-themes:training-theme-test')
```

