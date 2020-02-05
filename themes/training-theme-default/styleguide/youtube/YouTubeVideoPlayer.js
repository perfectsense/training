import { VideoPlayer } from '../core/video/VideoPlayer.js'
// YouTube API Reference https://developers.google.com/youtube/iframe_api_reference

/**
 * YouTube Video Player. We load the YouTube iframe JS API and use it to interact with the iframe
 * playing YouTube's video.
 */

export class YouTubeVideoPlayer extends VideoPlayer {
  constructor () {
    super()

    this.playing = false
    this.player = null

    this.selectors = {
      targetId: 'YouTubeVideoPlayer-%id%'
    }
  }

  connectedCallback () {
    super.init()
    this.init()
  }

  init () {
    /*
     -1 (unstarted)
     0 (ended)
     1 (playing)
     2 (paused)
     3 (buffering)
     5 (video cued).
     */
    this.stateChangeHandlers = {
      '0': this.onPlayerEnd.bind(this),
      '1': this.onPlayerPlaying.bind(this),
      '2': this.onPlayerPaused.bind(this)
    }

    this.videoId = this.getAttribute(`data-video-id`)

    // Only need to load the YouTube API once
    if (!window.YouTubeAPIReady) {
      this.loadYouTubeApi()
    } else {
      this.initPlayer()
    }

    // This is the global level callback function for YouTube's API
    window.onYouTubeIframeAPIReady = () => {
      window.YouTubeAPIReady = true

      this.initPlayer()
    }
  }

  // YouTube likes to refer to the player via ID, so we do that and create
  // the stateChange handler which they use to communicate all their events
  initPlayer () {
    const playerTarget = this.selectors.targetId.replace('%id%', this.playerId)

    this.player = new window.YT.Player(playerTarget, {
      videoId: this.videoId,
      events: {
        onStateChange: this.onStateChange.bind(this)
      }
    })

    this.setupTimeTracking()
    super.onVideoReady()
  }

  getVideoDuration () {
    return typeof this.player.getDuration === 'undefined'
      ? 0
      : this.player.getDuration()
  }

  loadYouTubeApi () {
    let tag = document.createElement('script')
    tag.src = 'https://www.youtube.com/iframe_api'
    let firstScriptTag = document.getElementsByTagName('script')[0]
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag)
  }

  onStateChange (event) {
    let state = event.data

    if (this.stateChangeHandlers[state]) {
      this.stateChangeHandlers[state]()
    }
  }

  onPlayerPlaying (event) {
    if (!this.playing) {
      // to prevent multiple start events when seeking/buffering
      this.playing = true
      super.onVideoStart(event)
    } else {
      super.onVideoPlay(event)
    }
  }

  onPlayerPaused (event) {
    super.onVideoPause(event)
  }

  onPlayerEnd (event) {
    this.playing = false
    super.onVideoEnd(event)
  }

  // Custom function to deal with time tracking. YouTube's stateChange API doesn't provide time tracking
  // events, so we do this ourselves with an interval instead. We call this event every second for proper
  // quartile and end events
  setupTimeTracking () {
    window.setInterval(() => {
      if (this.getAttribute('data-playback-playing')) {
        super.onVideoTimeUpdate({
          secondsElapsed: this.player.getCurrentTime()
        })
      }
    }, 1000)
  }

  // Implementing play/pause based on YouTube's API
  play () {
    this.player.playVideo()
  }

  pause () {
    this.player.pauseVideo()
  }

  getPlatformName () {
    return 'youtube'
  }
}
