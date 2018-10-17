import imagesLoaded from 'imagesloaded'
import Masonry from 'masonry'
import plugins from 'pluginRegistry'
export class ListMasonry {

  constructor (el, options = {}) {
    this.el = el

    this.init()
  }

  init () {
    const masonryList = this.el
    if (masonryList) {
      const mason = new Masonry(masonryList, {
        columnWidth: '.grid-sizer',
        gutter: '.gutter-sizer',
        horizontalOrder: true,
        itemSelector: '.grid-item',
        percentPosition: true
      })

      imagesLoaded(masonryList).on('progress', function () {
        // layout Masonry after each image loads
        mason.layout()
      })
    }

    // Init flipper on pageload
    var el = document.querySelector('.flipper')

    if (el) {
      window.setTimeout(function () {
        el.classList.add('flip')
      }, 200)

      window.setTimeout(function () {
        el.classList.remove('flip')
      }, 800)
    }
  }
}

plugins.register(ListMasonry, '.ListMasonry-items')
