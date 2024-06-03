import { Timer } from '../../util/Timer'

export default class PageListAutoRotate extends HTMLElement {
  connectedCallback() {
    this.slidesContainer = this.querySelector('[data-slides]')
    this.slideItems = [...this.querySelectorAll('[data-slide]')]
    this.navigationItems = [...this.querySelectorAll('[data-navigation-item]')]
    this.carouselSpeed = +this.getAttribute('data-carousel-speed') * 1000
    this.autoPlay = this.hasAttribute('data-carousel-auto-play')
    this.style.setProperty('--carouselSpeed', `${this.carouselSpeed}ms`)
    this.bindEvents()
    this.selectSlide(0)
  }

  bindEvents() {
    this.slidesContainer.addEventListener('mouseenter', () => {
      this.setAttribute('data-paused', '')

      if (this.timer) {
        this.timer.pause()
      }
    })

    this.slidesContainer.addEventListener('mouseleave', () => {
      this.removeAttribute('data-paused')

      if (this.timer) {
        this.timer.resume()
      }
    })

    this.navigationItems.forEach((navigationItem, index) => {
      navigationItem.addEventListener('click', () => {
        this.selectSlide(index)
      })
    })
  }

  nextSlide() {
    const currentIndex = this.slideItems.findIndex((slide) => {
      return slide.hasAttribute('data-active')
    })

    this.selectSlide((currentIndex + 1) % this.slideItems.length)
  }

  selectSlide(index) {
    this.slideItems.forEach((slide, slideIndex) => {
      if (index === slideIndex) {
        slide.setAttribute('data-active', '')
      } else {
        slide.removeAttribute('data-active')
      }
    })

    this.navigationItems.forEach((slide, slideIndex) => {
      if (index === slideIndex) {
        slide.setAttribute('data-active', '')
      } else {
        slide.removeAttribute('data-active')
      }
    })

    if (this.timer) {
      this.timer.cancel()
    }

    if (this.autoPlay) {
      this.timer = new Timer(() => {
        this.nextSlide()
      }, this.carouselSpeed)
    }
  }
}
