/* global HTMLElement */
/**
 * Element that handles toggling state for another element on the page.
 * Note: Styling can be handled on an individual need basis by adding a class
 * to the element.
 */
export class psdToggler extends HTMLElement {

  connectedCallback () {
    this.addEventListener('click', function (e) {
      this._handleToggle(e)
    })
    this.style.opacity = 1
  }

  disconnectedCallback () {
    this.removeEventListener('click', this._handleToggle)
  }

  get toggleTrigger () {
    return this.getAttribute('toggle-trigger')
  }

  /**
   * Handles toggling behavior.
   * Note: We set a data attribute on ourselves as well as the body.
   * It has the name of the toggle in it, so we can have multiple toggles at once on the page,
   * as well as toggles within toggles.
   */
  _handleToggle (event) {
    event.preventDefault()

    const toggleName = this.toggleTrigger
    const toggleDataAttribute = `data-toggle-in-${toggleName}`
    const toggleItem = event.currentTarget.nextElementSibling

    if (!toggleItem) {
      return
    }

    if (document.body.getAttribute(toggleDataAttribute)) {
      toggleItem.removeAttribute(toggleDataAttribute)
      document.body.removeAttribute(toggleDataAttribute)
    } else {
      toggleItem.setAttribute(toggleDataAttribute, true)
      document.body.setAttribute(toggleDataAttribute, true)
    }
  }
}

window.customElements.define('psd-toggler', psdToggler)
