export class Link {
  constructor (element) {
    this.element = element
    this.init()
  }

  init () {
    this.element.addEventListener('click', event => {
      let link = event.currentTarget
      this.onClick(link, event)
    })
  }

  onClick (link, event) {
    // Implement in children
  }
}
