export class SearchFilter extends HTMLElement {
  connectedCallback() {
    this.applyStoredState()
    this.headingElement = this.querySelector('.SearchFilter-heading')
    this.clippingToggle = this.querySelector('[data-toggle-clipped]')

    if (this.headingElement) {
      this.headingElement.addEventListener(
        'click',
        () => (this.expanded = !this.expanded)
      )
    }

    if (this.clippingToggle) {
      this.clippingToggle.addEventListener(
        'click',
        () => (this.clipped = !this.clipped)
      )
    }
  }

  get state() {
    return {
      heading: this.heading,
      expanded: this.expanded,
      clipped: this.clipped,
    }
  }

  set state(state) {
    this.expanded = state.expanded
    this.clipped = state.clipped
  }

  applyStoredState() {
    const stateObject = this.closest(
      'bsp-search-results-module'
    )?.filterStates?.find((entry) => entry.heading === this.heading)

    if (stateObject) {
      this.state = stateObject
    }
  }

  get heading() {
    return this.getAttribute('data-heading')
  }

  get expanded() {
    return this.hasAttribute('data-expanded')
  }

  set expanded(expanded) {
    this.toggleAttribute('data-expanded', expanded)
  }

  get clipped() {
    return this.hasAttribute('data-clipped')
  }

  set clipped(clipped) {
    this.toggleAttribute('data-clipped', clipped)
  }
}
