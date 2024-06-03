export default class Accordion extends HTMLElement {
  headerElement: HTMLElement | null = null
  bodyElement: HTMLElement | null = null

  static get observedAttributes() {
    return ['data-expanded']
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

  attributeChangedCallback(name: string) {
    if (name === 'data-expanded') {
      this.toggle()
    }
  }

  connectedCallback() {
    this.headerElement = this.querySelector('[data-accordion-header]')
    this.bodyElement = this.querySelector('[data-accordion-body]')

    if (this.headerElement) {
      this.headerElement.addEventListener('click', (event) => {
        event.preventDefault()
        this.expanded = !this.expanded
      })
    }

    if (this.expanded && this.bodyElement) {
      this.bodyElement.style.height = 'auto'
    }
  }

  transition(startHeight: number, endHeight: number, visibility: string) {
    if (this.bodyElement) {
      this.bodyElement.style.height = `${startHeight}px`
    }

    // Double requestAnimationFrame guarantees transition occurs
    window.requestAnimationFrame(() => {
      window.requestAnimationFrame(() => {
        if (this.bodyElement) {
          this.bodyElement.style.height = `${endHeight}px`
          this.bodyElement.style.visibility = visibility
        }
      })
    })
  }

  toggle() {
    if (this.bodyElement) {
      const contentHeight = this.bodyElement.scrollHeight

      if (this.expanded) {
        this.transition(0, contentHeight, 'visible')
      } else {
        this.transition(contentHeight, 0, 'hidden')
      }
    }
  }
}
