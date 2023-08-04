export class Banner extends window.HTMLElement {
  connectedCallback() {
    const elementsClose = this.querySelector('.Banner-close')
    if (window.sessionStorage.getItem('banner') == null) {
      this.init()
    }
    elementsClose.addEventListener('click', this, false)
  }

  handleEvent(event) {
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
