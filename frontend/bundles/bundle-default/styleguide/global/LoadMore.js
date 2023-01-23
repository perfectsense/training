export class LoadMore extends HTMLElement {
  static events = {
    loadedContent: 'LoadMore:loadedContent',
  }

  get url() {
    return new URL(this.getAttribute('data-url'), location.origin)
  }

  connectedCallback() {
    this.querySelector('button').addEventListener('click', (event) => {
      event.preventDefault()
      this.loadMore()
    })
  }

  loadMore() {
    if (this.hasAttribute('data-loading')) return
    this.setAttribute('data-loading', '')

    window
      .fetch(this.url, { credentials: 'include' })
      .then((response) => {
        return response.text()
      })
      .then((text) => {
        const parser = new DOMParser()
        const newDocument = parser.parseFromString(text, 'text/html')
        const event = new CustomEvent(LoadMore.events.loadedContent, {
          bubbles: true,
          detail: newDocument,
        })
        this.dispatchEvent(event)
      })
      .catch(console.log)
  }
}
