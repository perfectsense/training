/**
 * Element that handles toggling state for another element on the page.
 * Note: Styling can be handled on an individual need basis by adding a class
 * to the element.
 */
export class PsToggler extends window.HTMLElement {
  connectedCallback () {
    let myToggleName = this.getAttribute('data-toggle')
    let allMyToggles = this.querySelectorAll(
      '[data-toggle-trigger="' + myToggleName + '"]'
    )
    let self = this

    /**
     * Handles toggling behavior. This is here so we can use a closure to pass self in
     *
     * Note: We set a data attribute on ourselves as well as the body.
     * It has the name of the toggle in it, so we can have multiple toggles at once on the page,
     * as well as toggles within toggles.
     */
    this.handleToggle = function (event) {
      event.preventDefault()

      let toggleName = this.getAttribute('data-toggle-trigger')
      let toggleDataAttribute = 'data-toggle-in'

      if (toggleName) {
        if (self.getAttribute(toggleDataAttribute) === toggleName) {
          document.body.removeAttribute(toggleDataAttribute)
          self.removeAttribute(toggleDataAttribute, toggleName)
        } else {
          document.body.setAttribute(toggleDataAttribute, toggleName)
          self.setAttribute(toggleDataAttribute, toggleName)
        }
      }
    }

    allMyToggles.forEach(item =>
      item.addEventListener('click', this.handleToggle, true)
    )
  }

  disconnectedCallback () {
    let allToggles = this.querySelectorAll('[data-toggle-trigger]')

    allToggles.forEach(item =>
      item.removeEventListener('click', this.handleToggle, true)
    )
  }
}
