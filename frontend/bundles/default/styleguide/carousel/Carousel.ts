import { debounce } from '../util/Debounce.js'
import Flickity from 'flickity'

export default class Carousel extends HTMLElement {
  private flickity: Flickity | null = null
  private carouselSlides: HTMLElement | null = null

  get carouselOptions() {
    return {
      adaptiveHeight: false,
      imagesLoaded: true,
      pageDots: false,
      lazyLoad: 1,
      on: {
        ready: () => this.repositionArrows(),
        staticClick: (
          event: Event,
          pointer: Event | Touch,
          cellElement: Element,
          cellIndex: number
        ) => this.onStaticClick(event, pointer, cellElement, cellIndex),
      },
    }
  }

  connectedCallback() {
    this.carouselSlides = this.querySelector('.Carousel-slides')

    if (!this.carouselSlides) return

    this.flickity = new Flickity(this.carouselSlides, this.carouselOptions)

    window.addEventListener(
      'resize',
      debounce(
        50,
        () => {
          this.flickity?.resize()
          this.repositionArrows()
        },
        true
      )
    )
  }

  onStaticClick(
    event: Event,
    pointer: Event | Touch,
    cellElement: Element,
    cellIndex: number
  ) {
    this.dispatchEvent(
      new window.CustomEvent('Carousel:FlickityStaticClick', {
        detail: { event, pointer, cellElement, cellIndex },
        bubbles: true,
      })
    )
  }

  repositionArrows() {
    const selectedCarouselMedia: HTMLElement | null = this.querySelector(
      '.is-selected .GallerySlide-media'
    )

    const flickityButtons: NodeListOf<HTMLElement> = this.querySelectorAll(
      '.flickity-prev-next-button'
    )

    if (!selectedCarouselMedia) return

    flickityButtons.forEach((button) => {
      button.style.top = `${
        (selectedCarouselMedia.offsetHeight - button.offsetHeight) / 2
      }px`
    })
  }

  disconnectedCallback() {
    this.flickity?.destroy()
  }
}
