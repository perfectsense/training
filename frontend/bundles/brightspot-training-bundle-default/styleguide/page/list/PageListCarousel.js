import { throttle } from '../../util/Throttle.js'

export class PageListCarousel extends window.HTMLElement {
  async connectedCallback () {
    const { default: Flickity } = await import('flickity')

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
        watchCSS: true // watch for this, we enable carousels with CSS by default
      }

      if (
        this.classList.contains('PageListE') ||
        this.classList.contains('PageListJ')
      ) {
        this.carouselOptions = {
          adaptiveHeight: true,
          cellAlign: 'left',
          groupCells: '100%',
          imagesLoaded: true,
          lazyLoad: 2,
          pageDots: false,
          watchCSS: true
        }
      }

      if (this.classList.contains('PageListF')) {
        this.carouselOptions = {
          adaptiveHeight: false,
          cellAlign: 'left',
          groupCells: '100%',
          imagesLoaded: true,
          lazyLoad: 2,
          pageDots: false
        }
      }

      if (this.classList.contains('AuthorListB')) {
        this.carouselOptions = {
          adaptiveHeight: false,
          cellAlign: 'left',
          groupCells: '100%',
          imagesLoaded: true,
          lazyLoad: 2,
          pageDots: false
        }
      }

      if (this.classList.contains('PageListS')) {
        this.carouselOptions = {
          adaptiveHeight: true,
          cellAlign: 'left',
          imagesLoaded: true,
          lazyLoad: 2,
          prevNextButtons: true,
          pageDots: true
        }
      }

      if (this.classList.contains('QuoteListA')) {
        this.carouselOptions = {
          adaptiveHeight: false,
          cellAlign: 'left',
          imagesLoaded: true,
          lazyLoad: 2,
          prevNextButtons: true,
          pageDots: false
        }
      }

      this.carouselOptions.on = {
        ready: () => {
          this.repositionArrows()
        }
      }

      this.flickity = new Flickity(this.carouselSlides, this.carouselOptions)

      window.addEventListener(
        'resize',
        throttle(50, () => {
          this.repositionArrows()
        })
      )
    })
  }

  disconnectedCallback () {
    this.flickity.destroy()
  }

  repositionArrows () {
    const carouselMedia = this.querySelector('.Promo-media')

    if (carouselMedia) {
      const imgHeight = carouselMedia.offsetHeight

      this.querySelectorAll('.flickity-prev-next-button').forEach(item => {
        item.style.top = `${imgHeight / 2}px`
      })
    }
  }
}
