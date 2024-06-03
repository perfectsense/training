import { events as LoadMoreEvents } from '../global/LoadMore'
import { SearchFilter } from './SearchFilter'
import { SearchFilterDropdown } from './SearchFilterDropdown'
import { SearchFilters } from './SearchFilters'
import { SearchFiltersMobile } from './SearchFiltersMobile'
import { SearchSorts } from './includes/SearchSorts'

export default class SearchResultsModule extends HTMLElement {
  filterStates = []

  static #state = {
    READY: 'ready',
    SEARCHING: 'searching',
  }

  connectedCallback() {
    this.registerComponents()
    this.cacheElements()
    this.handleSearchInput()
    this.searchForm.addEventListener('submit', this)
    this.addEventListener('click', this)
    this.addEventListener(LoadMoreEvents.loadedContent, this)
    this.state = SearchResultsModule.#state.READY
  }

  get isSearchResultsPage() {
    return !this.moduleId
  }

  get moduleId() {
    return this.getAttribute('data-id')
  }

  get state() {
    return this.getAttribute('data-state')
  }

  set state(state) {
    this.setAttribute('data-state', state)
  }

  handleEvent(event) {
    switch (event.type) {
      case 'submit':
        this.handleFormSubmit(event)
        break
      case LoadMoreEvents.loadedContent:
        this.handleLoadMore(event)
        break
    }
  }

  handleLoadMore(event) {
    const newDocument = event.detail
    const newContent = this.moduleId
      ? newDocument.querySelector(
          `bsp-search-results-module[data-id="${this.moduleId}"]`
        )
      : newDocument.querySelector('bsp-search-results-module:not(data-id)')
    const newResults = newContent.querySelectorAll(
      '.SearchResultsModule-results-item'
    )
    const resultsElement = this.querySelector('.SearchResultsModule-results')
    newResults.forEach((element) => resultsElement.append(element))
    const newLoadMore = newContent.querySelector('bsp-load-more')

    if (newLoadMore) {
      event.target.parentNode.replaceChild(newLoadMore, event.target)
    } else {
      event.target.parentNode.removeChild(event.target)
    }
  }

  handleFormSubmit(event) {
    event.preventDefault()
    this.submitSearch(event)
  }

  submitSearch(event) {
    if (this.state === SearchResultsModule.#state.SEARCHING) {
      return
    }

    this.state = SearchResultsModule.#state.SEARCHING

    // Store filter UI states to allow them to be restored after search is performed
    this.filterStates = [...this.querySelectorAll('bsp-search-filter')].map(
      (filter) => filter.state
    )

    if (this.searchInput && this.searchInput === document.activeElement) {
      this.searchInput.blur()
    }

    const searchParams = new URLSearchParams(new FormData(this.searchForm))

    // Get a complete list of filter parameter names from the search form
    const filterElements = this.querySelectorAll(
      'input[type="checkbox"], select, input[type="radio"]'
    )

    const filterNames = new Set()

    for (const element of filterElements) {
      const elementName = element.name

      if (elementName !== '') {
        filterNames.add(elementName)
      }
    }

    // Ensure that the query string includes empty params
    for (const paramName of filterNames) {
      if (!searchParams.has(paramName)) {
        searchParams.append(paramName, '')
      }
    }

    // Ensure that an empty value is sent instead of 'on'
    for (const [key, value] of searchParams) {
      if (value === 'on') {
        searchParams.set(key, '')
      }
    }

    // Apply the effect of the submitter
    const submitter = event?.submitter

    if (submitter) {
      // Apply the reset action
      if (submitter.hasAttribute('data-reset')) {
        const paramName = submitter.getAttribute('data-parameter-name')

        if (paramName) {
          // Reset named filter
          searchParams.delete(paramName)
        } else {
          // Reset all filters
          for (const [key] of searchParams) {
            if (filterNames.has(key)) {
              searchParams.delete(key)
            }
          }
        }
      }

      // Apply the clear action
      if (submitter.hasAttribute('data-clear')) {
        const paramName = submitter.getAttribute('data-parameter-name')
        const paramValue = submitter.getAttribute('data-parameter-value')

        if (paramName && paramValue) {
          // Clear filter value
          const values = searchParams.getAll(paramName)
          if (values.length > 1) {
            // Delete the key/value pair
            searchParams.delete(paramName, paramValue)
          } else {
            // Explicitly set an empty value
            searchParams.set(paramName, '')
          }
        } else if (paramName) {
          // Clear named filter
          searchParams.set(paramName, '')
        } else {
          // Clear all filters
          for (const [key] of searchParams) {
            if (filterNames.has(key)) {
              searchParams.set(key, '')
            }
          }
        }
      }
    }

    // Get a complete set of potential keys from the Search Form
    const searchFormKeys = new Set()

    this.searchForm
      .querySelectorAll('input, select, textarea')
      .forEach((element) => {
        const elementName = element.name.trim()
        if (elementName) {
          searchFormKeys.add(elementName)
        }
      })

    // Preserve the current URL parameters that are not part of the search filters
    const currentParams = new URLSearchParams(location.search)
    for (const [key, value] of currentParams) {
      if (!searchFormKeys.has(key)) {
        searchParams.append(key, value)
      }
    }

    const requestUrl = window.location.pathname + '?' + searchParams

    if (this.isSearchResultsPage) {
      window.history.replaceState({}, document.title, requestUrl)
    }

    this.getNewSearch(requestUrl).then((response) => {
      this.renderSearchResults(response)
      this.state = SearchResultsModule.#state.READY
    })
  }

  getNewSearch(apiUrl) {
    return new Promise((resolve, reject) => {
      window
        .fetch(apiUrl, {
          credentials: 'include',
        })
        .then((response) => {
          resolve(response.text())
        })
        .catch(() => {
          reject(new Error('error'))
        })
    })
  }

  handleSearchInput() {
    this.clearButton &&
      this.clearButton.addEventListener('click', () => {
        this.searchInput.value = ''
        this.searchInput.focus()
      })
  }

  renderSearchResults(response) {
    // filter HTML response
    const parser = new DOMParser()
    const newDocument = parser.parseFromString(response, 'text/html')
    const newContent = this.moduleId
      ? newDocument.querySelector(
          `bsp-search-results-module[data-id="${this.moduleId}"]`
        )
      : newDocument.querySelector('bsp-search-results-module:not(data-id)')

    const searchResultsFromResponse = newContent.querySelector(
      '.SearchResultsModule-ajax'
    ).innerHTML

    if (searchResultsFromResponse) {
      this.searchResults.innerHTML = searchResultsFromResponse
    }

    this.dispatchRendered()
  }

  dispatchRendered() {
    const customEvent = new CustomEvent('Ajax:Rendered', {
      bubbles: true,
    })

    document.body.dispatchEvent(customEvent)
  }

  cacheElements() {
    this.searchForm = this.querySelector('.SearchResultsModule-form')
    this.searchInput = this.searchForm.querySelector(
      '.SearchResultsModule-formInput'
    )
    this.searchResults = this.querySelector('.SearchResultsModule-ajax')
    this.clearButton = this.searchForm.querySelector(
      '.SearchResultsModule-clearInputButton'
    )
  }

  registerComponents() {
    this.registerComponent('bsp-search-filter', SearchFilter)
    this.registerComponent('bsp-search-filter-drowpdown', SearchFilterDropdown)
    this.registerComponent('bsp-search-filters', SearchFilters)
    this.registerComponent('bsp-search-filters-mobile', SearchFiltersMobile)
    this.registerComponent('bsp-search-sorts', SearchSorts)
  }

  registerComponent(name, Constructor) {
    if (!customElements.get(name)) {
      customElements.define(name, Constructor)
    }
  }
}
