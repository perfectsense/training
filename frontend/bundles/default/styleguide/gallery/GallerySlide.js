export default class GallerySlide extends HTMLElement {
  get slideIndex() {
    return this.dataset.slideIndex
  }

  get carousel() {
    return this.closest('bsp-carousel')
  }

  connectedCallback() {
    this.handleEventListeners()
  }

  handleEventListeners() {
    this.slideClickEvent()
  }

  slideClickEvent() {
    this.carousel?.addEventListener('Carousel:FlickityStaticClick', (event) => {
      console.log(event.detail)
      if (!event.detail.cellElement.contains(this)) return
      this.onSlideClick()
    })

    if (!this.carousel) {
      this.addEventListener('click', (event) => {
        event.preventDefault()
        this.onSlideClick()
      })
    }

    this.addEventListener('keydown', (event) => {
      if (event.key === 'Enter') {
        this.onSlideClick()
      }
    })
  }

  onSlideClick() {
    this.dispatchEvent(
      new window.CustomEvent('GallerySlide:click', {
        detail: {
          index: this.slideIndex,
        },
        bubbles: true,
      })
    )
  }
}
