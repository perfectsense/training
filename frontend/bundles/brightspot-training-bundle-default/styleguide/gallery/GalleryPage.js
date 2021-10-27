export class GalleryPage extends window.HTMLElement {
  get galleryStyle () {
    return this.dataset.galleryStyle
  }

  get headerHeight () {
    return document.querySelector('.Page-header').offsetHeight
  }

  get slides () {
    return this.querySelectorAll('.GallerySlideWaterfall img')
  }

  connectedCallback () {
    this.backToTop = this.querySelector('[class$="-backToTop"]')
    this.handleEventListeners()
  }

  handleEventListeners () {
    window.addEventListener('load', () => {
      if (this.galleryStyle === 'waterfall') {
        this.slideClickEvent()

        window.addEventListener('scroll', () => {
          window.pageYOffset > this.headerHeight
            ? (this.backToTop.style.display = 'block')
            : (this.backToTop.style.display = 'none')
        })

        this.backToTop.addEventListener('click', e => {
          e.preventDefault()
          window.scroll({
            top: 0,
            left: 0,
            behavior: 'smooth'
          })
        })
      }
    })
  }

  slideClickEvent () {
    this.slides.forEach(slide => {
      slide.addEventListener('click', e => {
        e.preventDefault()
        openSlide(slide)
      })

      // for accessibility
      slide.addEventListener('keyup', e => {
        e.preventDefault()
        if (e.key === 'Enter') {
          openSlide(slide)
          document.querySelector('[class$="-closeButton"]').focus()
        }
      })
    })

    const openSlide = slide => {
      const slideIndex = slide.closest('[data-slide-index]').dataset.slideIndex
      document.body.dispatchEvent(
        new window.CustomEvent('GalleryPageCarousel:Open', {
          detail: {
            index: slideIndex
          }
        })
      )
    }
  }
}
