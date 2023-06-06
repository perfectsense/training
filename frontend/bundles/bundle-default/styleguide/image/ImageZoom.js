export class ImageZoom extends HTMLElement {
  static get observedAttributes() {
    return ['data-zoomed']
  }

  get pictureEl() {
    return this.querySelector('picture')
  }

  get zoomed() {
    return this.hasAttribute('data-zoomed')
  }

  set zoomed(bool) {
    if (bool) {
      this.setAttribute('data-zoomed', '')
    } else {
      this.removeAttribute('data-zoomed')
    }
  }

  connectedCallback() {
    const zoomButtonEl = document.createElement('button')
    zoomButtonEl.classList.add('bsp-imagezoom__button')
    zoomButtonEl.addEventListener('click', (event) => {
      event.preventDefault()
      this.zoomed = !this.zoomed
    })
    const pictureElClone = this.pictureEl.cloneNode(true)
    pictureElClone.classList.add('clone')
    pictureElClone.appendChild(zoomButtonEl)
    this.appendChild(pictureElClone)
  }
}
