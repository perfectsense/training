import { VideoPlayer } from '../video/VideoPlayer.js'

export default class JwVideoPlayer extends VideoPlayer {
  constructor() {
    super()

    this.playing = false
    this.player = null
  }

  get videoElement() {
    return this.querySelector('video')
  }

  connectedCallback() {
    super.init()

    this.player = this

    this.playerVId = this.player.getAttribute('data-player') || false
    this.videoVId = this.player.getAttribute('data-video-uuid') || false
    this.mediaVId = this.player.getAttribute('data-media-id') || false

    if (this.playerVId && this.mediaVId && this.videoVId) {
      this.init()
    } else {
      console.info(
        'JW Video Player: Cannot play video, no media, player or video ID found'
      )
      return false
    }
  }

  disconnectedCallback() {
    if (window.jwplayer(this.videoElement)) {
      window.jwplayer(this.videoElement).remove()
    }
  }

  init() {
    this.loadJwApi(this.playerVId)
  }

  loadJwApi(playerId) {
    if (window.jwplayer && document.getElementById(playerId)) {
      this.onJwApiReady()
    } else {
      const tag = document.createElement('script')
      tag.src = 'https://cdn.jwplayer.com/libraries/' + playerId + '.js'
      tag.id = playerId
      tag.addEventListener('load', this.onJwApiReady.bind(this))

      const firstScriptTag = document.getElementsByTagName('script')[0]
      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag)
    }
  }

  onJwApiReady() {
    this.videoPlayer = this.player.querySelector('video')
    this.thePlayer = window.jwplayer(this.videoElement)

    if (typeof this.thePlayer !== 'undefined') {
      this.thePlayer.setup({
        playlist: `https://cdn.jwplayer.com/v2/media/${this.mediaVId}`,
        image: this.poster,
        width: '100%',
        responsive: 'true',
        aspectratio: '16:9',
        title: this.videoTitle,
        mute: this.muted,
        autostart: this.autoplay,
      })

      this.playerEvents()
    }
  }

  playerEvents() {
    this.thePlayer.on('ready', (event) => {
      this.onReady(event)
    })

    this.thePlayer.on('play', (event) => {
      this.playing = true
      this.onPlay(event)
    })

    this.thePlayer.on('pause', (event) => {
      this.onPause(event)
    })

    this.thePlayer.on('mute', (event) => {
      this.onMute(event)
    })

    this.thePlayer.on('time', (event) => {
      this.currentTime = this.thePlayer.getPosition()
      this.duration = this.thePlayer.getDuration()
      this.onTimeUpdate(event)
    })

    this.thePlayer.on('complete', (event) => {
      this.playing = false
      this.onEnded(event)
    })
  }

  play() {
    this.thePlayer.play()
  }

  pause() {
    this.thePlayer.pause()
  }
}
