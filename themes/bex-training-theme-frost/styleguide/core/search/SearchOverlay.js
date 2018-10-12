import { breakpoint } from '../../util/Utils.js'
import plugins from 'pluginRegistry'
export class SearchOverlay {
  constructor (el, options = {}) {
    this.el = el

    this.clearButton = document.querySelector('.SearchOverlay-clearButton')
    this.headerSearch = document.querySelector('[data-header-search]')
    this.searchClose = document.querySelector('.SearchOverlay-close')

    this.init()
  }

  init () {
    if (breakpoint() === 'mq-lg' || breakpoint() === 'mq-xl') {
      this.headerSearch.addEventListener('click', (e) => {
        e.preventDefault()
        this.el.setAttribute('data-searchoverlay-show', true)
        document.body.setAttribute('data-searchoverlay-show', true)
        document.querySelector('.SearchOverlay-input').focus()
      })

      this.searchClose.addEventListener('click', (e) => {
        e.preventDefault()
        this.el.removeAttribute('data-searchoverlay-show')
        document.body.removeAttribute('data-searchoverlay-show')
      })
    }
  }
}

plugins.register(SearchOverlay, '.SearchOverlay')
