import { getLanguage } from '../util/getLanguage'
import { events as LiveBlogEvents } from './LiveBlog'

export class LiveBlogPost extends window.HTMLElement {
  get postedDateTimestamp () {
    return +this.getAttribute('data-posted-date-timestamp')
  }

  get postedDate () {
    return new Date(this.postedDateTimestamp)
  }

  get isCurrentDay () {
    return this.getAttribute('data-is-current-day')
  }

  set isCurrentDay (bool) {
    bool
      ? this.setAttribute('data-is-current-day', '')
      : this.removeAttribute('data-is-current-day')
  }

  get headline () {
    return this.querySelector('.LiveBlogPost-headline').innerText
  }

  get id () {
    return this.getAttribute('data-post-id')
  }

  connectedCallback () {
    this.timeElement = this.querySelector('.LiveBlogPost-time')
    this.dateElement = this.querySelector('.LiveBlogPost-date')
    this.render()

    document.addEventListener(LiveBlogEvents.fetched, () => this.render())
  }

  getdPostedTimeFormatted () {
    const deltaMiliseconds = new Date() - this.postedDate

    if (deltaMiliseconds < 1000 * 60) {
      const seconds = (deltaMiliseconds / 1000).toFixed(0)
      const deltaText = this.getAttribute('data-seconds-delta-text')
      const formatted = `${seconds}${deltaText}`
      return formatted
    }

    if (deltaMiliseconds < 1000 * 60 * 60) {
      const minutes = (deltaMiliseconds / (1000 * 60)).toFixed(0)
      const deltaText = this.getAttribute('data-minutes-delta-text')
      const formatted = `${minutes}${deltaText}`
      return formatted
    }

    if (deltaMiliseconds < 1000 * 60 * 60 * 3) {
      const hours = (deltaMiliseconds / (1000 * 60 * 60)).toFixed(0)
      const deltaText = this.getAttribute('data-hours-delta-text')
      const formatted = `${hours}${deltaText}`
      return formatted
    }

    return this.postedDate.toLocaleString(getLanguage(), {
      hour: 'numeric',
      minute: 'numeric',
      hour12: true
    })
  }

  getdPostedDateFormatted () {
    return this.postedDate.toLocaleString(getLanguage(), {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    })
  }

  render () {
    this.timeElement.innerText = this.getdPostedTimeFormatted()
    this.dateElement.innerText = this.getdPostedDateFormatted()

    const currentDay = new Date()
    currentDay.setHours(0, 0, 0)

    this.isCurrentDay = this.postedDate > currentDay
  }

  scrollIntoView () {
    // Scroll to the post's anchor elmeent which is styled to control offset.
    const anchor = this.querySelector('.LiveBlogPost-anchor')
    const options = {
      behavior: 'smooth',
      ...arguments
    }
    anchor.scrollIntoView(options)
  }
}
