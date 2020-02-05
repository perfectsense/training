import { throttle } from '../../util/Throttle.js'
import Flickity from 'flickity'

export class Carousel extends window.HTMLElement {
  connectedCallback () {
    this.carouselSlides = this.querySelector('[class$="-slides"]') || this

    // waiting until window load to trigger carousels. They still look good before
    // window load, and we have run into race condition and image issues sometimes
    // when we kick it off too fast
    window.addEventListener('load', () => {
      this.handleOptions()
      this.flickity = new Flickity(this.carouselSlides, this.carouselOptions)
      this.setTabIndexing()
      this.handleSlideChange()
    })

    // on resize, we recheck our options against media queries and rebuild the entire
    // carousel if we changed options
    window.addEventListener(
      'resize',
      throttle(250, () => {
        this.handleOptions()
        this.rebuildCarouselIfChanged()
      })
    )
  }

  // for accessibility, since flickity does not do this for us. We want anything hidden
  // to be tabindex -1 along with aria-hidden, as tab navigation can jump into hidden
  // carousel slides if there are links there
  setTabIndexing () {
    this.flickity.cells.forEach(item => {
      if (item.element) {
        if (item.element.getAttribute('aria-hidden') === 'true') {
          item.element.setAttribute('tabindex', '-1')
        } else {
          item.element.setAttribute('tabindex', '0')
        }
      }
    })
  }

  handleSlideChange () {
    this.flickity.on('change', () => {
      this.setTabIndexing()
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

  // we set or reset options here based on the current media query
  // if a conditional has mq specific options, we check to see if the mq
  // changed, and if it did, we set a global changed var so that we
  // can rebuild the carousel

  handleOptions () {
    this.optionsChanged = false

    let arrowShape =
      'M22.4566257,37.2056786 L-21.4456527,71.9511488 C-22.9248661,72.9681457 -24.9073712,72.5311671 -25.8758148,70.9765924 L-26.9788683,69.2027424 C-27.9450684,67.6481676 -27.5292733,65.5646602 -26.0500598,64.5484493 L20.154796,28.2208967 C21.5532435,27.2597011 23.3600078,27.2597011 24.759951,28.2208967 L71.0500598,64.4659264 C72.5292733,65.4829232 72.9450684,67.5672166 71.9788683,69.1217913 L70.8750669,70.8956413 C69.9073712,72.4502161 67.9241183,72.8848368 66.4449048,71.8694118 L22.4566257,37.2056786 Z'

    this.carouselOptions = {
      arrowShape: arrowShape,
      adaptiveHeight: false,
      imagesLoaded: true,
      pageDots: false,
      lazyLoad: 2
    }

    if (this.classList.contains('ListF')) {
      this.carouselOptions = {
        arrowShape: arrowShape,
        adaptiveHeight: false,
        imagesLoaded: true,
        pageDots: false,
        selectedAttraction: 0.018
      }

      if (this.getMediaQuery() === 'mq-md') {
        this.carouselOptions.groupCells = 2
      }

      if (this.getMediaQuery() === 'mq-lg') {
        this.carouselOptions.groupCells = 3
      }

      if (this.getMediaQuery() === 'mq-hk') {
        this.carouselOptions.groupCells = 3
      }

      if (this.getMediaQuery() === 'mq-xl') {
        this.carouselOptions.groupCells = 4
      }

      if (this.didMQChange()) {
        this.optionsChanged = true
      }
    }
  }

  didMQChange () {
    this.changedBreakpoints = false

    if (this.getMediaQuery() === 'mq-xs') {
      if (this.breakpoint !== 'xs') {
        this.changedBreakpoints = true
        this.breakpoint = 'xs'
      }
    }

    if (this.getMediaQuery() === 'mq-sm') {
      if (this.breakpoint !== 'sm') {
        this.changedBreakpoints = true
        this.breakpoint = 'sm'
      }
    }

    if (this.getMediaQuery() === 'mq-md') {
      if (this.breakpoint !== 'md') {
        this.changedBreakpoints = true
        this.breakpoint = 'md'
      }
    }

    if (this.getMediaQuery() === 'mq-lg') {
      if (this.breakpoint !== 'lg') {
        this.changedBreakpoints = true
        this.breakpoint = 'lg'
      }
    }

    if (this.getMediaQuery() === 'mq-hk') {
      if (this.breakpoint !== 'hk') {
        this.changedBreakpoints = true
        this.breakpoint = 'hk'
      }
    }

    if (this.getMediaQuery() === 'mq-xl') {
      if (this.breakpoint !== 'xl') {
        this.changedBreakpoints = true
        this.breakpoint = 'xl'
      }
    }

    return this.changedBreakpoints
  }

  rebuildCarouselIfChanged () {
    if (this.optionsChanged) {
      this.flickity.destroy()
      this.flickity = new Flickity(this.carouselSlides, this.carouselOptions)
      this.setTabIndexing()
    }
  }

  disconnectedCallback () {
    this.flickity.destroy()
  }
}
