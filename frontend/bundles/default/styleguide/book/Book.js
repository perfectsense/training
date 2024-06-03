import { events as ChapterEvents } from './Chapter'

export default class Book extends HTMLElement {
  connectedCallback() {
    this.addEventListener('click', this)
    this.addEventListener(ChapterEvents.active, this)
  }

  handleEvent(event) {
    if (event.type === 'click') {
      this.onClick(event)
    }
  }

  onClick(event) {
    if (event.target.matches('.Book-header, .Book-header *')) {
      this.expanded = !this.expanded
    }
  }

  get expanded() {
    return this.hasAttribute('data-expanded')
  }

  set expanded(expanded) {
    this.toggleAttribute('data-expanded', expanded)
  }
}
