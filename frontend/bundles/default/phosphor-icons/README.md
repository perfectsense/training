# Phosphor Icons

This theme includes utilities to use Phosphor Icons. You can find available icons at https://phosphoricons.com/.

## Adding icons to the project

List icons and weights to install in [icons.yml](./icons.yml). After making changes run `yarn install`.

Icons will be installed as handlebars templates in the `/styleguide/_icons` directory.

## Using icons in handlebars

An icon file's path can be resolved using the `iconPath` helper. This path can be passed to `include` or similar.

For example:
- To include the `arrow-right` icon use `{{include (iconPath)}}`. The regular icon weight will be used by default.
- To include an alternative weight, such as `fill`, use `{{include (iconPath 'arrow-right', fill')}}`.

## Updating the icon set
Phosphor Icons can be updated by upgrading the node dependency `@phosphor-icons/core`. It may be necessary to then run `yarn install` to ensure the installed icon set is up to date.
