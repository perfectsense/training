/* global jwplayer */
import {VideoPlayer} from 'videoPlayer'

// Kaltura Reference: https://vpaas.kaltura.com/documentation/Web-Video-Player/Kaltura-Media-Player-API.html
export class JWVideoPlayer extends VideoPlayer {
  selectors = {
    targetId: 'JWVideoPlayer-%id%'
  }

  player = null
  videoUrl = null
  imageUrl = null

  get videoDuration () {
    return this.player.getDuration()
  }

  constructor (el) {
    super(el)
    super.init()

    this.videoUrl = this.$el.attr('data-video-url')
    this.imageUrl = this.$el.attr('data-image-url')

    if (typeof jwplayer === 'undefined') {
      console.error('ERROR: JWPlayer SDK not loaded')
      return
    }

    this.init()
  }

  init () {
    const playerTarget = this.selectors.targetId.replace('%id%', this.playerId)

    let config = {
      file: this.videoUrl,
      image: this.imageUrl
    }

    if (this.settings.dfpUrl) {
      config.advertising = {
        'tag': this.settings.dfpUrl,
        'client': 'googima'
      }
    }

    this.player = jwplayer(playerTarget).setup(config)

    this.player
        .on('ready', () => {
          this.player.seek(this.settings.seekSeconds)
          this.player.setMute(this.settings.muted)

          if (this.settings.autoplay) {
            this.player.play(true)
          }
        })
        .on('play', (oldstate) => {
          this.onMediaPlaying()
        })
        .on('pause', () => {
          this.onPlayerPause()
        })
        .on('firstFrame', () => {
          this.onMediaReady()
          this.onMediaStart()
        })
        .on('complete', () => {
          this.onMediaEnded()
        })
        .on('time', (event) => {
          this.onMediaTimeUpdate(event.position)
        })
        .on('mute', (event) => {
          this.onPlayerMute(event.mute)
        })
  }

  // Listening on Muted state
  onPlayerMute (mute) {
    if (mute) {
      this.onVideoMute()
    } else {
      this.onVideoUnMute()
    }
  }

  onPlayerPause () {
    this.settings.paused = true
    this.onVideoPause()
  }

  onMediaReady () {
    this.onVideoReady()
  }

  onMediaStart () {
    super.onVideoStart()
  }

  onMediaPlaying () {
    if (this.settings.paused) {
      this.settings.paused = false
      this.onVideoUnPause()
    }
  }

  onMediaEnded () {
    this.onVideoEnd()
  }

  onMediaTimeUpdate (secondsElapsed) {
    this.onVideoTimeUpdate({
      secondsElapsed
    })
  }

  play () {
    this.player.play(true)
  }

  pause () {
    this.player.pause(true)
  }

  updateView ($newVideo) {
    super.updateView($newVideo)

    this.settings.autoplay = true

    if ($newVideo.is('[data-muted]')) {
      this.settings.muted = true
    }

    this.settings.seekSeconds = $newVideo.attr('data-seek-seconds') || 0

    this.videoUrl = $newVideo.attr('data-video-url')
    this.imageUrl = $newVideo.attr('data-image-url')

    // re-init
    this.init()
  }
}
