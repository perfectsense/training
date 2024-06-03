export class SearchFilters extends HTMLElement {
  connectedCallback() {
    this.cacheElements()
    this.handleFiltersSelect()
  }

  cacheElements() {
    this.searchResultsModule = this.closest('bsp-search-results-module')
    this.filters = this.querySelectorAll('.SearchFilterInput input')
  }

  // If we are on a bigger media query, autosubmit the form when we change filters
  handleFiltersSelect() {
    this.filters.forEach((item) => {
      item.addEventListener('click', () => {
        this.searchResultsModule.submitSearch()
      })
    })
  }
}
