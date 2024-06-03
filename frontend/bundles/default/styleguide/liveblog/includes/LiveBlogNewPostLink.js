import { events as LiveBlogEvents } from '../LiveBlog'

export default class LiveBlogNewPostLink extends HTMLElement {
  updatePost(post) {
    this.post = post
    this.postLink.href = `#${this.post.id}`

    if (this.observer) {
      this.observer.disconnect()
      this.observer = null
    }
    this.observer = new IntersectionObserver(this.handleIntersect)
    this.observer.observe(this.post)
  }

  connectedCallback() {
    this.postLink = this.querySelector('.LiveBlogNewPostLink-link')
    this.dismissButton = this.querySelector(
      '.LiveBlogNewPostLink-dismissButton'
    )

    document.addEventListener(LiveBlogEvents.newPosts, () => {
      const mostRecentPost = document.querySelector(
        '.LiveBlogPage-currentPosts bsp-liveblog-post'
      )
      this.updatePost(mostRecentPost)
    })

    this.postLink.addEventListener('click', (event) => {
      event.preventDefault()
      history.pushState(null, null, `#${this.post.id}`)
      this.post.scrollIntoView()
    })

    this.dismissButton.addEventListener('click', (event) => {
      event.preventDefault()
      this.postLink.removeAttribute('href')
    })
  }

  handleIntersect = (entries, observer) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        observer.unobserve(entry.target)
        this.postLink.removeAttribute('href')
      }
    })
  }
}
