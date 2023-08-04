import { throttle } from '../util/Throttle.js'
import hoverintent from 'hoverintent'

export class PageHeader extends window.HTMLElement {
  connectedCallback() {
    this.cacheElements()
    this.handleEventListeners()
    this.dealWithMenuHover()
  }

  cacheElements() {
    this.menuButton = this.querySelector('.Page-header-menu-trigger')
    this.hamburgerMenu = this.querySelector('.Page-header-hamburger-menu')
    this.navigationItems =
      this.hamburgerMenu.querySelectorAll('.NavigationItem')
    this.navigationItemsItem = this.querySelectorAll(
      '[class$="Navigation-items-item"]'
    )
    this.headerBar = document.querySelector('.Page-header-bar')
    this.menuWrapper = this.querySelector('.Page-header-hamburger-menu-wrapper')
    this.searchOverlay = document.querySelector('.SearchOverlay')
  }

  dealWithMenuHover() {
    this.navigationItemsItem.forEach((item) => {
      if (item.querySelector('.NavigationItem-more')) {
        this.hoverListener = hoverintent(
          item,
          () => {
            this.openNavItem(item, 'hover')
          },
          () => {
            this.closeNavItem(item, 'hover')
          }
        ).options({ timeout: 400, interval: 150, handleFocus: true })

        // for accessibility
        item
          .querySelector('.NavigationItem-more')
          .addEventListener('keyup', (e) => {
            if (e.key === 'Enter' || e.keyCode === 32) {
              this.closeOpenNavItem()
              this.openNavItem(item, 'hover')
              item.querySelector('a.NavigationLink').focus()
            }
          })
      }
    })
  }

  handleEventListeners() {
    window.addEventListener(
      'scroll',
      throttle(50, () => {
        if (
          document.body.scrollTop > 0 ||
          document.documentElement.scrollTop > 0
        ) {
          document.body.setAttribute('data-scrolling', true)
        } else {
          document.body.removeAttribute('data-scrolling', true)
        }
      })
    )

    // Using this here instead of the generic toggle plugin as we do not want these search buttons
    // interacting with the generic plugin toggles, and we also want to have them toggle each other
    // off when the other is toggled in
    document.addEventListener('keydown', (e) => {
      if (!e.target.closest('[class$="NavigationItem-items"]')) {
        this.closeOpenNavItem()
      }
    })

    if (this.menuButton) {
      this.menuButton.addEventListener('click', (e) => {
        e.preventDefault()

        if (this.isMenuOpen()) {
          this.closeMenu()
        } else {
          this.closeSearch()
          this.openMenu()
        }
      })
    }

    if (this.navigationItems) {
      this.navigationItems.forEach((item) => {
        if (item.querySelector('.NavigationItem-more')) {
          item
            .querySelector('.NavigationItem-more')
            .addEventListener('click', (e) => {
              e.preventDefault()
              this.openNavItem(item, 'click')
            })
        }
      })
    }
  }

  openNavItem(item, action) {
    if (item) {
      if (item.getAttribute('data-item-' + action)) {
        item.removeAttribute('data-item-' + action)
      } else {
        this.navigationItems.forEach((navItem) => {
          navItem.removeAttribute('data-item-' + action)
        })

        item.setAttribute('data-item-' + action, true)

        const subNav = item.querySelector('.NavigationItem-items')
        if (subNav) {
          if (subNav.getBoundingClientRect().right > window.innerWidth - 20) {
            subNav.setAttribute('data-open-direction', 'left')
          }
        }
      }
    }
  }

  closeNavItem(item, action) {
    item.removeAttribute('data-item-' + action)
  }

  closeOpenNavItem() {
    if (this.querySelector('[data-item-hover]')) {
      this.querySelector('[data-item-hover]').removeAttribute('data-item-hover')
    }
  }

  openMenu() {
    document.body.setAttribute('data-toggle-header', 'hamburger-menu')
    this.setAttribute('data-toggle-header', 'hamburger-menu')
    this.menuButton.setAttribute('aria-expanded', 'true')
    this.hamburgerMenu.focus()
  }

  closeMenu() {
    if (this.isMenuOpen()) {
      document.body.removeAttribute('data-toggle-header')
      this.removeAttribute('data-toggle-header')
      this.menuButton.setAttribute('aria-expanded', 'false')
    }
  }

  closeSearch() {
    document.body.removeAttribute('data-toggle-header')
    if (this.searchOverlay) {
      this.searchOverlay.removeAttribute('data-toggle-header')
    }
  }

  isMenuOpen() {
    if (document.body.getAttribute('data-toggle-header') === 'hamburger-menu') {
      return true
    } else {
      return false
    }
  }
}
