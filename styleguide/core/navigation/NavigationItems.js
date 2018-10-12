import $ from 'jquery'

export default class PageNavigationItems {
  constructor (el, options) {
    this.$el = $(el)
    this.settings = Object.assign({}, {}, options)
    let $clickableArrow = this.$el.prev().find('.PageNavigationItem-text-arrow')
    let $PageNavigationItem = this.$el.parent('.PageNavigationItem')
    let pageNavigationItem = $PageNavigationItem[0]

    $clickableArrow.on('click', (e) => {
      if (!pageNavigationItem.hasAttribute('data-show-nav-items')) {
        pageNavigationItem.setAttribute('data-show-nav-items', 'true')
      } else {
        pageNavigationItem.removeAttribute('data-show-nav-items')
      }
    })
  }
}
