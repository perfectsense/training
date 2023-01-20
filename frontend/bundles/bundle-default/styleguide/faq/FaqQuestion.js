export class FaqQuestion extends HTMLElement {
  static get observedAttributes() {
    return ['data-expanded']
  }

  get headerEl() {
    return this.querySelector('.FaqQuestion-header')
  }

  get answerEl() {
    return this.querySelector('.FaqQuestion-answer')
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
    this.headerEl.addEventListener('click', (event) => {
      event.preventDefault()
      this.expanded = !this.expanded
    })

    if (this.expanded) {
      this.answerEl.style.height = 'auto'
    }
  }

  transition(startHeight, endHeight) {
    this.answerEl.style.height = `${startHeight}px`

    // Double requestAnimationFrame guarantees transition occurs
    window.requestAnimationFrame(() => {
      window.requestAnimationFrame(() => {
        this.answerEl.style.height = `${endHeight}px`
      })
    })
  }

  toggle() {
    // + 20 for the 20 px of padding at the bottom of the text
    const contentHeight = this.answerEl.scrollHeight + 20

    if (this.expanded) {
      this.transition(0, contentHeight)
    } else {
      this.transition(contentHeight, 0)
    }
  }
}
