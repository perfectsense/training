# bex-training
Bex Training project repository

# New Project Checklist

- [ ] Verify Java dependencies in `core/pom.xml`, `frontend/pom.xml`, and `site/pom.xml`.
- [ ] Verify JavaScript dependencies in `package.json`
- [ ] Verify Theme setup (and possibly generate additional themes) in `themes/`. Instructions are in the [Themes Readme](themes/README.md)
- [ ] Verify project builds.
- [ ] Update `README.md`.
- [ ] Commit changes to git.

# Building for the first time
1.  Run `yarn cache clean && yarn`
2.  Run `mvn clean -Plibrary verify`

# Contributing

* Make sure that the JIRA issue exists that describes the new
  feature, improvement or the bug you're going to work on.
* Make sure that the JIRA issue is assigned to yourself and that the status is
  set to *Dev*. You should also verify that *Issue Type* is correct; *Summary*
  is clear and concise; and *Description* contains the requirements or steps
  to reproduce.
* Branch off of `develop` unless told otherwise.
* All branch names should be prefixed with `feature/` or `bug/` and named descriptively with the bug/feature number
  attached at the front. (e.g. `feature/XY-12-New-Feature-Item`).
* Each commit messages should be brief, descriptive, and grammatically correct
  with proper capitalization and appropriate punctuations.
* Once development is complete, create a pull request with the JIRA issue key
  and a brief description as the title. Attach the feature/bug number at the front of the title to make clear
  what it is associated to. (e.g. "XY-21: Feature NPE When Image is Null")
* Make sure the PR has a label associated to it - e.g. Bug/Feature - as well as a milestone<sup>1</sup>

1: The reason for this is to when looking back at all the PR's that went in to a given release you can clearly see
how many features/bugs were fixed as well as easily filter by Sprint/release schedule.

# Java Class Name & Organization Rules

* Create a package per content type and name it `bex.training.<plugin>.<type>` (e.g. `bex.training.core.group`).
* This package should contain all classes related to that type (e.g. view model classes).
* Imports should be grouped first by java/javax then other non-static imports and finally static imports. Alphabetically within the group.

If you are using IntelliJ you can auto arrange imports in the order described above with the following settings:
![Import Order](https://cloud.githubusercontent.com/assets/1299507/25505623/a45fccd2-2b70-11e7-9bf4-5ab531da4cc6.png)

# Front end

This project uses [Brightspot Express](https://github.com/perfectsense/brightspot-express). See that repository for more documentation.

To get started with the styleguide server (allows front end dev without Tomcat & Brightspot installed):

1.  Run `yarn cache clean && yarn`
2.  Run `gulp styleguide`

All commands run in project root.

The styleguide will then be accessible at [http://localhost:3000](http://localhost:3000).

# Development and Release Process

# Brightspot Environments

Environment | URL                                                                                    | Branch    | Source
----------- | ---------------------------------------------------------------------------------------| --------- | --------------------
QA          | TBD                                                                                    | `develop` | travis, automatic
Prod        | TBD                                                                                    | `master`  | beam deployment

* [JIRA](https://perfectsense.atlassian.net/browse/)

# Development Branches

Branch Name   | Purpose
------------- | ----------------------------------
`develop`     | Latest stable development codebase
`feature/{x}` | Feature-specific changes

# Pull Requests

All commits (outside of the /ops/ directory) are committed to a
`feature/{x}` branch off of `develop`. When the feature is complete,
submit a pull request back to `develop`.

Be sure to include any ticket number(s) in the title of the pull request,
and *always* delete the `feature/{x}` branch after merging the pull request.

# Release and Hotfix Branches

Branch Name   | Purpose
------------- | ----------------------------------
`master`      | Stable current production codebase
`hotfix/{x}`  | Production hotfix

# Release Process

When development for the current release is complete, merge `develop` into
`master`. This starts a build in
[Travis](https://travis-ci.com/perfectsense/bex-training). Take note
of the build number and update the deployment buildNumber in
[prod.yml](ops/beam/prod.yml).

`beam up`, commit this change directly to `master`, and tag this commit with
`v{#}` where `{#}` is a [Semantic Version Number](http://semver.org/).

Merge `master` into `develop` after the release.

No other changes are merged into `master` until the next release.

# Hotfixes

Any unscheduled releases (hotfixes) are branched off of `master`
with a branch name like `hotfix/{x}`, then create a pull request to merge
back into `master`.

Delete the `hotfix/{x}` branch after merging the pull request, then follow
the release process above.
