export class Link {
  constructor(element) {
    this.element = element
    this.init()
  }

  init() {
    this.element.addEventListener('click', (event) => {
      const link = event.currentTarget
      this.onClick(link, event)
    })
  }

  onClick() {
    // Implement in children
  }
}
