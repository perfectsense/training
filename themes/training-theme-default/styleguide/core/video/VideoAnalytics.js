export class VideoAnalytics {
  constructor () {
    document.body.addEventListener('Video:onVideoPlaybackStarted', event => {
      this.onVideoStarted(event)
    })

    document.body.addEventListener('Video:onVideoEnded', event => {
      this.onVideoEnded(event)
    })

    document.body.addEventListener('Video:onVideoQuartileCompleted', event => {
      this.onVideoQuartileCompleted(event)
    })

    document.body.addEventListener('Video:onVideoTimeIntervalUpdate', event => {
      this.onVideoTimeUpdate(event)
    })
  }

  onVideoStarted (event) {
    this.sendEvent({
      action: 'Started',
      title: event.detail.video.title
    })
  }

  onVideoEnded (event) {
    this.sendEvent({
      action: 'Ended',
      title: event.detail.video.title
    })
  }

  onVideoTimeUpdate (event) {
    this.sendEvent({
      action: 'Time update',
      title: event.detail.video.title,
      value: event.detail.secondsElapsed
    })
  }

  onVideoQuartileCompleted (event) {
    let action = null
    switch (event.detail.quartile) {
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
        title: event.detail.video.title,
        value: event.detail.quartile
      })
    }
  }

  sendEvent (event) {
    // implement in child class
  }
}
