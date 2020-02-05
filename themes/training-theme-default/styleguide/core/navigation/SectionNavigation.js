import { throttle } from '../../util/Throttle.js'
import Flickity from 'flickity'
import hoverintent from 'hoverintent'

// widths determined by designs
const maxWidth = 480
const carouselViewport = 560

export class SectionNavigation extends window.HTMLElement {
  connectedCallback () {
    this.init()
    this.setEventListeners()
    this.setSubNav()
    this.dealWithMenuHover()
  }

  disconnectedCallback () {
    this.hoverListener.destroy()
  }

  init () {
    this.childEls = this.querySelectorAll('.SectionNavigation-items-item')
    this.childStack = Array.from(this.childEls)
    this.subNav = this.querySelector('[data-sub-trigger]')
    this.subNavMenu = this.querySelector('.SectionNavigationItem-subNav')
    this.childWidthTotal = this.getChildWidths(this.childStack)
    this.items = this.querySelector('.SectionNavigation-items')
  }

  dealWithMenuHover () {
    if (this.subNav) {
      this.hoverListener = hoverintent(
        this.subNav,
        () => {
          this.subNav.setAttribute('data-hover', '')
        },
        () => {
          this.subNav.removeAttribute('data-hover')
        }
      )

      this.subNav.addEventListener('focus', () => {
        this.subNav.setAttribute('data-hover', '')
      })

      document.addEventListener('click', e => {
        if (!this.subNav.contains(e.target)) {
          this.subNav.removeAttribute('data-hover')
        }
      })
    }
  }

  // initializing event Listeners
  setEventListeners () {
    window.addEventListener('load', () => {
      this.checkWidths()

      if (window.innerWidth < carouselViewport) {
        this.setCarousel()
      }
    })

    window.addEventListener(
      'resize',
      throttle(250, () => {
        if (
          window.innerWidth < carouselViewport &&
          this.childWidthTotal > window.innerWidth
        ) {
          this.setCarousel()
        } else {
          this.destroyCarousel()
        }
      })
    )
  }

  // creates carousel if enough items
  setCarousel () {
    this.childStack.forEach(child => {
      child.removeAttribute('data-hide')
    })

    this.subNav.setAttribute('data-sub-trigger', 'hide')

    if (this.flickity === undefined) {
      this.flickity = new Flickity(this.items, {
        contain: true,
        cellAlign: 'left',
        adaptiveHeight: true,
        prevNextButtons: false,
        pageDots: false,
        wrapAround: true
      })

      this.flickity.resize()
    }
  }

  // destroys carousel
  destroyCarousel () {
    if (this.flickity) {
      this.flickity.destroy()
      this.checkWidths()
      this.flickity = undefined
    }
  }

  // returns the total width of the items in the array
  getChildWidths (array) {
    let width = 0

    for (let i = 0; i < array.length; i++) {
      let style = window.getComputedStyle(array[i])
      width +=
        array[i].getBoundingClientRect().width +
        parseInt(style.marginLeft) +
        parseInt(style.marginRight)
    }

    return width
  }

  checkWidths () {
    this.childStack.forEach(child => {
      child.removeAttribute('data-hide')
    })

    if (this.childWidthTotal > maxWidth) {
      this.populateNav()
      this.subNav.setAttribute('data-sub-trigger', 'show')
    } else if (this.subNav) {
      this.subNav.setAttribute('data-sub-trigger', 'hide')
    }
  }

  // fills out the sub nav with items that don't fit in main section nav
  populateNav () {
    for (let i = this.childStack.length - 2; i >= 0; i--) {
      let array = this.childStack.slice(0, i)
      let arrayWidths = this.getChildWidths(array)
      this.childStack[i].setAttribute('data-hide', '')

      if (arrayWidths < maxWidth) {
        let diff = this.childStack.length - array.length - 1

        while (diff > 0) {
          this.subNavMenuItems[this.subNavMenuItems.length - diff].setAttribute(
            'data-show',
            ''
          )
          diff--
        }

        break
      }
    }
  }

  setSubNav () {
    let array = this.childStack.slice(0, this.childStack.length - 1)

    array.forEach(child => {
      this.subNavMenu.innerHTML += child.innerHTML
    })

    if (this.subNavMenu) {
      this.subNavMenuItems = this.subNavMenu.querySelectorAll(
        '.SectionNavigationItem'
      )
    }
  }
}
