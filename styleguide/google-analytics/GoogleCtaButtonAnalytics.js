/* global ga */
import { GoogleTrackableType } from './GoogleTrackableType'
import { Link } from '../core/link/Link'

export class GoogleCtaButtonAnalytics extends Link {
  constructor (el) {
    super(el)
    this.element = el
    this.document = document
    if (typeof ga === 'undefined') {
      return
    }
  }

  onClick (link, event) {
    GoogleTrackableType.sendEvent({
      category: this.element.getAttribute('data-ga-category') || 'cta button',
      action: this.element.getAttribute('data-ga-action') || 'click',
      label: `${this.element.href} | ${this.document.title}`,
      value: this.element.getAttribute('data-ga-value') || null
    })
  }
}
