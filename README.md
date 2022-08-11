# Brightspot Training

## Navigation and Directory Structure

Take care to add the appropriate [front matter](https://jekyllrb.com/docs/front-matter/).

Each top level section should be in a file like `./_pages/[section-name]/index.md`

The top of this file (index.md) should look like this:
```
---
title: My Section
permalink: /my-section/
has_children: true
---
```

Other files in the same directory (`./_pages/[section-name]/[my-page].md`) should have front matter like this:
```
---
title: My Page
permalink: /my-section/my-page.html
parent: My Section
---
```

Child pages can also have children, if necessary. In this case, add `has_children: true` and optionally `has_toc: false` (to disable the automatic Table of Contents) to the child page, and grandchildren must specify their `parent` and `grand_parent`, like so:
```
---
title: My Grandchild Page
permalink: /my-section/my-grandchild-page.html
parent: My Page
grand_parent: My Section
---
```

When in doubt, review the [Just the Docs navigation documentation](https://just-the-docs.github.io/just-the-docs/docs/navigation-structure/) and/or other examples from this repository.

## Local Authoring

The included `jekyll.sh` script is all you need to preview your changes locally. The only prerequisite is [Docker](https://www.docker.com/products/docker-desktop/).

```sh
$ ./jekyll.sh serve
```

Visit http://localhost:4000/training/ to view the local build of the site. Any changes (other than changes to `_config.yml`) will be reflected automatically.

The theme is [Just The Docs](https://just-the-docs.github.io/just-the-docs/), so check that site for formatting and navigation documentation.
