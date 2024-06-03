import Flickity from 'flickity'

export default class PageListCarousel extends HTMLElement {
  connectedCallback() {
    this.carouselSlides = this.querySelector('[class$="-items"]') || this

    // waiting until window load to trigger carousels. They still look good before
    // window load, and we have run into race condition and image issues sometimes
    // when we kick it off too fast
    window.addEventListener('load', () => {
      this.carouselOptions = {
        adaptiveHeight: false,
        cellAlign: 'left',
        imagesLoaded: true,
        lazyLoad: 2,
        pageDots: false,
        prevNextButtons: false,
        watchCSS: true, // watch for this, we enable carousels with CSS by default
      }

      if (
        this.classList.contains('PageListCarouselA') ||
        this.classList.contains('ProductListCarousel')
      ) {
        this.carouselOptions = {
          adaptiveHeight: true,
          cellAlign: 'left',
          groupCells: '100%',
          imagesLoaded: true,
          lazyLoad: 2,
          pageDots: false,
          watchCSS: true,
        }
      }

      if (
        this.classList.contains('PageListCarouselB') ||
        this.classList.contains('PageListCarouselD')
      ) {
        this.carouselOptions = {
          adaptiveHeight: false,
          cellAlign: 'left',
          groupCells: '100%',
          imagesLoaded: true,
          lazyLoad: 2,
          pageDots: false,
        }
      }

      if (this.classList.contains('AuthorListB')) {
        this.carouselOptions = {
          adaptiveHeight: false,
          cellAlign: 'center',
          groupCells: '100%',
          imagesLoaded: true,
          lazyLoad: 2,
          pageDots: false,
        }
      }

      if (this.classList.contains('PageListCarouselC')) {
        this.carouselOptions = {
          adaptiveHeight: true,
          cellAlign: 'left',
          imagesLoaded: true,
          lazyLoad: 2,
          prevNextButtons: true,
          pageDots: false,
        }
      }

      if (this.classList.contains('QuoteListA')) {
        this.carouselOptions = {
          adaptiveHeight: false,
          cellAlign: 'left',
          imagesLoaded: true,
          lazyLoad: 2,
          prevNextButtons: true,
          pageDots: false,
        }
      }

      if (this.classList.contains('LogoListA')) {
        this.carouselOptions = {
          adaptiveHeight: true,
          cellAlign: 'left',
          imagesLoaded: true,
          lazyLoad: 2,
          prevNextButtons: true,
          pageDots: false,
          wrapAround: true,
        }
      }

      this.carouselOptions.on = {
        ready: () => {
          this.calculateMediaHeight()
        },
      }

      this.flickity = new Flickity(this.carouselSlides, this.carouselOptions)
    })

    new ResizeObserver(() => {
      this.calculateMediaHeight()
    }).observe(this)
  }

  disconnectedCallback() {
    this.flickity.destroy()
  }

  calculateMediaHeight() {
    const carouselMedia = this.querySelector('.PagePromo-media') || 0
    if (!carouselMedia) return
    this.style.setProperty('--media-height', `${carouselMedia.offsetHeight}px`)
  }
}
