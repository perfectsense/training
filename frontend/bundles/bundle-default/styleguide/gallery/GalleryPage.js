export class GalleryPage extends window.HTMLElement {
  get galleryStyle() {
    return this.dataset.galleryStyle
  }

  get headerHeight() {
    return document.querySelector('.Page-header').offsetHeight
  }

  get slides() {
    return this.querySelectorAll('.GallerySlideWaterfall img')
  }

  get fullScreenButtons() {
    return this.querySelectorAll('[class$="-fullScreenButton"]')
  }

  connectedCallback() {
    this.handleEventListeners()
  }

  handleEventListeners() {
    window.addEventListener('load', () => {
      if (this.galleryStyle === 'waterfall') {
        this.slideClickEvent()
      }
    })
  }

  slideClickEvent() {
    this.slides.forEach((slide) => {
      slide.addEventListener('click', (e) => {
        e.preventDefault()
        openSlide(slide)
      })

      // for accessibility
      slide.addEventListener('keyup', (e) => {
        e.preventDefault()
        if (e.key === 'Enter') {
          openSlide(slide)
          document.querySelector('[class$="-closeButton"]').focus()
        }
      })
    })

    this.fullScreenButtons.forEach((button) => {
      button.addEventListener('click', (e) => {
        const slide = button.nextElementSibling.querySelector(
          '.GallerySlideWaterfall img'
        )
        e.preventDefault()
        openSlide(slide)
      })
    })

    const openSlide = (slide) => {
      const slideIndex = slide.closest('[data-slide-index]').dataset.slideIndex
      document.body.dispatchEvent(
        new window.CustomEvent('GalleryPageCarousel:Open', {
          detail: {
            index: slideIndex,
          },
        })
      )
    }
  }
}
