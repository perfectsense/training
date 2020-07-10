# Frontend Test CLI

Command line interface to the frontend shared-unit-tests: ThemeCompatibilityTest and CdnCssTest

# Installation

Download the `brightspot-frontend-test` script and `frontend-test-cli.jar` and place them together in your `$PATH`.

```
cd ~/bin/ (or somewhere on your path)
curl -o frontend-test-cli.jar https://artifactory.psdops.com/public/com/psddev/component-lib/frontend-test-cli/4.1.13-x9ae385/frontend-test-cli-4.1.13-x9ae385.jar
unzip frontend-test-cli.jar brightspot-frontend-test
```

# Usage

The Frontend Test CLI doesn't _build_ your theme, so it operates on the build directory, not the source.

Help:
```
brightspot-frontend-test -h
```

To run only CdnCssTest:
```
brightspot-frontend-test -c styleguide/All.min.css -n "My Theme Name" -t /path/to/theme/build/styleguide/
```

To run only ThemeCompatibilityTest:
```
brightspot-frontend-test -n "My Theme Name" -t /path/to/theme/build/styleguide/ -r /path/to/root/build/styleguide/
```

To run both:
```
brightspot-frontend-test -n "My Theme Name" -c styleguide/All.min.css -t /path/to/theme/build/styleguide/ -r /path/to/root/build/styleguide/
```
