#set($h1 = '#')
#set($h2 = '##')
#set($h3 = '###') 
$h1 Bundles

The project comes with a `default` bundle.

$h2 Creating a new bundle
$h3 To create a new bundle named `test`:

1. `./createbundle.sh --name={rootArtifactId}-bundle-test --bundle=brightspot-bundle-dk`
2. `cd ${rootArtifactId}-bundle-test`
3. `yarn`
4. `yarn build`
5. `yarn server:styleguide`

$h3 To add the `test` bundle to the gradle build:

Add the bundle as a module to the `settings.gradle`
```
include(':${rootArtifactId}-bundles:${rootArtifactId}-bundle-test')
project(':${rootArtifactId}-bundles:${rootArtifactId}-bundle-test').projectDir = file('bundles/${rootArtifactId}-bundle-test')
```

Add the bundle to `site/build.gradle` dependencies
```
compile project(':${rootArtifactId}-bundles:${rootArtifactId}-bundle-test')
```

