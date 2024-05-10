const { defineConfig } = require("cypress");
const webpackPreprocessor = require('@cypress/webpack-preprocessor')

// Set isLower to true if you are targetting a lower environment
const isLower = true;

// Default values for environment variables
const defaultEnv = {
  debugUsername: process.env.CYPRESS_debugUsername || "",
  debugPassword: process.env.CYPRESS_debugPassword || "",
  host: process.env.CYPRESS_baseUrl || "http://localhost",
}

// Load local configuration if available
if (isLower) {
  try {
    const lowerConfig = require("./cypress.lower.json");
    Object.assign(defaultEnv, lowerConfig.env || {});
  } catch (e) {
    // Ignore error if the file doesn't exist
  }
}

module.exports = defineConfig({
  defaultCommandTimeout: 15000,
  pageLoadTimeout: 120000,
  responseTimeout: 120000,
  requestTimeout: 120000,
  e2e: {
    specPattern: ["./node_modules/@cypress-brightspot/cypress-brightspot/tests/e2e/cypress/e2e/*.cy.js","cypress/e2e/*.cy.js"],
    baseUrl: defaultEnv.host,
    watchForFileChanges: false,
    chromeWebSecurity: false,
    waitForAnimations: true,
    setupNodeEvents(on, config) {
      on('file:preprocessor', webpackPreprocessor())
    },
  },
  env: defaultEnv,
})


