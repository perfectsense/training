// ***********************************************************
// This example support/e2e.js is processed and
// loaded automatically before your test files.
//
// This is a great place to put global configuration and
// behavior that modifies Cypress.
//
// You can change the location of this file or turn off
// automatically serving support files with the
// 'supportFile' configuration option.
//
// You can read more here:
// https://on.cypress.io/configuration
// ***********************************************************

// Import commands.js using ES2015 syntax:
import './commands'

// Alternatively you can use CommonJS syntax:
// require('./commands')

import '@cypress-brightspot/cypress-brightspot';


const resizeObserverLoopErrRe = /^ResizeObserver loop limit exceeded/;

Cypress.on('uncaught:exception', (err) => {
    // if (resizeObserverLoopErrRe.test(err.message)) {
    //     return false;
    // }

    // if (err.message.includes(`Failed to execute 'removeChild' on 'Node':`)) {
    //     return false;
    // }
    // if (err.message.includes(`ResizeObserver loop limit exceeded`)) {
    //     return false;
    // }

    // if (err.message.includes(`Failed to execute 'define' on 'CustomElementRegistry'`)) {
    //     return false;
    // }
    return false;
});