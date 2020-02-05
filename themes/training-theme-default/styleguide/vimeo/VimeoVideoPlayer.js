import { VideoPlayer } from '../core/video/VideoPlayer.js'
// Vimeo API Reference https://github.com/vimeo/player.js

/**
 * Vimeo Video Player. We use Vimeo's API which takes a div on the page and creates an iframe to
 * play the video when a new Video.Player is called on the div
 */

export class VimeoVideoPlayer extends VideoPlayer {
  constructor () {
    super()

    this.playing = false
    this.player = null
  }

  connectedCallback () {
    super.init()
    this.init()
  }

  // Vimeo's API provides the new Player syntax, so we only need to load it once
  init () {
    if (!window.VimeoAPIReady) {
      this.loadVimeoApi()
    } else {
      this.onVimeoAPIReady()
    }
  }

  // The duration for vimeo isn't available until the video stars playing, so we return 0
  // until something else sets this for us
  getVideoDuration () {
    return this.duration || 0
  }

  onVimeoAPIReady () {
    window.VimeoAPIReady = true

    let videoPlayerDiv = this.querySelector('.VimeoVideoPlayer-player')

    this.player = new window.Vimeo.Player(videoPlayerDiv)

    // On other players like YouTube, HTML5 or Brightcove, these are pulled from the video tag or
    // from the iframe tag. For Vimeo, we do these manually based on the data attributes we set for ourselves
    if (this.settings.muted) {
      this.player.setVolume(0)
    }

    if (this.settings.seekSeconds) {
      this.player.setCurrentTime(this.settings.seekSeconds)
    }

    // When we autoplay, we need to mute, as that's pretty much a browser standard now
    if (this.getAttribute('data-autoplay')) {
      this.player.setVolume(0)
      this.play()
    }

    super.onVideoReady()
    this.tieIntoEvents()
  }

  tieIntoEvents () {
    this.player.on('play', event => {
      this.onPlayerPlay(event)
    })

    this.player.on('pause', event => {
      this.onPlayerPause(event)
    })

    this.player.on('timeupdate', event => {
      this.onPlayerTimeUpdate(event)
    })

    this.player.on('ended', event => {
      this.onPlayerEnd(event)
    })
  }

  loadVimeoApi () {
    let tag = document.createElement('script')
    tag.src = 'https://player.vimeo.com/api/player.js'
    tag.addEventListener('load', this.onVimeoAPIReady.bind(this))

    let firstScriptTag = document.getElementsByTagName('script')[0]
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag)
  }

  // The video duration isn't available until this event, so we set it when the player starts to play
  onPlayerPlay (event) {
    if (!this.playing) {
      // to prevent multiple start events when seeking/buffering
      this.playing = true
      this.duration = event.duration
      super.onVideoStart(event)
    } else {
      super.onVideoUnPause(event)
    }
  }

  onPlayerPause (event) {
    super.onVideoPause(event)
  }

  onPlayerTimeUpdate (event) {
    super.onVideoTimeUpdate({
      secondsElapsed: event.seconds
    })
  }

  onPlayerEnd (event) {
    this.playing = false
    super.onVideoEnd(event)
  }

  // Implementing play/pause based on Vimeo's API
  play () {
    this.player.play()
  }

  pause () {
    this.player.pause()
  }

  getPlatformName () {
    return 'vimeo'
  }
}
