# E2E Cypress Tests
Brightspot Training E2E Cypress Example

[Cypress](https://docs.cypress.io/guides/overview/why-cypress) is an automated testing tool for running end-to-end tests within the CMS and styleguide. With Cypress, you can easily create tests for modern web applications, debug them visually, and automatically run them in your continuous integration builds, on your local machine, or against lower environments.

As part of our testing practices, we’ve developed a library of useful commands that simplifies the process of writing scripts for complex CMS test cases. [cypress-brightspot](https://github.com/perfectsense/cypress-brightspot) provides extendable classes, ready-made page objects, useful custom commands to be used for writing quick Brightspot CMS Cypress tests.


Table Of Contents
-----------------
<!-- TOC -->

- [E2E Cypress Tests](#e2e-cypress-tests)
  - [Table Of Contents](#table-of-contents)
  - [Local Setup](#local-setup)
  - [Development](#development)
    - [Helpful Cypress Starter Kit](#helpful-cypress-starter-kit)
    - [Cypress-Brightspot](#cypress-brightspot)
    - [Edit Page Builder](#edit-page-builder)

<!-- /TOC -->
<!-- /TOC -->

Local Setup
-----------
1. Start the training projects local dev environment following the README in the project root.
   1. Run `yarn cache clean && yarn`
   2. Run `./gradlew`
   3. Run `docker compose up -d`
2. Naviagate to the `/e2e` directory.
3. Run `npm install` to install Cypress and all necessary dependencies.
4. Run `npx cypress open` to open Cypress GUI and begin running tests!

When testing locally, occasionally wipe relevant docker volumes to ensure consistent E2E testing.
1. Run `docker-compose down`
2. Run `docker volume rm $(docker volume ls -q -f name=training-*)`
3. Run `docker-compose up -d`

Development
-----------

### Helpful Cypress Starter Kit
- [Getting Started](https://docs.cypress.io/guides/core-concepts/introduction-to-cypress)
- [Test Organization](https://docs.cypress.io/guides/core-concepts/introduction-to-cypress)
- [CSS Selectors and Cypress](https://www.browserstack.com/guide/cypress-css-selectors)

### Cypress-Brightspot
The test methods utilizes page object structure to complete standard Brightspot publishing functionality. Most of that logic is contained in the `cypress-brightspot` utility package which we can extend and augment for our own purposes.

An example test using an imported page object straight from our library might look like:

```
import { loginPage, sitesPage } from '@cypress-brightspot/cypress-brightspot'

describe('Example Login Test', () => {
  it('should login and navigate to Sites and Settings', () => {
    cy.visit('/cms')
    loginPage.login(username, password)
    sitesPage.openSiteSettings()

    // And we can go on to publish Site Settings, Articles, etc...
  })
})
```

If you're interested in changing some of our existing functionality by extending or overriding our exported objects or classes, it might look like:

```
//  Extend All Page class from cypress-brightspot package
import { BaseCmsPage } from "@cypress-brightspot/cypress-brightspot";

class ExampleArticlePage extends BaseCmsPage {
  constructor(){
    super();
    this.elements.headline = '.selector'
  }

  getHeadline(){
    cy.log('Extending imported cypress-brightspot class');
    return cy.get(this.elements.newPageElement);
  }
}

// Export example page object
export const onExampleArticlePage = new ExampleArticlePage()
```

If you're interested in just updating field selectors without overriding existing logic use `setElementSelector(element, value)` utility method and pass the element name and update value you need to set

```
  it('Should successfully allow the user to update page elements mid-test', () => {
      loginPage.visit();
      expect(loginPage.elements.loginButton).to.equal('old-selector');
      loginPage.setElementSelector('loginButton', 'new-selector');
      expect(loginPage.elements.loginButton).to.equal('new-selector');
  });
```

### Edit Page Builder
To generate Page Object classes for Cypress from your JSON configuration, follow these steps:

1. Run the following command to generate the JSON file containing the object type definitions:
    ```
    ./gradlew web:exportObjectTypes
    ```

2. Once `objectTypes.json` is available, you can run the `editPageBuilder.js` script to generate Page Object files:

    ```
    node ./node_modules/@cypress-brightspot/cypress-brightspot/examples/editPageBuilder.js "../web/build/objectTypes.json" "./cypress/support/pages"
    ```
    The first argument (`../web/build/objectTypes.json`) specifies the path to the generated `objectTypes.json`.
    The second argument (`./support/pages`) specifies the directory where the generated Page Object files will be created.
  
3. After running the script, you should see the generated Page Object classes in the specified directory. Each class file corresponds to an object type and follows the Cypress Page Object Model structure, ready for use in your tests.