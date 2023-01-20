export class Accordion extends HTMLElement {
  static get observedAttributes() {
    return ['data-expanded']
  }

  get headerElement() {
    return this.querySelector('[data-accordion-header]')
  }

  get bodyElement() {
    return this.querySelector('[data-accordion-body]')
  }

  get expanded() {
    return this.hasAttribute('data-expanded')
  }

  set expanded(bool) {
    if (bool) {
      this.setAttribute('data-expanded', '')
    } else {
      this.removeAttribute('data-expanded')
    }
  }

  attributeChangedCallback(name) {
    if (name === 'data-expanded') {
      this.toggle()
    }
  }

  connectedCallback() {
    this.headerElement.addEventListener('click', (event) => {
      event.preventDefault()
      this.expanded = !this.expanded
    })

    if (this.expanded) {
      this.bodyElement.style.height = 'auto'
    }
  }

  transition(startHeight, endHeight, visibility) {
    this.bodyElement.style.height = `${startHeight}px`

    // Double requestAnimationFrame guarantees transition occurs
    window.requestAnimationFrame(() => {
      window.requestAnimationFrame(() => {
        this.bodyElement.style.height = `${endHeight}px`
        this.bodyElement.style.visibility = visibility
      })
    })
  }

  toggle() {
    const contentHeight = this.bodyElement.scrollHeight

    if (this.expanded) {
      this.transition(0, contentHeight, 'visible')
    } else {
      this.transition(contentHeight, 0, 'hidden')
    }
  }
}
