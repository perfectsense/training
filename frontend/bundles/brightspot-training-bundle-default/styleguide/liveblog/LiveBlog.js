import { LiveBlogNewPostLink } from './includes/LiveBlogNewPostLink'

// Limit delta detection to intervals of 15 seconds
const CONTENT_DELTA_RANGE = 15000

// Returns the epoch time of the nearest 15 interval
const getContentDeltaEpoch = () => {
  const epochNow = new window.Date().getTime()
  return epochNow - (epochNow % CONTENT_DELTA_RANGE)
}

// Retruns the epoch time of the most recent post
const getMostRecentPostEpoch = () => {
  const posts = [...document.querySelectorAll('.LiveBlogPost')]

  return posts.reduce((acc, post) => {
    const timestamp = +post.getAttribute('data-posted-date-timestamp')
    return timestamp > acc ? timestamp : acc
  }, null)
}

export const events = {
  fetched: 'LiveBlog:fetched',
  newPosts: 'LiveBlog:newPosts'
}

export class LiveBlog extends window.HTMLElement {
  connectedCallback () {
    this.postsEndpoint = this.getAttribute('data-posts-endpoint')
    this.live = this.hasAttribute('data-live-event')
    this.currentPostsEl = document.querySelector('.LiveBlogPage-currentPosts')

    if (!this.live) return

    setInterval(() => {
      this.getNewPosts()
    }, CONTENT_DELTA_RANGE)
  }

  async getNewPosts () {
    let response
    const searchParams = new window.URLSearchParams()
    const mostRecentPostEpoch = getMostRecentPostEpoch()
    if (mostRecentPostEpoch) {
      searchParams.append('since', mostRecentPostEpoch)
    }
    searchParams.append('_', getContentDeltaEpoch())
    const url = `${this.postsEndpoint}?${searchParams}`

    try {
      response = await window.fetch(url).then(resopnse => resopnse.text())
    } catch (error) {
      return console.error('Error getting new posts', error)
    }

    const parser = new window.DOMParser()
    const newDocument = parser.parseFromString(response, 'text/html')
    const newContent = newDocument
      .querySelector('.LiveBlogPage-currentPosts')
      .innerHTML.trim()

    if (newContent) {
      this.currentPostsEl.insertAdjacentHTML('afterbegin', newContent)
      this.emit(events.newPosts)
      this.emit('Ajax:Rendered')

      const mostRecentPost = this.currentPostsEl.querySelector(
        'bsp-liveblog-post'
      )
      this.createOrUpdateNewPostLink(mostRecentPost)
    }
  }

  emit (event, detail = {}) {
    const customEvent = new window.CustomEvent(event, {
      bubbles: true,
      detail: detail
    })
    this.dispatchEvent(customEvent)
  }

  createOrUpdateNewPostLink (post) {
    if (this.newPostLink) {
      this.newPostLink.updatePost(post)
    } else {
      this.newPostLink = new LiveBlogNewPostLink(
        post,
        this.getAttribute('data-latest-update-text'),
        () => {
          this.newPostLink = null
        }
      )
      document.body.appendChild(this.newPostLink)
    }
  }
}
