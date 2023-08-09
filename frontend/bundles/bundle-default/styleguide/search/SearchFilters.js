export class SearchFilters extends window.HTMLElement {
  connectedCallback() {
    this.cacheElements()
    this.handleFiltersSelect()
  }

  cacheElements() {
    this.searchResultsModule = document.querySelector('.SearchResultsModule')
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
