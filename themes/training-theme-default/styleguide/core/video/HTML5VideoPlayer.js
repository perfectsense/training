import { VideoPlayer } from './VideoPlayer.js'

/**
 * HTML5 Video Player. Uses the native video API to pass events, get video info, andd play, pause
 */

export class HTML5VideoPlayer extends VideoPlayer {
  constructor () {
    super()

    this.playing = false
    this.player = null
  }

  connectedCallback () {
    super.init()
    this.init()
  }

  init () {
    this.player = this.querySelector('.HTML5VideoPlayer-video')

    super.onVideoReady()

    this.player.addEventListener('play', event => {
      if (!this.playing) {
        // to prevent multiple start events when seeking/buffering
        this.playing = true
        super.onVideoStart(event)
      } else {
        super.onVideoPlay(event)
      }
    })

    this.player.addEventListener('pause', event => {
      super.onVideoPause(event)
    })

    this.player.addEventListener('timeupdate', () => {
      super.onVideoTimeUpdate({
        secondsElapsed: this.player.currentTime
      })
    })

    this.player.addEventListener('ended', event => {
      this.playing = false
      super.onVideoEnd(event)
    })
  }

  getVideoDuration () {
    return this.player.duration || 0
  }

  play () {
    this.player.play()
  }

  pause () {
    this.player.pause()
  }

  getPlatformName () {
    return 'html5'
  }
}
