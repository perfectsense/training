export default class SearchOverlay extends HTMLElement {
  connectedCallback() {
    this.cacheElements()
    this.init()
  }

  cacheElements() {
    this.searchButton = this.querySelector('[class$="-search-button"]')
    this.searchInput = this.querySelector('[class$="-search-input"]')
    this.searchInputClear = this.querySelector('[class$="-search-form-clear"]')
    this.menuButton = document.querySelector('.Page-header-menu-trigger')
    this.pageHeader = document.querySelector('.Page-header')
  }

  init() {
    if (this.searchButton) {
      this.searchButton.addEventListener('click', (e) => {
        e.preventDefault()

        if (this.isSearchOpen()) {
          this.closeSearch()
        } else {
          this.closeMenu()
          this.openSearch()
        }
      })
    }

    if (this.searchInputClear) {
      this.searchInputClear.addEventListener('click', (e) => {
        e.preventDefault()
        this.searchInput.value = ''
      })
    }
  }

  openSearch() {
    document.body.setAttribute('data-toggle-header', 'search-overlay')
    this.setAttribute('data-toggle-header', 'search-overlay')
    this.searchInput.focus()
  }

  closeSearch() {
    if (this.isSearchOpen()) {
      document.body.removeAttribute('data-toggle-header')
      this.removeAttribute('data-toggle-header')
    }
  }

  closeMenu() {
    document.body.removeAttribute('data-toggle-header')
    this.pageHeader.removeAttribute('data-toggle-header')
    this.menuButton.setAttribute('aria-expanded', 'false')
  }

  isSearchOpen() {
    if (document.body.getAttribute('data-toggle-header') === 'search-overlay') {
      return true
    } else {
      return false
    }
  }
}
