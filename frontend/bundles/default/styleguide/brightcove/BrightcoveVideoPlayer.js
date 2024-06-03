import { VideoPlayer } from '../video/VideoPlayer.js'

export default class BrightcoveVideoPlayer extends VideoPlayer {
  constructor() {
    super()

    this.playing = false
    this.player = null
  }

  connectedCallback() {
    super.init()

    this.player = this

    this.accountVId = this.player.getAttribute('data-account') || false
    this.playerVId = this.player.getAttribute('data-player') || false
    this.videoVId = this.player.getAttribute('data-video-id') || false

    if (this.accountVId && this.playerVId && this.videoVId) {
      this.init()
    } else {
      console.info(
        'Brightcove Video Player: Cannot play video, no account, player or video ID found'
      )
      return false
    }
  }

  disconnectedCallback() {
    if (window.videojs.getPlayers()['BrightcoveVideoPlayer-' + this.videoVId]) {
      delete window.videojs.getPlayers()[
        'BrightcoveVideoPlayer-' + this.videoVId
      ]
    }
  }

  init() {
    this.loadBrightcoveApi(this.accountVId, this.playerVId)
  }

  loadBrightcoveApi(accountId, playerId) {
    const tag = document.createElement('script')
    tag.src =
      'https://players.brightcove.net/' +
      accountId +
      '/' +
      playerId +
      '_default/index.min.js'
    tag.addEventListener('load', this.onBrightcoveAPIReady.bind(this))

    const firstScriptTag = document.getElementsByTagName('script')[0]
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag)
  }

  onBrightcoveAPIReady() {
    this.videoPlayer = this.querySelector('video')
    this.thePlayer = window.videojs(this.videoPlayer.id)

    if (typeof this.thePlayer !== 'undefined') {
      this.startVolume = this.thePlayer.volume()
      this.playerEvents()
    }
  }

  playerEvents() {
    this.onReady()

    this.thePlayer.on('playing', (event) => {
      this.playing = true
      this.onPlay(event)
    })

    this.thePlayer.on('pause', (event) => {
      this.onPause(event)
    })

    this.thePlayer.on('timeUpdate', (event) => {
      this.currentTime = this.thePlayer.currentTime()
      this.duration = this.thePlayer.duration()
      this.onTimeUpdate(event)
    })

    this.thePlayer.on('ended', (event) => {
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
