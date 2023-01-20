export class GalleryPageCarousel extends window.HTMLElement {
  async connectedCallback() {
    const { default: Flickity } = await import('flickity')

    this.carouselSlides = this.querySelector('[class$="-slides"]') || this
    this.carouselSlide = this.querySelector('[class$="-slides"]') || this
    this.closeButtonEl = this.querySelector('[class$="-closeButton"]') || this

    // waiting until window load to trigger carousels. They still look good before
    // window load, and we have run into race condition and image issues sometimes
    // when we kick it off too fast
    window.addEventListener('load', () => {
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
    })

    this.closeButtonEl.addEventListener('click', (e) => this.onCloseCarousel(e))

    document.addEventListener('keydown', (e) => {
      if (e.key === 'Escape') {
        this.onCloseCarousel(e)
      }
    })

    document.body.addEventListener('GalleryPageCarousel:Open', (data) =>
      this.onOpenCarousel(data, Flickity)
    )
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

  onCloseCarousel(e) {
    e.preventDefault()
    this.removeAttribute('active')

    this.flickity.destroy()
    document.documentElement.style.removeProperty('overflow')
  }

  onOpenCarousel(data, Flickity) {
    this.setAttribute('active', '')

    document.documentElement.style.setProperty('overflow', 'hidden')

    this.flickity = new Flickity(this.carouselSlides, this.carouselOptions)
    this.flickity.resize()
    this.flickity.select(data.detail.index, true, true)
  }

  disconnectedCallback() {
    this.flickity.destroy()
  }
}
