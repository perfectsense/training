import Flickity from 'flickity'

export default class GalleryFullscreen extends HTMLElement {
  connectedCallback() {
    this.carouselSlides = this.querySelector('.GalleryFullscreen-slides')
    this.carouselSlide = this.querySelector('.GalleryFullscreen-slides')
    this.closeButtonEl = this.querySelector('.GalleryFullscreen-closeButton')
    this.dialog = this.closest('dialog')

    this.carouselOptions = {
      adaptiveHeight: false,
      wrapAround: true,
      prevNextButtons: true,
      pageDots: false,
      draggable: true,
      lazyLoad: 2,
      on: {
        change: (index) => {
          this.onSlideChange(index)
        },
        select: (index) => {
          this.onSlideChange(index)
        },
      },
    }

    this.closeButtonEl.addEventListener('click', (event) => {
      event.preventDefault()
      this.dialog.close()
    })

    document.body.addEventListener('GallerySlide:click', (event) => {
      const targetCarousel = event.target.closest('bsp-carousel')
      const parentCarousel = this.closest('bsp-carousel')

      // Slides on gallery pages using the waterfall layout
      // don't have a bsp-carousel parent.
      if (!targetCarousel && !parentCarousel) {
        this.onOpenFullscreen(event)
        return
      }

      if (targetCarousel === parentCarousel) {
        this.onOpenFullscreen(event)
      }
    })

    this.dialog.addEventListener('close', () => this.onCloseFullscreen())
  }

  onSlideChange(index) {
    if (this.querySelector('[class$="-slide"][active]')) {
      this.querySelector('[class$="-slide"][active]').removeAttribute('active')
    }

    this.querySelector('[data-slide-id="slide-' + index + '"]').setAttribute(
      'active',
      ''
    )
  }

  onCloseFullscreen() {
    this.flickity.destroy()
    document.documentElement.style.removeProperty('overflow')
  }

  onOpenFullscreen(data) {
    document.documentElement.style.setProperty('overflow', 'hidden')
    this.dialog.showModal()

    this.flickity = new Flickity(this.carouselSlides, this.carouselOptions)
    this.flickity.resize()
    this.flickity.select(data.detail.index, true, true)
  }

  disconnectedCallback() {
    this.flickity.destroy()
  }
}
