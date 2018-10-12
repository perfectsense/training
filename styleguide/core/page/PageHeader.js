import $ from 'jquery'
import {throttle} from 'styleguide/core/util/Throttle.js'

export default class PageHeader {
  constructor (el, options) {
    this.$el = $(el)
    this.settings = Object.assign({}, {}, options)
    let header = el[0]
    let $menu = this.$el.find('.PageHeader-button')
    let $hat = this.$el.find('.PageHeader-hat')
    let $nav = this.$el.find('.PageHeader-navigation')

    window.addEventListener('scroll', () => {
      throttle(500, this.stickyHeader(this.$el))
    })

    $menu.on('click', (e) => {
      if (!header.hasAttribute('data-show-mobile-nav')) {
        header.setAttribute('data-show-mobile-nav', 'true')
      } else {
        header.removeAttribute('data-show-mobile-nav')
      }
    })

    // If hat and nav exist both exist we need to
    // set the top of the navigation on mobile
    if ($hat.length > 0 && $nav.length > 0) {
      this.setMobileNavTop(this.$el, $nav)
      window.addEventListener('resize', () => {
        throttle(500, this.setMobileNavTop(this.$el, $nav))
      })
    }
  }
  stickyHeader ($el) {
    if ($el.offset().top > 0) {
      $el[0].setAttribute('data-sticky-header', 'true')
    } else {
      $el[0].setAttribute('data-sticky-header', 'false')
    }
  }
  setMobileNavTop ($header, $nav) {
    let windowWidth = window.innerWidth
    let headerHeight = $header.height()
    let nav = $nav[0]

    if (windowWidth <= 767) {
      $nav.css('top', headerHeight - 1) // accounts for border
    } else {
      if (nav.hasAttribute('style')) {
        nav.removeAttribute('style')
      }
    }
  }
}
