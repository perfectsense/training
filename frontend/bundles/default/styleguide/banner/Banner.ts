export default class Banner extends HTMLElement {
  connectedCallback() {
    const elementsClose = this.querySelector('.Banner-close')
    if (window.sessionStorage.getItem('banner') == null) {
      this.init()
    }
    if (elementsClose) {
      elementsClose.addEventListener('click', this, false)
    }
  }

  handleEvent(event: Event) {
    if (event.type === 'click') {
      this.closeBanner()
    }
  }

  init() {
    this.setAttribute('data-banner', 'show')
  }

  closeBanner() {
    this.removeAttribute('data-banner')
    this.remove()
    window.sessionStorage.setItem('banner', 'hidden')
  }
}
