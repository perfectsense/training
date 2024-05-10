/**
 *  Import the necessary cypress-brightspot page objects we will need for our test.
 *
 * The complete list of available page objects can be found in the module README here: https://www.npmjs.com/package/@cypress-brightspot/cypress-brightspot#page-objects
 */
import {
  deleteArticleIfExist,
  loginPage,
  homePage,
  articleEditPage,
} from "@cypress-brightspot/cypress-brightspot";

/**
 * Import JSON objects containing mock test data.
 *
 * When developing out tests, we can create JSON files as needed to mock test data and reuse them across different tests.
 * Some methods intake JSON object parameters, so you may need to inspect those methods to determine what attributes should be present in the JSON file.
 */
import user from "../../fixtures/user.json";
import site from "../../fixtures/site.json";
import article from "../../fixtures/article.json";

describe("Create Article Test", () => {
  // Before we begin our test, we should reset the test data in our environment by deleting the Article if it already exists
  beforeEach(() => {
    deleteArticleIfExist(site, article, user);
  });

  it("Should publish an Article successfully", () => {
    // As we complete different actions, we will use different page objects following the flow of our Test User.
    // To start, we will use the loginPage object since that is the current page we will be manipulating.
    loginPage.visit();
    loginPage.loginUserViaUI(user);

    // Now, on the CMS Homepage, we will use the homePage object to control our Test User's actions and open a new Article type.
    homePage.switchSites(site.name);
    homePage.createContentType("Article");

    // On the Article Edit Page, we will use the articleEditPage object to fill in fields, publish, and verify success.
    articleEditPage.getHeadlineField().type(article.headline);
    articleEditPage.getSubHeadlineField().type(article.subHeadline);
    articleEditPage.getBodyField().type(article.body);

    articleEditPage.getPublishButton().click();
    articleEditPage.getSuccessMessage().should("be.visible");
  });

  it("Should add a Section to an Article", () => {
    /**
     * All the necessary methods and page objects to do the below should already exist in cypress-brightspot.
     * 
     * Complete the following steps:
     *   1) Re-use the steps from the previous test to create an Article
     *   2) Navigate to the Homepage
     *   3) Search for the newly published Article
     *   4) Set the 'Inspire Sports' Section on the Article
     *   5) Publish the Article
     */
  });
});
