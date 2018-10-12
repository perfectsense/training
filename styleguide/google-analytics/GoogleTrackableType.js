/* global ga */

export class GoogleTrackableType {
  static sendEvent (data) {
    if (data.category && data.action) {
      ga('send', 'event', data.category, data.action, data.label || null, data.value || null)
    }
  }
}
