describe('Test Login', function() {
  it('Test login command', function() {
    cy.authenticateUrl("http://apnews.com", "", "")
  })
})