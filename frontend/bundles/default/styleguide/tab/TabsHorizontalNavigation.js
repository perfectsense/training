export default class TabsHorizontalNavigation extends HTMLElement {
  #hasOverflow

  connectedCallback() {
    this.contentElement = this.querySelector(
      '.TabsHorizontalNavigation-content'
    )

    this.resizeObserver = new ResizeObserver(() => this.setOverflowState())
    this.resizeObserver.observe(this)
    window.addEventListener('load', () => this.setOverflowState())

    this.previousButton = this.querySelector('[data-click="previous"]')
    this.nextButton = this.querySelector('[data-click="next"]')

    this.contentElement.addEventListener('scroll', () => {
      this.setScrollState()
    })

    this.previousButton.addEventListener('click', () => {
      this.contentElement.scrollBy({
        left: -200,
        behavior: 'smooth',
      })
    })

    this.nextButton.addEventListener('click', () => {
      this.contentElement.scrollBy({
        left: 200,
        behavior: 'smooth',
      })
    })
  }

  setScrollState() {
    if (!this.hasOverflow) {
      this.scrollState = null
    }

    if (this.contentElement.scrollLeft <= 0) {
      this.scrollState = 'start'
      return
    }

    if (
      this.contentElement.scrollWidth -
        this.contentElement.scrollLeft -
        this.contentElement.clientWidth <=
      0
    ) {
      this.scrollState = 'end'
      return
    }

    this.scrollState = 'middle'
  }

  setOverflowState() {
    this.hasOverflow =
      this.contentElement.scrollWidth > this.contentElement.clientWidth

    this.setScrollState()
  }

  set scrollState(scrollState) {
    if (scrollState === null) {
      this.removeAttribute('data-scroll-state')
      return
    }

    this.setAttribute('data-scroll-state', scrollState)
  }

  get hasOverflow() {
    return this.#hasOverflow
  }

  set hasOverflow(hasOverflow) {
    this.#hasOverflow = hasOverflow
    this.toggleAttribute('data-has-overflow', hasOverflow)
  }
}
