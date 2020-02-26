import hoverintent from 'hoverintent'
import { throttle } from '../../util/Throttle.js'

export class PageHeader extends window.HTMLElement {
  connectedCallback () {
    this.cacheElements()
    this.handleEventListeners()
    this.dealWithMenuHover()

    if (
      this.getMediaQuery() === 'mq-lg' ||
      this.getMediaQuery() === 'mq-hk' ||
      this.getMediaQuery() === 'mq-xl'
    ) {
      this.checkClickTarget()
    }

    this.checkForHeaderSticky()
  }

  cacheElements () {
    this.menuButton = this.querySelector('.Page-header-menu-trigger')
    this.searchButton = this.querySelector('.Page-header-search-button')
    this.searchInput = this.querySelector('.Page-header-search-input')
    this.hamburgerMenu = this.querySelector('.Page-header-hamburger-menu')
    this.navigationItems = this.hamburgerMenu.querySelectorAll(
      '.NavigationItem'
    )
    this.headerBar = document.querySelector('.Page-header-bar')
    this.menuWrapper = this.querySelector('.Page-header-hamburger-menu-wrapper')
    this.event = this.setClickTarget.bind(this)
  }

  checkForHeaderSticky () {
    let lastHeaderBarTop = 0
    let self = this

    function checkHeaderLocation () {
      let headerBarTop = self.headerBar.offsetTop

      if (headerBarTop !== 0) {
        lastHeaderBarTop = headerBarTop
      }

      if (window.scrollY > lastHeaderBarTop) {
        document.body.setAttribute('data-header-sticky', '')
      } else if (document.body.getAttribute('data-header-sticky') === '') {
        document.body.removeAttribute('data-header-sticky')
      }
    }

    window.addEventListener(
      'scroll',
      throttle(50, () => {
        checkHeaderLocation()
      })
    )

    checkHeaderLocation()
  }

  dealWithMenuHover () {
    this.navigationItems.forEach(item => {
      if (item.querySelector('.NavigationItem-more')) {
        this.hoverListener = hoverintent(
          item,
          () => {
            this.openNavItem(item, 'hover')
          },
          () => {
            this.closeNavItem(item, 'hover')
          }
        )
      }
    })
  }

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

  // Using this here instead of the generic toggle plugin as we do not want these search buttons
  // interacting with the generic plugin toggles, and we also want to have them toggle each other
  // off when the other is toggled in
  handleEventListeners () {
    window.addEventListener(
      'resize',
      throttle(250, e => {
        if (
          this.getMediaQuery() === 'mq-lg' ||
          this.getMediaQuery() === 'mq-hk' ||
          this.getMediaQuery() === 'mq-xl'
        ) {
          this.checkClickTarget()

          if (this.navigationItems) {
            this.navigationItems.forEach(item => {
              if (item.querySelector('.NavigationItem-more')) {
                this.closeNavItem(item, 'click')
                this.closeNavItem(item, 'hover')
              }
            })
          }
        } else {
          this.removeClickTarget()
        }
      })
    )

    if (this.menuButton) {
      this.menuButton.addEventListener('click', e => {
        e.preventDefault()

        if (this.isMenuOpen()) {
          this.closeMenu()
        } else {
          this.openMenu()
        }
      })
    }

    if (this.searchButton) {
      this.searchButton.addEventListener('click', e => {
        e.preventDefault()

        if (this.isSearchOpen()) {
          this.closeSearch()
        } else {
          this.openSearch()
        }
      })
    }

    if (this.navigationItems) {
      this.navigationItems.forEach(item => {
        if (item.querySelector('.NavigationItem-more')) {
          item
            .querySelector('.NavigationItem-more')
            .addEventListener('click', e => {
              e.preventDefault()

              this.openNavItem(item, 'click')
            })
        }
      })
    }
  }

  openNavItem (item, action) {
    if (item) {
      if (item.getAttribute('data-item-' + action)) {
        item.removeAttribute('data-item-' + action)
      } else {
        this.navigationItems.forEach(navItem => {
          navItem.removeAttribute('data-item-' + action)
        })

        item.setAttribute('data-item-' + action, true)
      }
    }
  }

  closeNavItem (item, action) {
    item.removeAttribute('data-item-' + action)
  }

  checkClickTarget () {
    document.addEventListener('click', this.event)
  }

  setClickTarget (e) {
    if (this.menuWrapper) {
      let nav = this.menuWrapper.contains(e.target)
      let headerBar = this.headerBar.contains(e.target)

      if (!nav && !headerBar) {
        this.closeMenu()
      }
    }
  }

  removeClickTarget () {
    if (this.event && document.removeEventListener('click', this.event)) {
      document.removeEventListener('click', this.event)
    }
  }

  openMenu () {
    document.body.setAttribute('data-toggle-header', 'hamburger-menu')
    this.setAttribute('data-toggle-header', 'hamburger-menu')
    this.menuButton.setAttribute('aria-expanded', 'true')
    this.hamburgerMenu.focus()
  }

  closeMenu () {
    if (this.isMenuOpen()) {
      document.body.removeAttribute('data-toggle-header')
      this.removeAttribute('data-toggle-header')
      this.menuButton.setAttribute('aria-expanded', 'false')
    }
  }

  isMenuOpen () {
    if (document.body.getAttribute('data-toggle-header') === 'hamburger-menu') {
      return true
    } else {
      return false
    }
  }

  openSearch () {
    document.body.setAttribute('data-toggle-header', 'search-overlay')
    this.setAttribute('data-toggle-header', 'search-overlay')
    this.searchInput.focus()
  }

  closeSearch () {
    if (this.isSearchOpen()) {
      document.body.removeAttribute('data-toggle-header')
      this.removeAttribute('data-toggle-header')
    }
  }

  isSearchOpen () {
    if (document.body.getAttribute('data-toggle-header') === 'search-overlay') {
      return true
    } else {
      return false
    }
  }

  getDistanceFromTop (el) {
    let distance = 0
    if (el.offsetParent) {
      do {
        distance += el.offsetTop
        el = el.offsetParent
      } while (el)
    }
    return distance >= 0 ? distance : 0
  }

  disconnectedCallback () {
    this.hoverListener.destroy()
  }
}
