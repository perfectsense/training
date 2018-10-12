/* global ga */

import {VideoAnalytics} from 'videoAnalytics'

export class GoogleVideoAnalytics extends VideoAnalytics {
  sendEvent (data) {
    if (data.action) {
      ga('send', 'event', 'video', data.action, data.title || null, data.value || null)
    }
  }
}
