export class SearchFiltersMobile extends HTMLElement {
  #activeFilters = []

  connectedCallback() {
    this.cacheElements()
    this.handleFiltersOverlay()
    this.handleFiltersApply()
  }

  cacheElements() {
    this.searchResultsModule = this.closest('bsp-search-results-module')
    this.filtersOpenButton = this.searchResultsModule.querySelectorAll(
      '.SearchResultsModule-filters-open'
    )
    this.filtersApplyButton = this.querySelector(
      '.SearchResultsModuleMobileFilters-applyFilters'
    )
    this.filtersClearButton = this.querySelector(
      '.SearchResultsModuleMobileFilters-clearFilters'
    )
    this.dismissButton = this.querySelector(
      '.SearchResultsModuleMobileFilters-dismissButton'
    )
    this.dialogElement = this.querySelector('dialog')
    this.filters = this.querySelectorAll('.SearchFilterInput input')
    // Store initial active filters to allow reset when dropdown is canceled
    this.#activeFilters = [...this.filters].filter((input) => input.checked)
  }

  // Submit the form on apply button, AJAX is handled by the form itself
  handleFiltersApply() {
    this.filtersApplyButton.addEventListener('click', () => {
      this.applyFilters()
    })

    this.filtersClearButton.addEventListener('click', () => {
      this.clearFilters()
      this.applyFilters()
    })
  }

  // open and close the filters overlay using our own API
  handleFiltersOverlay() {
    this.dialogElement.addEventListener('close', () => {
      document.documentElement.style.removeProperty('overflow')
      this.resetFilters()
    })

    this.dismissButton.addEventListener('click', () => {
      this.dialogElement.close()
    })

    this.filtersOpenButton.forEach((filterButton) => {
      filterButton.addEventListener('click', (event) => {
        event.preventDefault()
        this.querySelector('dialog').showModal()
        document.documentElement.style.setProperty('overflow', 'hidden')
      })
    })
  }

  applyFilters() {
    document.documentElement.style.removeProperty('overflow')
    this.filters.forEach((input) => {
      const primaryInput = this.searchResultsModule.querySelector(
        `[name="${input.dataset.passiveInputName}"][value="${input.value}"]`
      )

      primaryInput.checked = input.checked
    })
  }

  clearFilters() {
    this.filters.forEach((input) => {
      input.checked = false
    })
  }

  resetFilters() {
    this.filters.forEach((input) => {
      input.checked = this.#activeFilters.includes(input)
    })
  }
}
