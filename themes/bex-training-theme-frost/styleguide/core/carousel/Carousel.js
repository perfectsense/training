import plugins from 'pluginRegistry'

/* eslint-disable no-undef */
export default class Carousel {
  constructor (el) {
    this.carousel = el

    // this Flickity function is messing with our smooth scrolling and we don't need it
    // so I'm just suppressing it here
    Flickity.prototype.pointerDownFocus = function (event) { return true }

    let largeCarouselOptions = {
      adaptiveHeight: true,
      cellAlign: 'center',
      imagesLoaded: true,
      percentPosition: true,
      wrapAround: true,
      lazyLoad: true
    }

    let carouselEnhancementOptions = {
      adaptiveHeight: false,
      contain: true,
      imagesLoaded: true,
      pageDots: false,
      wrapAround: true,
      lazyLoad: true
    }

    let promoCarouselOptions = {
      adaptiveHeight: false,
      contain: true,
      groupCells: true,
      imagesLoaded: true,
      pageDots: true,
      wrapAround: false,
      lazyLoad: true
    }

    let carouselOptions = carouselEnhancementOptions

    if (this.carousel.classList.contains('LargeCarousel-items')) {
      carouselOptions = largeCarouselOptions
    }

    if (this.carousel.classList.contains('ListPartnerCarousel-items') || this.carousel.classList.contains('ListNewsCarousel-items')) {
      carouselOptions = promoCarouselOptions
    }

    this.flickity = new Flickity(this.carousel, carouselOptions)

    this.counter = document.createElement('div')
    this.counter.classList.add('flickity-counter')
    this.counter.innerHTML = `${this.flickity.selectedIndex + 1} of ${this.flickity.slides.length}`

    this.carousel.insertBefore(this.counter, this.carousel.getElementsByClassName('flickity-prev-next-button next')[0])

    this.flickity.on('select', () => {
      this.counter.innerHTML = `${this.flickity.selectedIndex + 1} of ${this.flickity.slides.length}`
    })

    window.addEventListener('load', (evt) => {
      this.flickity.resize()
    })
  }
}

plugins.register(Carousel, '.Carousel, .LargeCarousel-items, .ListLeadCarousel-items, .ListPartnerCarousel-items, .ListNewsCarousel-items', 'complete')
