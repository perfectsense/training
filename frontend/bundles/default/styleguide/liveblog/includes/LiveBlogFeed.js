import { LiveBlogFeedPost } from './LiveBlogFeedPost'
import { events as LiveBlogEvents } from '../LiveBlog'

export default class LiveBlogFeed extends HTMLElement {
  connectedCallback() {
    this.createOrUpdate()

    document.addEventListener(LiveBlogEvents.newPosts, () =>
      this.createOrUpdate()
    )
  }

  async createOrUpdate() {
    await window.customElements.whenDefined('bsp-liveblog-post')

    this.posts = [...document.querySelectorAll('bsp-liveblog-post')]

    this.render()
  }

  render() {
    const days = this.posts.reduce((days, post) => {
      if (!days[post.getdPostedDateFormatted()]) {
        days[post.getdPostedDateFormatted()] = []
      }

      days[post.getdPostedDateFormatted()].push(post)
      return days
    }, {})

    const daysArray = Object.keys(days).map((date) => {
      return {
        date,
        posts: days[date],
      }
    })

    this.innerHTML = ''

    daysArray.forEach((day) => {
      const dateEl = document.createElement('div')
      dateEl.classList.add('LiveBlogFeed-date')
      dateEl.innerText = day.date
      this.appendChild(dateEl)

      day.posts.forEach((post) => {
        this.appendChild(new LiveBlogFeedPost(post))
      })
    })
  }
}
