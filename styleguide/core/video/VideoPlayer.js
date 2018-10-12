import $ from 'jquery'

const PLAYING_METRICS_INTERVAL = 5

export class VideoPlayer {
  selectors = {
    blockName: 'videoPlayer'
  }

  settings = {
    autoplay: false,
    muted: false,
    seekSeconds: 0
  }

  metrics = {
    playbackPosition: 0,
    quartilePosition: -1
  }

  playerId = null

  /** returns JSON data for current video  **/
  get video () {
    return {
      id: this.settings.videoId,
      title: this.settings.videoTitle,
      duration: this.videoDuration
    }
  }

  get videoDuration () {
    return -1 // abstract: implement in child class
  }

  constructor (el) {
    this.$el = $(el)

    this.playerId = this.$el.attr('data-player-id')
    this.$body = $('body')

    this.settings.videoId = this.$el.attr('data-video-id')
    this.settings.videoTitle = this.$el.attr('data-video-title')
    this.settings.autoplay = this.$el.is('[data-autoplay]')
    this.settings.muted = this.$el.is('[data-muted]')
    this.settings.seekSeconds = this.$el.attr('data-seek-seconds') || 0
    this.settings.dfpUrl = this.$el.attr('data-dfp-url')
  }

  init () {
    this.$el.on('click', '[data-card]', (event) => {
      this.$el.data('player-instance').play()
    })
    this.$el.removeAttr('data-playback-started')

    this.$el.data('player-instance', this)
  }

  onVideoReady (event) {
    this.$el.trigger('Video:onVideoLoaded', {
      playerId: this.playerId,
      video: this.video
    })
  }

  onVideoStart (event) {
    this.resetMetrics()

    this.$el.attr('data-playback-started', '')
    this.$el.trigger('Video:onVideoPlaybackStarted', {
      playerId: this.playerId,
      video: this.video
    })
  }

  onVideoTimeUpdate (event) {
    const secondsElapsed = event.secondsElapsed

    this.$el.trigger('Video:onVideoPlaybackTimeUpdate', {
      playerId: this.playerId,
      video: this.video,
      secondsElapsed: secondsElapsed
    })

    this.sendVideoPlaybackMetricEvents(secondsElapsed)
  }

  onVideoStop (event) {
    this.$el.trigger('Video:onVideoPlaybackStopped', {
      playerId: this.playerId,
      video: this.video
    })
  }

  onVideoMute (event) {
    this.$el.attr('data-muted', '')
    this.$el.trigger('Video:onVideoPlaybackMuted', {
      playerId: this.playerId,
      video: this.video
    })
  }

  onVideoUnMute (event) {
    this.$el.removeAttr('data-muted')
    this.$el.trigger('Video:onVideoPlaybackUnMuted', {
      playerId: this.playerId,
      video: this.video
    })
  }

  onVideoPause (event) {
    this.$el.attr('data-playback-paused', '')
    this.$el.trigger('Video:onVideoPlaybackPaused', {
      playerId: this.playerId,
      video: this.video
    })
  }

  onVideoUnPause (event) {
    this.$el.removeAttr('data-playback-paused')
    this.$el.trigger('Video:onVideoPlaybackUnPaused', {
      playerId: this.playerId,
      video: this.video
    })
  }

  onVideoEnd (event) {
    this.$el.trigger('Video:onVideoEnded', {
      playerId: this.playerId,
      video: this.video
    })
  }

  play () {
    // abstract: implement in child class
  }

  pause () {
    // abstract: implement in child class
  }

  playPause () {
    if (this.$el.is('[data-playback-paused]')) {
      this.play()
    } else {
      this.pause()
    }
  }

  resetMetrics () {
    this.metrics = {
      playbackPosition: 0,
      quartilePosition: -1
    }
  }

  sendVideoPlaybackMetricEvents (secondsElapsed) {
    const video = this.video
    const duration = this.videoDuration
    const percentComplete = (secondsElapsed / duration) * 100

    // don't send extra events if the video didn't naturally pass the interval
    while (secondsElapsed > this.metrics.playbackPosition + PLAYING_METRICS_INTERVAL * 2) {
      this.metrics.playbackPosition += PLAYING_METRICS_INTERVAL
    }

    if (secondsElapsed >= this.metrics.playbackPosition + PLAYING_METRICS_INTERVAL) {
      this.metrics.playbackPosition += PLAYING_METRICS_INTERVAL
      this.$el.trigger('Video:onVideoTimeIntervalUpdate', {
        playerId: this.playerId,
        video,
        secondsElapsed: this.metrics.playbackPosition
      })
    }

    let quartileEvent = null
    if (percentComplete >= this.metrics.quartilePosition) {
      // when resuming, make sure we don't repeat quartile events
      // once percentile updates correctly after seek, figure out next valid event position
      if (this.metrics.quartilePosition === -1) {
        if (percentComplete >= this.metrics.quartilePosition + 25) {
          this.metrics.quartilePosition = 0
          while (percentComplete >= this.metrics.quartilePosition) {
            this.metrics.quartilePosition += 25
          }
        }
      } else {
        quartileEvent = this.metrics.quartilePosition

        this.metrics.quartilePosition += 25
        if (this.metrics.quartilePosition === 100) {
          this.metrics.quartilePosition = 95
        }
      }
    }

    if (quartileEvent !== null) {
      // to ensure 100% is always reached, call at 95% instead
      if (quartileEvent === 95) {
        quartileEvent = 100
      }

      this.$el.trigger('Video:onVideoQuartileCompleted', {
        playerId: this.playerId,
        video,
        secondsElapsed: this.metrics.playbackPosition,
        quartile: quartileEvent
      })
    }
  }

  updateView ($newVideo) {
    this.settings.videoId = $newVideo.attr('data-video-id')
    this.settings.videoTitle = $newVideo.attr('data-video-title')
    this.settings.dfpUrl = this.$el.attr('data-dfp-url')
  }
}
