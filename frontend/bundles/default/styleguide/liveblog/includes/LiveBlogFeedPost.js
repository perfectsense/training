export class LiveBlogFeedPost extends HTMLElement {
  constructor(post) {
    super(post)
    this.post = post
  }

  connectedCallback() {
    this.classList.add('LiveBlogFeed-post')
    this.innerHTML = `
        <div class="LiveBlogFeed-post-time">
          ${this.post.getdPostedTimeFormatted()}
        </div>
        <a href="#${this.post.id}" class="LiveBlogFeed-post-headline">
          ${this.post.headline}
        </a>
        `.trim()

    this.addEventListener('click', (event) => {
      event.preventDefault()
      window.history.pushState(null, null, `#${this.post.id}`)
      this.post.scrollIntoView()
    })
  }
}

window.customElements.define('bsp-liveblog-feed-post', LiveBlogFeedPost)
