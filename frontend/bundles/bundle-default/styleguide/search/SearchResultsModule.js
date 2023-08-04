import { LoadMore } from '../global/LoadMore'
import { SearchActiveFilters } from './includes/SearchActiveFilters'
import { SearchFilter } from './SearchFilter'
import { SearchFilterDropdown } from './SearchFilterDropdown'
import { SearchFilters } from './SearchFilters'
import { SearchFiltersMobile } from './SearchFiltersMobile'
import { SearchSorts } from './includes/SearchSorts'

export class SearchResultsModule extends HTMLElement {
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
    this.addEventListener(LoadMore.events.loadedContent, this)
    this.state = SearchResultsModule.#state.READY
  }

  get state() {
    return this.getAttribute('data-state')
  }

  set state(state) {
    this.setAttribute('data-state', state)
  }

  handleClick(event) {
    if (event.target.matches('[data-action="clearAllFilters"]')) {
      this.clearFilters()
    }
  }

  handleEvent(event) {
    switch (event.type) {
      case 'click':
        this.handleClick(event)
        break
      case 'submit':
        this.handleFormSubmit(event)
        break
      case LoadMore.events.loadedContent:
        this.handleLoadMore(event)
        break
    }
  }

  handleLoadMore(event) {
    const index = [
      ...document.querySelectorAll(`[class="${this.className}"]`),
    ].indexOf(this)
    const newDocument = event.detail
    const newContent = newDocument.querySelectorAll(
      `[class="${this.className}"]`
    )[index]
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
    this.submitSearch()
  }

  clearFilters() {
    this.querySelectorAll(
      '.SearchFilterInput input[name][type="checkbox"]'
    ).forEach((input) => {
      input.checked = false
    })
  }

  submitSearch() {
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

    const formUrl = new URLSearchParams(
      new FormData(this.searchForm)
    ).toString()

    const ajaxUrl = window.location.pathname + '?' + formUrl

    window.history.replaceState({}, document.title, ajaxUrl)

    this.getNewSearch(ajaxUrl).then((response) => {
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

    const searchResultsFromResponse = newDocument.querySelector(
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
    this.registerComponent('bsp-search-active-filters', SearchActiveFilters)
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
