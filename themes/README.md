# Themes

The project comes with a `default` theme.

## Creating a new theme
### To create a new theme named `test`:

1. `gulp theme --name test`
2. `cd bex-training-theme-test`
3. `gulp styleguide`

### To add the `test` theme to the maven build:

Add the theme as a module to the `themes/pom.xml` file:
```xml
<modules>
    <module>bex-training-theme-default</module>
    <module>bex-training-theme-test</module>
</modules>
```

### To automatically deploy the `test` theme:

Add the theme as a dependency to the `site/pom.xml` file:
```xml
<dependency>
    <groupId>com.bex-training</groupId>
    <artifactId>bex-training-theme-test</artifactId>
    <version>${project.version}</version>
</dependency>
```

## Renaming a theme

### Changing the theme name in maven
You can change the name of a theme using the following steps:

1. Rename the theme's directory (e.g, from `themes/bex-training-theme-oldName` to `themes/bex-training-theme-newName`).
1. Change the `artifactId` and `name` entries in the theme's `pom.xml` file (e.g., `bex-training-theme-oldName` to `bex-training-theme-newName`).
2. Update the `module` entry of the theme in the `themes/pom.xml` aggregate pom.
3. Update the `artifactId` in the theme's `dependency` entry in the `site/pom.xml` pom. 
4. Update the `brightspot.theme.name` entry in the `.brightspot-theme.properties` file in the theme's directory (e.g., `themes/bex-training-theme-newName/.brightspot-theme.properties`).

