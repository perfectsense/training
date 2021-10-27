export class Banner extends window.HTMLElement {
  connectedCallback () {
    const elementsClose = document.querySelector('.Banner-close')
    if (window.sessionStorage.getItem('banner') == null) {
      this.init()
    }
    elementsClose.addEventListener('click', this.closeBanner, false)
  }

  init () {
    const elementsBanner = document.querySelector('.Banner')
    elementsBanner.setAttribute('data-banner', 'show')
  }

  closeBanner () {
    const elementsBanner = document.querySelector('.Banner')
    elementsBanner.removeAttribute('data-banner')
    elementsBanner.remove()
    window.sessionStorage.setItem('banner', 'hidden')
  }
}
