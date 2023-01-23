export class LiveBlogNewPostLink extends window.HTMLElement {
  constructor(post, linkText, onDisconnectedCallback) {
    super(post, onDisconnectedCallback)
    this.post = post
    this.linkText = linkText
    this.onDisconnectedCallback = onDisconnectedCallback
  }

  updatePost(post) {
    this.observer.unobserve(this.post)
    this.post = post
    this.observer.observe(this.post)
  }

  connectedCallback() {
    this.classList.add('LiveBlogNewPostLink')
    this.postLink = document.createElement('a')
    this.postLink.insertAdjacentHTML(
      'beforeend',
      '<svg class="icon-arrow-down"><use xlink:href="#icon-arrow-down"></use></svg>'
    )
    this.postLink.insertAdjacentText('beforeend', this.linkText)
    this.postLink.href = `#${this.post.id}`
    this.postLink.classList.add('LiveBlogNewPostLink-link')
    this.appendChild(this.postLink)

    this.dismissLink = document.createElement('a')
    this.dismissLink.insertAdjacentHTML(
      'beforeend',
      '<svg><use xlink:href="#close-x"></use></svg>'
    )
    this.dismissLink.href = '#'
    this.dismissLink.classList.add('LiveBlogNewPostLink-dismissLink')
    this.appendChild(this.dismissLink)

    this.postLink.addEventListener('click', (event) => {
      event.preventDefault()
      window.history.pushState(null, null, `#${this.post.id}`)
      this.post.scrollIntoView()
    })

    this.dismissLink.addEventListener('click', (event) => {
      event.preventDefault()
      this.remove()
    })

    this.observer = new window.IntersectionObserver(this.handleIntersect)
    this.observer.observe(this.post)
  }

  handleIntersect = (entries, observer) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        observer.unobserve(entry.target)
        this.remove()
      }
    })
  }

  disconnectedCallback() {
    this.onDisconnectedCallback()
  }
}

window.customElements.define('bsp-liveblog-new-post-link', LiveBlogNewPostLink)
