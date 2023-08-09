export class SearchFilterDropdown extends HTMLElement {
  #activeFilters = []
  static get observedAttributes() {
    return ['data-open']
  }

  static event = {
    OPEN: 'SearchFilterDropdown:open',
  }

  get filterInputs() {
    return this.querySelectorAll('input[type="checkbox"]')
  }

  connectedCallback() {
    this.buttonElement = this.querySelector('.SearchFilterDropdown-button')
    this.contentElement = this.querySelector('.SearchFilterDropdown-content')
    this.clearFiltersButton = this.querySelector(
      'button.SearchFilterDropdown-clearFilters'
    )

    // Store initial active filters to allow reset when dropdown is canceled
    this.#activeFilters = [...this.filterInputs].filter(
      (input) => input.checked
    )

    window.addEventListener(SearchFilterDropdown.event.OPEN, this)
    document.addEventListener('click', this)
    document.addEventListener('keydown', this)

    if (this.buttonElement) {
      this.buttonElement.addEventListener(
        'click',
        () => (this.open = !this.open)
      )
    }

    if (this.clearFiltersButton) {
      this.clearFiltersButton.addEventListener('click', () => {
        this.clearFilters()
      })
    }
  }

  attributeChangedCallback(attribute) {
    if (attribute === 'data-open' && this.open) {
      this.emit(SearchFilterDropdown.event.OPEN)
    }

    if (attribute === 'data-open' && !this.open) {
      this.resetFilters()
    }
  }

  handleEvent(event) {
    switch (event.type) {
      case SearchFilterDropdown.event.OPEN:
        // Close this filter if another one opens
        if (event.target !== this) {
          this.open = false
        }
        break
      case 'click':
        // Close this filter when clicks occur outside of it
        // todo: consider using focusout
        if (event.target !== this && !this.contains(event.target)) {
          this.open = false
        }
        break
      case 'keydown':
        if (event.key === 'Escape') {
          this.open = false
        }
    }
  }

  resetFilters() {
    this.filterInputs.forEach((input) => {
      input.checked = this.#activeFilters.includes(input)
    })
  }

  clearFilters() {
    this.filterInputs.forEach((input) => (input.checked = false))
  }

  get open() {
    return this.hasAttribute('data-open')
  }

  set open(open) {
    this.toggleAttribute('data-open', open)
  }

  emit(event, detail = {}) {
    const customEvent = new window.CustomEvent(event, {
      bubbles: true,
      detail,
    })
    this.dispatchEvent(customEvent)
  }
}
