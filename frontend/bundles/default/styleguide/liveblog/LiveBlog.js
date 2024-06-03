// standard interval for delta detection
const interval = 15000
// error interval to prevent server spamming
const errorInterval = 300000

let CONTENT_DELTA_RANGE = interval

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
  newPosts: 'LiveBlog:newPosts',
}

export default class LiveBlog extends HTMLElement {
  connectedCallback() {
    this.postsEndpoint = this.getAttribute('data-posts-endpoint')
    this.live = this.hasAttribute('data-live-event')
    this.currentPostsEl = document.querySelector('.LiveBlogPage-currentPosts')

    if (!this.live) return

    this.createInterval()
  }

  async getNewPosts() {
    let response
    let responseCode = 200
    const searchParams = new window.URLSearchParams()
    const mostRecentPostEpoch = getMostRecentPostEpoch()
    if (mostRecentPostEpoch) {
      searchParams.append('since', mostRecentPostEpoch)
    }
    searchParams.append('_', getContentDeltaEpoch())
    const url = `${this.postsEndpoint}?${searchParams}`

    try {
      const fetch = await window.fetch(url)
      response = await fetch.text()
      responseCode = fetch.status
    } catch (error) {
      // if there is a weird blow up error, increase the delta range to avoid spamming the server
      CONTENT_DELTA_RANGE = errorInterval
      this.resetInterval()

      return console.error('Error getting new posts', error)
    }

    if (responseCode !== 200) {
      // if there is a server error code (404/500/etc), increase the delta range to avoid spamming the server
      CONTENT_DELTA_RANGE = errorInterval
      this.resetInterval()

      return console.error('Error getting new posts', responseCode)
    }

    // if necessary, reset the delta range
    if (CONTENT_DELTA_RANGE !== interval) {
      CONTENT_DELTA_RANGE = interval
      this.resetInterval()
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
    }
  }

  createInterval() {
    this.interval = setInterval(() => {
      this.getNewPosts()
    }, CONTENT_DELTA_RANGE)
  }

  clearInterval() {
    clearInterval(this.interval)
  }

  resetInterval() {
    this.clearInterval()
    this.createInterval()
  }

  emit(event, detail = {}) {
    const customEvent = new window.CustomEvent(event, {
      bubbles: true,
      detail,
    })
    this.dispatchEvent(customEvent)
  }
}
