/* global URLSearchParams FormData CustomEvent Event */
import { throttle } from '../util/Throttle'

export class SearchResultsModule extends window.HTMLElement {
  connectedCallback () {
    this.cacheElements()
    this.handleFormSubmit()
    this.handleSearchInput()
    this.checkInputValue()
  }

  handleFormSubmit () {
    this.searchForm.addEventListener('submit', e => {
      e.preventDefault()
      this.searchInput && this.searchInput.blur()

      const formUrl = new URLSearchParams(
        new FormData(this.searchForm)
      ).toString()
      const ajaxUrl = window.location.pathname + '?' + formUrl
      window.history.replaceState({}, document.title, ajaxUrl)

      this.loadingTimeout = setTimeout(() => {
        this.clearSearchForm()
      }, 1000)

      this.getNewSearch(ajaxUrl).then(response => {
        clearTimeout(this.loadingTimeout)

        this.clearSearchForm()
        this.renderSearchResults(response)

        document.body.dispatchEvent(
          new CustomEvent('Search:onSearchUpdate', {
            detail: {
              responseMarkup: response
            }
          })
        )
      })
    })
  }

  /* External API to submit the search, which just triggers this event, which kicks
     off the Ajax stuffs */
  submitSearch () {
    let submitEvent

    if (typeof Event === 'function') {
      submitEvent = new Event('submit')
    } else {
      submitEvent = document.createEvent('Event')
      submitEvent.initEvent('submit', true, true)
    }

    this.searchForm.dispatchEvent(submitEvent)
  }

  getNewSearch (apiUrl) {
    return new Promise((resolve, reject) => {
      window
        .fetch(apiUrl, {
          credentials: 'include'
        })
        .then(response => {
          resolve(response.text())
        })
        .catch(() => {
          reject(new Error('error'))
        })
    })
  }

  clearSearchForm () {
    this.searchResults.innerHTML = ''
  }

  checkInputValue () {
    if (!this.searchInput) return

    if (this.searchInput.value.length === 0) {
      this.searchForm.setAttribute('data-has-value', false)
    } else {
      this.searchForm.setAttribute('data-has-value', true)
    }
  }

  handleSearchInput () {
    this.searchInput &&
      this.searchInput.addEventListener(
        'keyup',
        throttle(250, () => {
          this.checkInputValue()
        })
      )

    this.clearButton &&
      this.clearButton.addEventListener('click', () => {
        this.searchInput.value = ''
        this.searchInput.focus()
        this.checkInputValue()
      })
  }

  renderSearchResults (response) {
    // filter HTML response
    const filterDiv = document.createElement('div')
    filterDiv.innerHTML = response
    const searchResultsFromResponse = filterDiv.querySelector(
      '.SearchResultsModule-ajax'
    ).innerHTML

    if (searchResultsFromResponse) {
      this.searchResults.innerHTML = searchResultsFromResponse
    }
    this.dispatchRendered()
  }

  dispatchRendered () {
    const customEvent = new CustomEvent('Ajax:Rendered', {
      bubbles: true
    })

    document.body.dispatchEvent(customEvent)
  }

  cacheElements () {
    this.searchForm = this.querySelector('.SearchResultsModule-form')
    this.searchInput = this.searchForm.querySelector(
      '.SearchResultsModule-formInput'
    )
    this.searchResults = this.querySelector('.SearchResultsModule-ajax')
    this.clearButton = this.searchForm.querySelector(
      '.SearchResultsModule-clearInputButton'
    )
  }
}
