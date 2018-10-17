# Lists
## Columns
### Not Masonry
* Lists in columnar format are controlled by the `ListGrid` files.
* They range from 1 column to 5 columns
* Editors select from the List's **overrides** panel the number of columns, which maps to `data-atrribute='data-column-count'` on the parent container.
* The columns are created using `css grid`. `N` columns are created by using `n` equal fractional units for the column templates. Gutter spacing defaults to 20px but can be controlled via the css vars that are set globally by the CMS using the **gridGutters Theme Field**
* IE/Edge do not support the full implementation of the W3C CSS grid spec, so they have a fallback. The columns are created using floats and margins. This implementation is separated out so that it can be removed once IE/Edge fully support the spec as written by the W3C.
* There are Less mixins and functions that create the CSS for both the CSS grid implementation and the fallback float implementation. The fallback uses `calc` to to determine the width of each column to accomodate for the variable grid gutter spacing.
### Masonry
* Masonry currently only supports up to 3 columns. Masonry handles the break points. The gutters are also controlled via the **gridGutters Theme Field**. `calc` is used to determine the width of each column to accomodate for the variable grid gutter spacing.
### Column Padding and Text Alignment
* This is controlled by the promos themselves but noting here for clarification. Padding and text alignment are controlled by the global theme fields **modulePadding** and **horizontalAlignment**. **horizontalAlignment** can also be configured at the promo level. The CSS for the module spacing will be a derivative of `n` **modulePadding**, so if the module needs smaller spacing, for example, the spacing will be a percentage of **modulePadding**.


