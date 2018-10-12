import {VideoPlayer} from 'videoPlayer'
// YouTube API Reference https://developers.google.com/youtube/iframe_api_reference

export class YouTubeVideoPlayer extends VideoPlayer {
  playing = false
  player = null

  selectors = {
    blockName: 'YouTubeVideoPlayer',
    targetId: 'YouTubeVideoPlayer-%id%'
  }

  get videoDuration () {
    return this.player.getDuration()
  }

  constructor (el, options) {
    super(el, options)
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

    this.videoId = this.$el.attr(`data-video-id`)

    if (!window.YouTubeAPIReady) {
      this.loadYouTubeApi()
    }

    window.onYouTubeIframeAPIReady = () => {
      window.YouTubeAPIReady = true

      const playerTarget = this.selectors.targetId.replace('%id%', this.playerId)

      this.player = new window.YT.Player(playerTarget, {
        videoId: this.videoId,
        events: {
          onReady: this.onPlayerReady.bind(this),
          onStateChange: this.onStateChange.bind(this)
          // TODO Youtube doesn't broadcast a playback time event, we could add one with setInterval and getCurrentTime
        }
      })
    }
  }

  loadYouTubeApi () {
    let tag = document.createElement('script')
    tag.src = 'https://www.youtube.com/iframe_api'
    let firstScriptTag = document.getElementsByTagName('script')[0]
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag)
  }

  onPlayerReady (event) {
    if (this.settings.muted) {
      event.target.mute()
    }
  }

  onStateChange (event) {
    let state = event.data
    console.info(`YouTube Player onStateChange: ${state}`)
    this.stateChangeHandlers[state]
        ? this.stateChangeHandlers[state]()
        : console.info(`stateChangeHandler for state named '${state}' has not been implemented`)
  }

  updateView ($newVideo) {
    super.updateView($newVideo)

    this.$el.html($newVideo.html())
    this.playing = false
    this.init()
  }

  onPlayerPlaying (event) {
    if (!this.playing) {  // to prevent multiple start events when seeking/buffering
      this.playing = true
      super.onVideoStart(event)
    }
  }

  onPlayerPaused (event) {
    super.onVideoPause(event)
  }

  onPlayerEnd (event) {
    this.playing = false
    super.onVideoEnd(event)
  }

  play () {
    this.player.playVideo()
  }

  pause () {
    this.player.pauseVideo()
  }
}
