/* global ga */

import $ from 'jquery'

export class VideoAnalytics {
  constructor () {
    this.$body = $('body')

    if (typeof ga === 'undefined' || !$.isFunction(ga)) {
      return
    }

    // attach events
    this.$body.on('Video:onVideoPlaybackStarted', (event, data) => {
      this.onVideoStarted(data)
    })

    this.$body.on('Video:onVideoEnded', (event, data) => {
      this.onVideoEnded(data)
    })

    this.$body.on('Video:onVideoQuartileCompleted', (event, data) => {
      this.onVideoQuartileCompleted(data)
    })

    this.$body.on('Video:onVideoTimeIntervalUpdate', (event, data) => {
      this.onVideoTimeUpdate(data)
    })
  }

  onVideoStarted (data) {
    this.sendEvent({
      action: 'Started',
      title: data.video.title
    })
  }

  onVideoEnded (data) {
    this.sendEvent({
      action: 'Ended',
      title: data.video.title
    })
  }

  onVideoTimeUpdate (data) {
    this.sendEvent({
      action: 'Time update',
      title: data.video.title,
      value: data.secondsElapsed
    })
  }

  onVideoQuartileCompleted (data) {
    let action = null
    switch (data.quartile) {
      case 25: {
        action = 'Video 25% Completed'
        break
      }
      case 50: {
        action = 'Video 50% Completed'
        break
      }
      case 75: {
        action = 'Video 75% Completed'
        break
      }
      case 100: {
        action = 'Video 100% Completed'
        break
      }
    }

    if (action) {
      this.sendEvent({
        action: action,
        title: data.video.title,
        value: data.quartile
      })
    }
  }

  sendEvent (data) {
    // abstract: implement in child class
  }
}
