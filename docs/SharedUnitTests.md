# Shared Unit Tests

These unit tests can be globally applied to all Brightspot Express projects.

## Usage:
Shared Unit Tests are included as part of the StarterKit. For legacy maven project setup see [Legacy Maven Projects](#legacy-maven-projects) below.

## Ignoring Certain Classes for Dynamic Tests
To exclude a certain class from ClassFinder-backed dynamic tests, create
a *.ignored.txt file based on the fully qualified name of the test class. All
discovered files will be combined into one set of classes to ignore.

For example, given the testClass name of brightspot.example.ExampleTest:

1) /brightspot/example/ExampleTest.ignored.txt
2) /brightspot/example/ignored.txt
3) /brightspot/ignored.txt
4) /ignored.txt

The following files will also be evaluated, but these files are reserved
for internal use only - that is, projects should create *.ignored.txt files,
and *.defaultIgnored.txt files must only be created in the component-lib project.

5) /brightspot/example/ExampleTest.defaultIgnored.txt
6) /brightspot/example/defaultIgnored.txt
7) /brightspot/defaultIgnored.txt
8) /defaultIgnored.txt

This file should be available as a test resource. The format of this file
is one class name or regular expression per line. Lines beginning with "#" are
treated as comments and ignored.

For typical multi-module Brightspot projects, these files belong in the "site"
project under "./src/test/resources/...".

## ViewModelAbstractMethodErrorTest
This test validates that all ViewModel implementations in
your project have concrete implementations of every method declared in their
respective View interfaces. This test can fail if View interfaces defined in
Express are redefined in your project styleguide without also extending the
corresponding ViewModels in your project.

To exclude specific ViewModels classes from this test, see "Ignoring Certain
Classes for Dynamic Tests" above. The fully qualified class name is
`brightspot.viewmodel.ViewModelAbstractMethodErrorTest`, "ignored" filename is
`./src/test/resources/brightspot/viewmodel/ViewModelAbstractMethodErrorTest.ignored.txt`.

## AmbiguousViewModelTest
This test fails the build if any View/Model combinations
have multiple ambiguous ViewModel implementations. This prevents the common error in logs like
```
WARNING: Found [2] conflicting view model bindings for model type [brightspot.core.gallery.Gallery] and view type [com.psddev.styleguide.core.promo.PromoView]: [brightspot.dummy.RedundantPromoViewModel, brightspot.
core.promo.PromotablePromoViewModel]
```

To exclude specific ViewModel, View Interface, or Model classes from this test,
see "Ignoring Certain Classes for Dynamic Tests" above. The fully qualified class name is
`brightspot.viewmodel.AmbiguousViewModelTest`, "ignored" filename is
`./src/test/resources/brightspot/viewmodel/AmbiguousViewModelTest.ignored.txt`.

## PageViewsNotIncludedInOtherViewsTest
This test fails the build if any Views implemented by PageEntryView ViewModels are themselves
included as the field type for a View. For example, this setup would fail the test:
```
ArticlePage.json
{
  "_template": "ArticlePage.hbs",
}

Page.json
{
  "_template": "Page.hbs",
  "below": {
    "_include": "ArticlePage.json"
  }
}


public interface ArticlePageView extends PageViewBelowField { }

public class ArticlePageViewModel extends ViewModel<Article> implements
        ArticlePageView,
        PageEntryView {
}
```

When page-level Views are included in other Views it can result in circular includes
in the styleguide build. Even if the styleguide builds, this is generally a sign of improper
modeling in styleguide. To resolve this, Views which include the page-level View should instead
include a separate View, e.g. Promo rather than ArticlePage. Creating a new View may also be
warranted. An example fix for the above scenario would be:
```
ArticlePage.json
{
  "_template": "ArticlePage.hbs",
}

Page.json
{
  "_template": "Page.hbs",
  "below": {
    "_include": "Promo.json"
  }
}
```

To exclude specific ViewModel or View Interface classes from this test,
see "Ignoring Certain Classes for Dynamic Tests" above. The fully qualified class name is
`brightspot.viewmodel.PageViewsNotIncludedInOtherViewsTest`, "ignored" filename is
`./src/test/resources/brightspot/viewmodel/PageViewsNotIncludedInOtherViewsTest.ignored.txt`.

## EnhancementViewModelTest
This test fails the build if any RichTextElements do not have ViewModels implementing
EnhancementViewItemField.

To exclude specific RichTextElement classes from this test,
see "Ignoring Certain Classes for Dynamic Tests" above. The fully qualified class name is
`brightspot.enhancement.EnhancementViewModelTest`, "ignored" filename is
`./src/test/resources/brightspot/enhancement/EnhancementViewModelTest.ignored.txt`.

## SingleSubstitutionTest
This test fails the build if any types are substituted with
Substitutions more than once. This is invalid, but it is only possible to
catch at runtime.

To exclude specific Substitution or Model classes from this test,
see "Ignoring Certain Classes for Dynamic Tests" above. The fully qualified class name is
`brightspot.objecttype.SingleSubstitutionTest`, "ignored" filename is
`./src/test/resources/brightspot/objecttype/SingleSubstitutionTest.ignored.txt`.

## NoAbstractSubstitutionTest
This test fails the build if any Substitutions extend abstract classes. This is invalid,
but it is only possible to catch at runtime.

To exclude specific Substitution or Model classes from this test,
see "Ignoring Certain Classes for Dynamic Tests" above. The fully qualified class name is
`brightspot.objecttype.NoAbstractSubstitutionTest`, "ignored" filename is
`./src/test/resources/brightspot/objecttype/NoAbstractSubstitutionTest.ignored.txt`.

## NoSubstitutionInterfacesTest
Implementing an interface with a Substitution has no effect, and it is likely that
the intent was to create an Augmentation instead.

To exclude specific Substitution classes from this test,
see "Ignoring Certain Classes for Dynamic Tests" above. The fully qualified class name is
`brightspot.objecttype.NoSubstitutionInterfacesTest`, "ignored" filename is
`./src/test/resources/brightspot/objecttype/NoSubstitutionInterfacesTest.ignored.txt`.

## AlterationClassAnnotationProcessorTest
Alteration can be used to add annotations to an existing model class. The annotations
added must have an associated ObjectType.AnnotationProcessorClass to be compatible with
Alteration.

To exclude specific Alteration classes from this test,
see "Ignoring Certain Classes for Dynamic Tests" above. The fully qualified class name is
`brightspot.objecttype.AlterationClassAnnotationProcessorTest`, "ignored" filename is
`./src/test/resources/brightspot/objecttype/AlterationClassAnnotationProcessorTest.ignored.txt`.

## AlterationFieldAnnotationProcessorTest
Alteration can be used to add annotations to the fields of an existing model class. The
annotations added must have an associated ObjectField.AnnotationProcessorClass to be compatible
with Alteration.

To exclude specific Alteration classes from this test,
see "Ignoring Certain Classes for Dynamic Tests" above. The fully qualified class name is
`brightspot.objecttype.AlterationFieldAnnotationProcessorTest`, "ignored" filename is
`./src/test/resources/brightspot/objecttype/AlterationFieldAnnotationProcessorTest.ignored.txt`.

## AlterationFieldsExistTest
Adding fields to an Alteration that do not already exist in the target class has no
effect, and it is likely that the intent was to create a Modification instead.

To exclude specific Alteration classes from this test,
see "Ignoring Certain Classes for Dynamic Tests" above. The fully qualified class name is
`brightspot.objecttype.AlterationFieldsExistTest`, "ignored" filename is
`./src/test/resources/brightspot/objecttype/AlterationFieldsExistTest.ignored.txt`.

## NoAlterationMethodsTest
Methods on an Alteration class will be completely ignored, and it is likely that
the intent was to create either a Modification or Substitution instead.

To exclude specific Alteration classes from this test,
see "Ignoring Certain Classes for Dynamic Tests" above. The fully qualified class name is
`brightspot.objecttype.NoAlterationMethodsTest`, "ignored" filename is
`./src/test/resources/brightspot/objecttype/NoAlterationMethodsExistTest.ignored.txt`.

## SearchablePromotableTest
Promotables are often queried from Solr (e.g. in DynamicItemStream) which can cause
exceptions if there are models implementing Promotable which are not also in the Searchable
group.

To exclude specific Recordable classes from this test,
see "Ignoring Certain Classes for Dynamic Tests" above. The fully qualified class name is
`brightspot.objecttype.SearchablePromotableTest`, "ignored" filename is
`./src/test/resources/brightspot/objecttype/SearchablePromotableTest.ignored.txt`.

## ValidObjectFieldTypesTest
Fields on Recordable types must be a valid Dari primitive type or another Record.
Attempting to use types that do not meet this requirement result in a runtime warning:
`Can't use [class java.util.SomeClass] for [someField] in [com.some.record.Class]`.

This test fails if any such fields are detected.

To exclude specific Recordable classes from this test,
see "Ignoring Certain Classes for Dynamic Tests" above. The fully qualified class name is
`brightspot.objecttype.ValidObjectFieldTypesTest`, "ignored" filename is
`./src/test/resources/brightspot/objecttype/ValidObjectFieldTypesTest.ignored.txt`.

## CdnCssTest
Maven projects see [Maven CdnCssTest](#maven-cdncsstest) below

This test checks enumerated CSS files in themes or the root styleguide to ensure
that all URLs contained in `url()` directives actually exist.

The Brightspot gradle plugin automatically configures all themes to look for an All.min.css in the styleguide.

CSS files can be manually specified with a comma-separated list in `site/build.gradle` like so:

```groovy
test {
    systemProperties.put('theme.my-theme.cdnCssFiles', 'styleguide/All.min.css,styleguide/bootstrap.min.css')
}
```

## ThemeCompatibilityTest
This test checks all themes for compatibility with the root styleguide.

The mechanism for doing so is simply running Styleguide Codegen against each theme and comparing the generated view
class definitions against those generated for the root styleguide.

Like CdnCssTest, themes are defined with properties like `theme.${themeName}.buildDir`. These are set for you
automatically using express-site.gradle plugin version 1.0.19 or higher.

The default behavior is to print a warning when incompatibilities are detected but not fail the build. In order to fail
the build, add the `test` section below to `site/build.gradle`.

It is also necessary to add `com.psddev:styleguide-codegen` as a `testImplementation` dependency. This is done for you if you're using brightspot-gradle-plugins v4.1.12 or newer.

`site/build.gradle`:
```groovy
dependencies {
  testImplementation 'com.psddev:styleguide-codegen'
}

test {
    project.rootProject.allprojects.each {
        if (it.name.contains('-theme-')) {
            systemProperties.put('theme.' + it.name + '.compatibilityTestMode', 'error')
        }
    }
}
```

Alternatively, in order to set the test mode for individual themes:

```groovy
test {
    systemProperties.put('theme.my-theme.compatibilityTestMode', 'error')
}
```

Valid values for compatibilityTestMode are `error`, `warn`, and `disable`.

### Exclusions

Exclusions are not recommended, but if they are absolutely necessary you can create another system propery called
`theme.${themeName}.compatibilityExcludedPaths` with a comma-separated list of excluded paths. For example, in `site/build.gradle`:

```groovy
test {
    systemProperties.put('theme.my-theme.compatibilityExcludedPaths', 'examples,core/video')
}
```

See: [Example Theme Compatibility Test Failures](ThemeCompatibilityTestFailures.md) for details on common test failures and what to do about them.

## ModificationFieldInternalNamePrefixTest
This test checks all Modifications of Object, Record, abstract classes, and interfaces
to ensure that either all serializable fields are annotated with an InternalName or the modification
class itself is annotated with a FieldInternalNamePrefix.

To exclude specific Modification classes from this test,
see "Ignoring Certain Classes for Dynamic Tests" above. The fully qualified class name is
`brightspot.objecttype.ModificationFieldInternalNamePrefixTest`, "ignored" filename is
`./src/test/resources/brightspot/objecttype/ModificationFieldInternalNamePrefixTest.ignored.txt`.
Classes in the `brightspot.*` and `com.psddev.*` packages are ignored by this test since several
modifications in Brightspot fail this test.

## Legacy Maven Projects
Add this to the `<dependencies>` section of your `site/pom.xml` (replace
`$version` with latest release found
[here](https://github.com/perfectsense/component-lib/releases)):
```
<dependencies>
    <dependency>
        <groupId>com.psddev.component-lib</groupId>
        <artifactId>shared-unit-tests</artifactId>
        <version>$version</version>
        <scope>test</scope>

        <dependency>
            <groupId>com.psddev</groupId>
            <artifactId>dari-h2</artifactId>
            <scope>test</scope>
        </dependency>
    </dependency>
</dependencies>
```

Add this to the `<build>` section of your `site/pom.xml` (or add the `<build>`
section if it doesn't exist already). Note the "YOUR_PROJECT_NAME" string - it
should result in the name of the `frontend` artifact. Other themes can (and
should!) be defined here as well, see CdnCssTest below for details.
```
<build>
    <plugins>
        <plugin>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.21.0</version>
            <dependencies>
                <dependency>
                    <groupId>org.junit.platform</groupId>
                    <artifactId>junit-platform-surefire-provider</artifactId>
                    <version>1.2.0-M1</version>
                </dependency>
            </dependencies>
            <configuration>
                <trimStackTrace>false</trimStackTrace><!-- helpful for debugging but annoying if left on all the time -->
                <systemPropertyVariables>
                    <!-- Any test-specific system properties such as theme -->
                    <!-- configuration are defined here -->
                    <theme.root.buildDir>${project.build.directory}/../../frontend/target/YOUR_PROJECT_NAME-frontend-${project.version}</theme.root.buildDir>
                </systemPropertyVariables>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-dependency-plugin</artifactId>
            <executions>
                <execution>
                    <id>unpack</id>
                    <phase>process-test-classes</phase>
                    <goals>
                        <goal>unpack</goal>
                    </goals>
                    <configuration>
                        <artifactItems>
                            <artifactItem>
                                <groupId>com.psddev.component-lib</groupId>
                                <artifactId>shared-unit-tests</artifactId>
                                <outputDirectory>${project.build.directory}/test-classes</outputDirectory>
                            </artifactItem>
                        </artifactItems>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## Maven CdnCssTest
This test checks enumerated CSS files in themes or the root styleguide to ensure
that all URLs contained in `url()` directives actually exist.

To enable this test, add themes to your system properties as indicated in the
Usage text above. For example, to test `All.min.css` and `bootstrap.min.css` in
the `default` theme, add configuration like so to your `maven-surefire-plugin`
plugin `<configuration>` section:

```xml
<configuration>
    <systemPropertyVariables>
        <theme.default.buildDir>${project.build.directory}/../../themes/YOUR_PROJECT_NAME-theme-default/target/classes</theme.default.buildDir>
        <theme.default.cdnCssFiles>styleguide/All.min.css,styleguide/bootstrap.min.css</theme.default.cdnCssFiles>
    </systemPropertyVariables>
</configuration>
```

Alternatively, once the `buildDir` properties have been configured in
`build.gradle`, the list of CSS files can be managed in
`./site/src/test/resources/brightspot/frontend/CdnCssTest.properties` like so:

```properties
theme.default.cdnCssFiles=styleguide/All.min.css,styleguide/bootstrap.min.css
```

The values in `CdnCssTest.properties` file take precedence over the system
properties in `pom.xml`.

