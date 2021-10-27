import { throttle } from '../util/Throttle.js'

export class Carousel extends window.HTMLElement {
  async connectedCallback () {
    const { default: Flickity } = await import('flickity')

    this.carouselSlides = this.querySelector('[class$="-slides"]') || this

    // waiting until window load to trigger carousels. They still look good before
    // window load, and we have run into race condition and image issues sometimes
    // when we kick it off too fast
    window.addEventListener('load', () => {
      this.carouselOptions = {
        adaptiveHeight: false,
        imagesLoaded: true,
        pageDots: false,
        lazyLoad: 2,
        on: {
          ready: () => {
            this.repositionArrows()
          }
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

  repositionArrows () {
    const carouselMedia =
      this.querySelector('.CarouselSlide-media') ||
      this.querySelector('.GallerySlide-media')
    const imgHeight = carouselMedia.offsetHeight
    this.querySelectorAll('.flickity-prev-next-button').forEach(item => {
      item.style.top = `${imgHeight / 2}px`
    })
  }

  disconnectedCallback () {
    this.flickity.destroy()
  }
}
