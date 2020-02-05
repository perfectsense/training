/**
 * This class gets extended by all the child players. The child players have their own Listeners
 * to the video actions depending on what the video technology is used and call the super functions to actually
 * dispatch these events so things like the video page or analytics can capture them
 */

export class VideoPlayer extends window.HTMLElement {
  constructor () {
    super()

    this.settings = {
      autoplay: false,
      muted: false,
      seekSeconds: 0
    }

    this.intervals = {
      playingInterval: 5,
      playbackPosition: 0,
      quartilePosition: -1
    }

    this.playerId = null
  }

  /** returns JSON data for current video  **/
  get video () {
    return {
      id: this.settings.videoId,
      videoPlatform: this.platform,
      seekSeconds: this.settings.seekSeconds,
      title: this.settings.videoTitle,
      inPlaylist: this.settings.inPlaylist,
      playListTagsosition: this.settings.playListTagsosition,
      duration: this.getVideoDuration(),
      playerWidth: this.getPlayerWidth(),
      playerHeight: this.getPlayerHeight(),
      playerResolution: this.getPlayerResolution(),
      isAutoPlay: this.getIsAutoPlay(),
      startVolume: this.getStartVolume(),
      volume: this.getVolume(),
      isFullscreen: this.onVideoFullscreen(),
      videoUuid: this.getVideoUuid(),
      videoFileType: this.getVideoFileType(),
      videoInitiation: this.getAttribute('data-playback-started') || false,
      credit: this.getCredit(),
      ownerSite: this.getOwnerSite(),
      videoClassification: this.getCategory(),
      source: this.getSource(),
      location: this.getDateline(),
      videoStatus: this.getVideoStatus(),
      quartile: this.getDisplayQuartile(this.intervals.quartilePosition)
    }
  }

  init () {
    this.playerId = this.getAttribute('data-player-id')
    this.platform = this.getPlatformName()
    this.settings.videoId = this.getAttribute('data-video-id')
    this.settings.videoTitle = this.getAttribute('data-video-title')
    this.settings.autoplay = this.getAttribute('data-autoplay') || false
    this.settings.muted = this.getAttribute('data-muted')
    this.settings.seekSeconds = this.getAttribute('data-seek-seconds') || 0
    this.settings.dfpUrl = this.getAttribute('data-dfp-url')
    this.settings.inPlaylist =
      this.parentElement.getAttribute('data-in-playlist') === 'true' || false

    if (this.settings.inPlaylist === true) {
      this.settings.playListTagsosition = this.getPlayListTagsosition(
        this.settings.videoId
      )
    }
  }

  getVideoDuration () {
    // abstract: implement in child class
  }

  /**
   * The following functions dispatches events when certain video events happen.
   * The child classes call these super functions
   */

  onVideoReady () {
    let customEvent = new window.CustomEvent('Video:onVideoLoaded', {
      bubbles: true,
      detail: {
        playerId: this.playerId,
        video: this.video
      }
    })

    this.dispatchEvent(customEvent)
  }

  onVideoStart () {
    this.resetIntervals()

    this.removeAttribute('data-playback-paused')
    this.setAttribute('data-playback-started', true)
    this.setAttribute('data-playback-playing', true)

    let customEvent = new window.CustomEvent('Video:onVideoPlaybackStarted', {
      bubbles: true,
      detail: {
        playerId: this.playerId,
        videoPlatform: this.platform,
        video: this.video
      }
    })

    this.dispatchEvent(customEvent)
  }

  onVideoPlay () {
    this.removeAttribute('data-playback-paused')
    this.setAttribute('data-playback-playing', true)

    let customEvent = new window.CustomEvent('Video:onVideoPlaybackPlay', {
      bubbles: true,
      detail: {
        playerId: this.playerId,
        videoPlatform: this.platform,
        video: this.video
      }
    })

    this.dispatchEvent(customEvent)
    console.log('super playing post dispatch')
  }

  onVideoTimeUpdate (event) {
    const secondsElapsed = event.secondsElapsed

    this.sendVideoPlaybackIntervalEvents(secondsElapsed)
  }

  onVideoMute () {
    this.setAttribute('data-muted', true)

    let customEvent = new window.CustomEvent('Video:onVideoPlaybackMuted', {
      bubbles: true,
      detail: {
        playerId: this.playerId,
        videoPlatform: this.platform,
        video: this.video
      }
    })

    this.dispatchEvent(customEvent)
  }

  onVideoUnMute () {
    this.removeAttribute('data-muted')

    let customEvent = new window.CustomEvent('Video:onVideoPlaybackUnMuted', {
      bubbles: true,
      detail: {
        playerId: this.playerId,
        videoPlatform: this.platform,
        video: this.video
      }
    })

    this.dispatchEvent(customEvent)
  }

  onVideoPause () {
    this.setAttribute('data-playback-paused', true)
    this.removeAttribute('data-playback-playing')

    let customEvent = new window.CustomEvent('Video:onVideoPlaybackPaused', {
      bubbles: true,
      detail: {
        playerId: this.playerId,
        videoPlatform: this.platform,
        video: this.video
      }
    })

    this.dispatchEvent(customEvent)
  }

  onVideoEnd () {
    this.removeAttribute('data-playback-paused')
    this.setAttribute('data-playback-ended', true)
    let customEvent = new window.CustomEvent('Video:onVideoEnded', {
      bubbles: true,
      detail: {
        playerId: this.playerId,
        videoPlatform: this.platform,
        video: this.video
      }
    })

    this.dispatchEvent(customEvent)
  }

  /**
   * API to play and pause video, these are going to actually be implemented in the child classes
   */

  play () {
    // abstract: implement in child class
  }

  pause () {
    // abstract: implement in child class
  }

  playPause () {
    if (this.getAttribute('data-playback-paused')) {
      this.play()
    } else {
      this.pause()
    }
  }

  /**
   * Interval functions. This happens on the time update and sends events every
   * "playingInterval" and does all the math for percentile events as well
   */

  resetIntervals () {
    this.intervals = {
      playingInterval: 5,
      playbackPosition: 0,
      quartilePosition: -1
    }
  }

  sendVideoPlaybackIntervalEvents (secondsElapsed) {
    const duration = this.getVideoDuration()

    if (!duration || duration === 0) {
      return
    }

    const percentComplete = (secondsElapsed / duration) * 100

    // don't send extra events if the video didn't naturally pass the interval
    while (
      secondsElapsed >
      this.intervals.playbackPosition + this.intervals.playingInterval * 2
    ) {
      this.intervals.playbackPosition += this.intervals.playingInterval
    }

    if (
      secondsElapsed >=
      this.intervals.playbackPosition + this.intervals.playingInterval
    ) {
      this.intervals.playbackPosition += this.intervals.playingInterval

      let customEvent = new window.CustomEvent(
        'Video:onVideoTimeIntervalUpdate',
        {
          bubbles: true,
          detail: {
            playerId: this.playerId,
            videoPlatform: this.platform,
            video: this.video,
            secondsElapsed: secondsElapsed
          }
        }
      )

      this.dispatchEvent(customEvent)
    }

    let quartileEvent = null

    if (percentComplete >= this.intervals.quartilePosition) {
      // when resuming, make sure we don't repeat quartile events
      // once percentile updates correctly after seek, figure out next valid event position
      if (this.intervals.quartilePosition === -1) {
        if (percentComplete >= this.intervals.quartilePosition + 25) {
          this.intervals.quartilePosition = 0
          while (percentComplete >= this.intervals.quartilePosition) {
            this.intervals.quartilePosition += 25
          }
        }
      } else {
        quartileEvent = this.intervals.quartilePosition

        this.intervals.quartilePosition += 25
        if (this.intervals.quartilePosition === 100) {
          this.intervals.quartilePosition = 95
        }
      }
    }

    if (quartileEvent !== null) {
      // to ensure 100% is always reached, call at 95% instead
      if (quartileEvent === 95) {
        quartileEvent = 100
      }

      let customEvent = new window.CustomEvent(
        'Video:onVideoQuartileCompleted',
        {
          bubbles: true,
          detail: {
            playerId: this.playerId,
            videoPlatform: this.platform,
            video: this.video,
            secondsElapsed: secondsElapsed,
            quartile: quartileEvent
          }
        }
      )

      this.dispatchEvent(customEvent)
    }
  }

  // Map internal quartilePosition so that crossing 25% done --> 25, etc
  getDisplayQuartile (quartilePos) {
    if (quartilePos === -1) {
      return 0
    }

    if (quartilePos === 50) {
      return 25
    }

    if (quartilePos === 75) {
      return 50
    }

    if (quartilePos === 95) {
      return 75
    }

    return 100
  }

  /**
   * The following functions are used by the analytics dataLayer
   */
  getCategory () {
    // abstract: implement in child class
  }

  getCredit () {
    // abstract: implement in child class
  }

  // Used for the "Location" element
  getDateline () {
    // abstract: implement in child class
  }

  getIsAutoPlay () {
    // abstract: implement in child class
  }

  getOwnerSite () {
    // abstract: implement in child class
  }

  getPlatformName () {
    // abstract: implement in child class
  }

  getPlayerHeight () {
    // abstract: implement in child class
  }

  getPlayerWidth () {
    // abstract: implement in child class
  }

  getPlayerResolution () {
    let width = this.getPlayerWidth()
    let height = this.getPlayerHeight()
    return width + ' x ' + height
  }

  getPlayListTagsosition (id) {
    let playlist = document.querySelector('[data-playlist]')
    let PlayListItems = playlist.querySelectorAll('.PlayListItem')
    let activeVid = playlist.querySelector(`[data-video-id='${id}']`)

    for (let i = 0; i < PlayListItems.length; i++) {
      if (activeVid === PlayListItems[i]) {
        return i
      }
    }
  }

  getSource () {
    // abstract: implement in child class
  }

  getVideoFileType () {
    // abstract: implement in child class
  }

  getVideoStatus () {
    if (this.getAttribute('data-playback-playing')) {
      return 'Playing'
    } else if (this.getAttribute('data-playback-paused')) {
      return 'Paused'
    } else if (this.getAttribute('data-playback-ended')) {
      return 'Ended'
    } else return null
  }

  getVideoUuid () {
    // abstract: implement in child class
  }

  getVolume () {
    // abstract: implement in child class
  }

  getStartVolume () {
    // abstract: implement in child class
  }

  onVideoFullscreen () {
    // abstract: implement in child class
  }
}
