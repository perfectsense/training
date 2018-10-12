import {VideoPlayer} from 'videoPlayer'
// Vimeo API Reference https://github.com/vimeo/player.js

export class VimeoVideoPlayer extends VideoPlayer {
  playing = false

  selectors = {
    targetId: 'VimeoVideoPlayer-%id%'
  }

  duration = null
  player = null

  get videoDuration () {
    return this.duration
  }

  constructor (el, options) {
    super(el, options)
    super.init()

    this.init()
  }

  init () {
    this.videoId = this.$el.attr(`data-video-id`)

    if (!window.VimeoAPIReady) {
      this.loadVimeoApi()
    } else {
      this.onVimeoAPIReady()
    }
  }

  onVimeoAPIReady () {
    window.VimeoAPIReady = true

    const playerTarget = this.selectors.targetId.replace('%id%', this.playerId)

    this.player = new window.Vimeo.Player(playerTarget)

    if (this.settings.muted) {
      this.player.setVolume(0)
    }

    if (this.settings.seekSeconds) {
      this.player.setCurrentTime(this.settings.seekSeconds)
    }

    this.player.on('play', (e) => {
      this.onPlayerPlay(e)
    })
    this.player.on('pause', (e) => {
      this.onPlayerPause()
    })
    this.player.on('timeupdate', (e) => {
      this.onPlayerTimeUpdate(e)
    })
    this.player.on('ended', (e) => {
      this.onPlayerEnd()
    })
  }

  loadVimeoApi () {
    let tag = document.createElement('script')
    tag.src = 'https://player.vimeo.com/api/player.js'
    let firstScriptTag = document.getElementsByTagName('script')[0]
    firstScriptTag.addEventListener('load', this.onVimeoAPIReady.bind(this))
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag)
  }

  onPlayerPlay (e) {
    if (!this.playing) {  // to prevent multiple start events when seeking/buffering
      this.playing = true
      this.duration = e.duration
      super.onVideoStart(e)
    } else {
      super.onVideoUnPause(e)
    }
  }

  onPlayerPause (e) {
    super.onVideoPause(e)
  }

  onPlayerTimeUpdate (e) {
    super.onVideoTimeUpdate({
      secondsElapsed: e.seconds
    })
  }

  onPlayerEnd (e) {
    super.onVideoEnd()
  }

  play () {
    this.player.play()
  }

  pause () {
    this.player.pause()
  }

  updateView ($newVideo) {
    this.$el.html($newVideo.html())

    this.settings.autoplay = true

    if ($newVideo.is('[data-muted]')) {
      this.settings.muted = true
    }

    this.settings.seekSeconds = $newVideo.attr('data-seek-seconds') || 0

    this.playing = false
    this.init()
  }
}
