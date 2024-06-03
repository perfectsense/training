export default class ProductPromoGallery extends HTMLElement {
  connectedCallback() {
    this.nextSlideButton = this.querySelector('.ProductPromoGallery-next')
    this.previousSlideButton = this.querySelector(
      '.ProductPromoGallery-previous'
    )
    this.slidesContainer = this.querySelector('.ProductPromoGallery-slides')

    this.slidesContainer.addEventListener('scroll', () => {
      this.setState()
    })

    this.nextSlideButton.addEventListener('click', () => {
      this.nextSlide()
    })

    this.previousSlideButton.addEventListener('click', () => {
      this.previousSlide()
    })

    this.setState()
    this.toggleAttribute('data-ready', true)
  }

  setState() {
    this.previousSlideButton.toggleAttribute(
      'disabled',
      this.isScrollAtBeginning()
    )

    this.nextSlideButton.toggleAttribute('disabled', this.isScrollAtEnd())
  }

  isScrollAtBeginning() {
    return this.slidesContainer.scrollLeft === 0
  }

  isScrollAtEnd() {
    return (
      this.slidesContainer.scrollLeft >=
      this.slidesContainer.scrollWidth - this.slidesContainer.clientWidth
    )
  }

  nextSlide() {
    this.slidesContainer.scrollBy({
      left: this.offsetWidth,
      behavior: 'smooth',
    })
  }

  previousSlide() {
    this.slidesContainer.scrollBy({
      left: -this.offsetWidth,
      behavior: 'smooth',
    })
  }
}
