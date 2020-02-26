export class Banner extends window.HTMLElement {
  connectedCallback () {
    this.closeButton = this.querySelector('[data-toggle-trigger]')

    this.checkSessionStorage()
  }

  checkSessionStorage () {
    if (window.sessionStorage.getItem('bannerStatus') !== 'noShow') {
      this.setAttribute('data-show', '')
      this.closeButton.addEventListener('click', () => {
        this.removeAttribute('data-show')
        window.sessionStorage.setItem('bannerStatus', 'noShow')
      })
    }
  }
}
