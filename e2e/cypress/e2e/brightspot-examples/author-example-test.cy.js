/**
 *  Import the necessary cypress-brightspot page objects we will need for our test.
 *
 * The complete list of available page objects can be found in the module README here: https://www.npmjs.com/package/@cypress-brightspot/cypress-brightspot#page-objects
 */
import { loginPage, homePage } from "@cypress-brightspot/cypress-brightspot";

// Import JSON objects containing mock test data.
import user from "../../fixtures/user.json";
import site from "../../fixtures/site.json";
import author from "../../fixtures/author.json";

// Importing page object form authorEditPage.js
import { authorEditPage } from "../../support/authorEditPage";

describe("Create Author Test", () => {
  it("Should publish an Author successfully", () => {
    /**
     * Complete the following steps:
     *   1) Create AuthorEditPage class in '/support/AuthorEditPage.js' to publish all necessary fields.
     *      AuthorEditPage should extend BaseContentTypePage class from cypress-brightspot.
     * 
     *   2) Publish a Author with all fields using '/fixtures/author.json' with test data for each field.
     * 
     *   3) Verify Author fields display correctly on the FE
     */
  });
});
