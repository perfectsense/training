export class SearchActiveFilters extends HTMLElement {
  connectedCallback() {
    this.querySelector(
      '.SearchResultsModuleActiveFilters-clearFilters'
    ).addEventListener('click', () => {
      this.filterButtons.forEach((filterButton) => {
        this.clearFilter(
          filterButton.dataset.filterName,
          filterButton.dataset.filterValue
        )
      })
    })

    this.filterButtons.forEach((filterButton) => {
      filterButton.addEventListener('click', () => {
        this.clearFilter(
          filterButton.dataset.filterName,
          filterButton.dataset.filterValue
        )
      })
    })
  }

  get filterButtons() {
    return this.querySelectorAll('[data-filter-name][data-filter-value]')
  }

  clearFilter(name, value) {
    this.closest('bsp-search-results-module').querySelector(
      `[name="${name}"][value="${value}"]`
    ).checked = false
  }
}
