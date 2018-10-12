/* global kWidget */
import {VideoPlayer} from 'videoPlayer'

// Kaltura Reference: https://vpaas.kaltura.com/documentation/Web-Video-Player/Kaltura-Media-Player-API.html
export class KalturaVideoPlayer extends VideoPlayer {
  selectors = {
    targetId: 'KalturaVideoPlayer-%id%'
  }

  uiConfId = null
  entryId = null
  partnerId = null
  dfpUrl = null
  player = null

  get videoDuration () {
    return this.player.evaluate('{duration}')
  }

  constructor (el) {
    super(el)
    super.init()

    this.entryId = this.$el.attr('data-entry-id')
    this.uiConfId = this.$el.attr('data-ui-conf-id')
    this.partnerId = this.$el.attr('data-partner-id')

    if (typeof kWidget === 'undefined') {
      console.error('ERROR: Kaltura SDK not loaded')
      return
    }

    this.init()
  }

  init () {
    const playerId = this.playerId
    const readyCallback = (playerIdSelector) => {
      const kdp = document.getElementById(playerIdSelector)
      this.player = kdp

      kdp.kBind(`mediaLoaded.${playerId}`, () => {
        this.onMediaReady()
      })

      kdp.kBind(`firstPlay.${playerId}`, () => {
        this.onMediaStart()
      })

      kdp.kBind(`playerPlayed.${playerId}`, () => {
        this.onMediaPlaying()
      })

      kdp.kBind(`playerPlayEnd.${playerId}`, () => {
        this.onMediaEnded()
      })

      kdp.kBind(`mute.${playerId}`, () => {
        this.onPlayerMute()
      })

      kdp.kBind(`unmute.${playerId}`, () => {
        this.onPlayerUnMute()
      })

      kdp.kBind(`playerPaused.${playerId}`, () => {
        this.onPlayerPause()
      })

      kdp.kBind(`playerUpdatePlayhead.${playerId}`, (e) => {
        this.onMediaTimeUpdate(e)
      })
    }

    const initData = {
      'targetId': this.selectors.targetId.replace('%id%', playerId),
      'uiconf_id': this.uiConfId,
      'entry_id': this.entryId,
      'wid': '_' + this.partnerId,
      'readyCallback': readyCallback,
      'flashvars': {
        'autoPlay': this.settings.autoplay,
        'autoMute': this.settings.muted,
        'mediaProxy.mediaPlayFrom': this.settings.seekSeconds
      }
    }

    if (this.settings.dfpUrl) {
      initData.flashvars.doubleClick = {
        'adTagUrl': this.settings.dfpUrl,
        'leadWithFlash': 'false'
      }
    }

    kWidget.embed(initData)
  }

  loadVideo () {
    this.player.sendNotification('changeMedia', {'entryId': this.entryId, 'partnerId': this.partnerId})
  }

  // Listening on Muted state
  onPlayerMute () {
    this.onVideoMute()
  }

  onPlayerUnMute () {
    this.onVideoUnMute()
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
    this.player.sendNotification('doPlay')
  }

  pause () {
    this.player.sendNotification('doPause')
  }

  updateView ($newVideo) {
    super.updateView($newVideo)

    this.entryId = $newVideo.attr('data-entry-id')
    this.partnerId = $newVideo.attr('data-partner-id')

    // re-init
    this.loadVideo()
  }
}
