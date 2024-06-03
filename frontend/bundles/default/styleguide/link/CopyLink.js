export default class CopyLink extends HTMLElement {
  connectedCallback() {
    this.addEvents()
  }

  addEvents() {
    this.addEventListener('click', () => {
      this.copyLink()
    })
  }

  copyLink() {
    const copyValue = this.getAttribute('data-link')
    navigator.clipboard.writeText(copyValue).then(
      () => {
        this.setAttribute('data-success', true)

        setTimeout(() => {
          this.removeAttribute('data-success')
        }, 2000)
      },
      () => {
        console.error('Unable to write to clipboard')
      }
    )
  }
}
