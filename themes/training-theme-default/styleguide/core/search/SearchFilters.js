export class SearchFilters extends window.HTMLElement {
  connectedCallback () {
    this.cacheElements()
    this.handleFiltersOverlay()
    this.handleSortSelect()
    this.handleFiltersApply()
    this.handleFiltersSelect()
    this.handleSelectedFiltersReset()
    this.gatherSelectedFilters()
  }

  cacheElements () {
    this.searchResultsModule = document.querySelector('.SearchResultsModule')
    this.searchForm = document.querySelector('.SearchResultsModule-form')
    this.searchSort = this.querySelector('.SearchResultsModule-sorts select')
    this.filtersOpenButton = this.querySelector(
      '.SearchResultsModule-filters-open'
    )
    this.filtersCloseButton = this.querySelector(
      '.SearchResultsModule-filters-close'
    )
    this.filtersOverlay = this.querySelector(
      '.SearchResultsModule-filters-overlay'
    )
    this.filtersApply = this.querySelector('.SearchResultsModule-filters-apply')
    this.filtersSeeAll = this.querySelectorAll('.SearchFilter-seeAll')
    this.filters = this.querySelectorAll('.SearchFilterInput input')
    this.selectedFiltersArray = []
    this.selectedFilters = this.querySelector(
      '.SearchResultsModule-filters-selected'
    )
    this.selectedFiltersReset = this.querySelector(
      '.SearchResultsModule-filters-selected-reset'
    )
    this.selectedFiltersContent = this.querySelector(
      '.SearchResultsModule-filters-selected-content'
    )
  }

  handleSortSelect () {
    if (this.searchSort) {
      this.searchSort.addEventListener('change', e => {
        this.gatherSelectedFilters()
        // Using API of search results module
        this.searchResultsModule.submitSearch()
      })
    }
  }

  // If we are on a bigger media query, autosubmit the form when we change filters
  handleFiltersSelect () {
    this.filters.forEach(item => {
      item.addEventListener('click', () => {
        if (
          this.getMediaQuery() === 'mq-xl' ||
          (this.getMediaQuery() === 'mq-lg' || this.getMediaQuery() === 'mq-hk')
        ) {
          this.gatherSelectedFilters()
          // Using API of search results module
          this.searchResultsModule.submitSearch()
        }
      })
    })
  }

  // Submit the form on apply button, AJAX is handled by the form itself
  handleFiltersApply () {
    this.filtersApply.addEventListener('change', e => {
      e.preventDefault()
      this.gatherSelectedFilters()
      // Using API of search results module
      this.searchResultsModule.submitSearch()
    })
  }

  // open and close the filters overlay using our own API
  handleFiltersOverlay () {
    this.filtersOpenButton.addEventListener('click', e => {
      e.preventDefault()

      this.openOverlay()
    })

    this.filtersCloseButton.addEventListener('click', e => {
      e.preventDefault()

      this.closeOverlay()
    })
  }

  handleSelectedFiltersReset () {
    this.selectedFiltersReset.addEventListener('click', e => {
      e.preventDefault()

      this.filters.forEach(item => {
        item.checked = false
      })

      this.gatherSelectedFilters()
      // Using API of search results module
      this.searchResultsModule.submitSearch()
    })
  }

  handleSelectedFiltersRemove () {
    let selectedFilters = this.querySelectorAll(
      '.SearchResultsModule-filters-selected-filter'
    )

    selectedFilters.forEach(selectedFilter => {
      selectedFilter.querySelector('a').addEventListener('click', e => {
        e.preventDefault()

        this.removeSelectedFilter(selectedFilter)
      })
    })
  }

  removeSelectedFilter (selectedFilter) {
    let value = selectedFilter.getAttribute('data-value')

    let filter = this.querySelectorAll(`[value="${value}"]`)

    // just in case we have some weird setup with multiple same values
    filter.forEach(item => {
      if (item.checked) {
        item.checked = false
      }
    })

    this.gatherSelectedFilters()
    // Using API of search results module
    this.searchResultsModule.submitSearch()
  }

  gatherSelectedFilters () {
    this.selectedFiltersArray = []
    this.filters = this.querySelectorAll('.SearchFilterInput input')

    this.filters.forEach(filter => {
      if (filter.checked) {
        let label = filter.parentElement.querySelector('span').innerHTML
        let value = filter.getAttribute('value')

        this.selectedFiltersArray.push({
          label: label,
          value: value
        })
      }
    })

    this.renderSelectedFilters()
  }

  renderSelectedFilters () {
    if (this.selectedFiltersArray.length > 0) {
      this.selectedFiltersContent.innerHTML = ''

      this.selectedFiltersArray.forEach(selectedFilter => {
        this.selectedFiltersContent.innerHTML += `<div class="SearchResultsModule-filters-selected-filter" data-value=${
          selectedFilter.value
        }><span>${
          selectedFilter.label
        }</span><a href="#" title="X"><svg class="close-x"><use xlink:href="#close-x"/></svg></a></div>`
      })

      this.handleSelectedFiltersRemove()

      this.selectedFilters.setAttribute('data-showing', true)
    } else {
      this.selectedFilters.removeAttribute('data-showing')
    }
  }

  openOverlay () {
    this.filtersOverlay.setAttribute('data-filters-open', true)
    document.body.setAttribute('data-filters-open', true)
  }

  closeOverlay () {
    this.filtersOverlay.removeAttribute('data-filters-open')
    document.body.removeAttribute('data-filters-open')
  }

  // TODO: Move this to its own utility file/function
  getMediaQuery () {
    let mqValue =
      window
        .getComputedStyle(document.querySelector('body'), '::before')
        .getPropertyValue('content') || false

    if (mqValue) {
      return mqValue.replace(/["']/g, '')
    } else {
      return false
    }
  }
}
