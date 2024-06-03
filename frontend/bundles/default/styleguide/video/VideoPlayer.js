/**
 * This class gets extended by all the child players. The child players have their own Listeners
 * to the video actions depending on what the video technology is used and call the super functions to actually
 * dispatch these events so things like the video page or analytics can capture them
 */

export const events = {
  ready: 'VideoPlayer:ready',
  timeUpdate: 'VideoPlayer:timeUpdate',
  play: 'VideoPlayer:play',
  playing: 'VideoPlayer:playing',
  pause: 'VideoPlayer:paused',
  volumeChange: 'VideoPlayer:volumeChange',
  enterFullscreen: 'VideoPlayer:enterFullscreen',
  exitFullscreen: 'VideoPlayer:exitFullscreen',
  ended: 'VideoPlayer:ended',
  error: 'VideoPlayer:error',
  quartileCompleted: 'VideoPlayer:quartileCompleted',
}

const intervals = {
  playingInterval: 5,
  playbackPosition: 0,
  quartilePosition: -1,
}

export class VideoPlayer extends HTMLElement {
  connectedCallback() {
    this.intervals = { ...intervals }
  }

  get videoId() {
    return this.getAttribute('data-video-id')
  }

  get videoTitle() {
    return this.getAttribute('data-video-title')
  }

  get playerId() {
    return this.getAttribute('data-player-id')
  }

  get autoplay() {
    return this.hasAttribute('data-autoplay')
  }

  get disableClickToPlay() {
    return this.hasAttribute('data-disable-click-to-play')
  }

  get muted() {
    return this.hasAttribute('data-muted')
  }

  get loop() {
    return this.hasAttribute('data-loop')
  }

  get poster() {
    return this.getAttribute('data-poster')
  }

  /** returns JSON data for current video  **/
  get video() {
    return {
      id: this.videoId,
      videoPlatform: this.platform,
      seekSeconds: this.seekSeconds,
      title: this.videoTitle,
      inPlaylist: document.querySelector('ps-playlist'),
      playListTagsosition: [
        ...document.querySelectorAll('ps-playlist-item'),
      ].findIndex((el) => el.isCurrent),
      duration: this.duration,
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
      quartile: this.getDisplayQuartile(this.intervals.quartilePosition),
    }
  }

  init() {
    this.platform = this.getPlatformName()
  }

  /**
   * The following functions dispatches events when certain video events happen.
   * The child classes call these super functions
   */

  onReady() {
    this.emit(events.ready)
  }

  onPlay() {
    this.emit(events.playing)
  }

  onTimeUpdate() {
    this.sendVideoPlaybackIntervalEvents()
    this.emit(events.timeUpdate)
  }

  onMute() {
    this.emit(events.volumeChange)
  }

  onPause() {
    this.emit(events.pause)
  }

  onEnded() {
    this.intervals = { ...intervals }
    this.emit(events.ended)
  }

  onError() {
    this.emit(events.error)
  }

  onVolumneChange() {
    this.emit(events.volumeChange)
  }

  /**
   * API to play and pause video, these are going to actually be implemented in the child classes
   */

  play() {
    // abstract: implement in child class
  }

  pause() {
    // abstract: implement in child class
  }

  playPause() {
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

  sendVideoPlaybackIntervalEvents() {
    if (!this.duration || this.duration === 0) {
      return
    }

    const percentComplete = (this.currentTime / this.duration) * 100

    // don't send extra events if the video didn't naturally pass the interval
    while (
      this.currentTime >
      this.intervals.playbackPosition + this.intervals.playingInterval * 2
    ) {
      this.intervals.playbackPosition += this.intervals.playingInterval
    }

    if (
      this.currentTime >=
      this.intervals.playbackPosition + this.intervals.playingInterval
    ) {
      this.intervals.playbackPosition += this.intervals.playingInterval

      const customEvent = new window.CustomEvent(
        'Video:onVideoTimeIntervalUpdate',
        {
          bubbles: true,
          detail: {
            playerId: this.playerId,
            videoPlatform: this.platform,
            video: this.video,
            secondsElapsed: this.currentTime,
          },
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

      this.emit(events.quartileCompleted, { quartile: quartileEvent })
    }
  }

  // Map internal quartilePosition so that crossing 25% done --> 25, etc
  getDisplayQuartile(quartilePos) {
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
  getCategory() {
    // abstract: implement in child class
  }

  getCredit() {
    // abstract: implement in child class
  }

  // Used for the "Location" element
  getDateline() {
    // abstract: implement in child class
  }

  getIsAutoPlay() {
    // abstract: implement in child class
  }

  getOwnerSite() {
    // abstract: implement in child class
  }

  getPlatformName() {
    // abstract: implement in child class
  }

  getPlayerHeight() {
    // abstract: implement in child class
  }

  getPlayerWidth() {
    // abstract: implement in child class
  }

  getPlayerResolution() {
    const width = this.getPlayerWidth()
    const height = this.getPlayerHeight()
    return width + ' x ' + height
  }

  getPlayListTagsosition(id) {
    const playlist = document.querySelector('[data-playlist]')
    const PlaylistItems = playlist.querySelectorAll('.PlaylistItem')
    const activeVid = playlist.querySelector(`[data-video-id='${id}']`)

    for (let i = 0; i < PlaylistItems.length; i++) {
      if (activeVid === PlaylistItems[i]) {
        return i
      }
    }
  }

  getSource() {
    // abstract: implement in child class
  }

  getVideoFileType() {
    // abstract: implement in child class
  }

  getVideoStatus() {
    if (this.getAttribute('data-playback-playing')) {
      return 'Playing'
    } else if (this.getAttribute('data-playback-paused')) {
      return 'Paused'
    } else if (this.getAttribute('data-playback-ended')) {
      return 'Ended'
    } else return null
  }

  getVideoUuid() {
    // abstract: implement in child class
  }

  getVolume() {
    // abstract: implement in child class
  }

  getStartVolume() {
    // abstract: implement in child class
  }

  onVideoFullscreen() {
    // abstract: implement in child class
  }

  onEnterFullScreen() {
    this.emit(events.enterFullscreen)
  }

  onExitFullscreen() {
    this.emit(events.exitFullscreen)
  }

  emit(event, detail = {}) {
    const customEvent = new window.CustomEvent(event, {
      bubbles: true,
      detail,
    })
    this.dispatchEvent(customEvent)
  }
}
