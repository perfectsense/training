export class SearchResultsModule extends window.HTMLElement {
  connectedCallback () {
    this.cacheElements()

    this.handleFormSubmit()
  }

  handleFormSubmit () {
    this.searchForm.addEventListener('submit', e => {
      e.preventDefault()

      let formUrl = new window.URLSearchParams(
        new window.FormData(this.searchForm)
      ).toString()
      let ajaxUrl = window.location.pathname + '?' + formUrl
      window.history.replaceState({}, document.title, ajaxUrl)

      this.loadingTimeout = window.setTimeout(() => {
        this.clearSearchForm()
        this.setLoadingState()
      }, 1000)

      this.getNewSearch(ajaxUrl).then(response => {
        window.clearTimeout(this.loadingTimeout)

        this.clearSearchForm()
        this.renderSearchResults(response)
        document.body.dispatchEvent(
          new window.CustomEvent('Search:onSearchUpdate', {
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
      submitEvent = new window.Event('submit')
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
          reject()
        })
    })
  }

  clearSearchForm () {
    this.searchResults.innerHTML = ''
  }

  setLoadingState () {
    let loadingIcon = document.createElement('div')
    loadingIcon.classList.add('loading-icon')

    this.searchResults.appendChild(loadingIcon)
    this.setAttribute('data-loading', true)
  }

  renderSearchResults (response) {
    // filter HTML response
    let filterDiv = document.createElement('div')
    filterDiv.innerHTML = response
    let searchResultsFromResponse = filterDiv.querySelector(
      '.SearchResultsModule-ajax'
    ).innerHTML

    if (searchResultsFromResponse) {
      this.searchResults.innerHTML = searchResultsFromResponse
    }

    this.dispatchRendered()
  }

  dispatchRendered () {
    let customEvent = new window.CustomEvent('Ajax:Rendered', {
      bubbles: true
    })

    document.body.dispatchEvent(customEvent)
  }

  cacheElements () {
    this.searchForm = this.querySelector('.SearchResultsModule-form')
    this.searchResults = this.querySelector('.SearchResultsModule-ajax')
  }
}
