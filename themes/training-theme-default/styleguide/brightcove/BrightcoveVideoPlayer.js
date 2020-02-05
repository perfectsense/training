import { VideoPlayer } from '../core/video/VideoPlayer.js'

/**
 * Brightcove Video Player. The Brightcove player uses VideoJS behind the scenes. Even though it uses
 * native "video" HTML tag in the .hbs, it runs it own's JS to re-create this tag with the proper src
 * values and then adds VideoJS on top of that. Because of that we re-load the Brightcove API on every
 * connectedCallback instead of checking to see if it exists. That API has no "reload" functionality and
 * instead only scans the page for the brightcove "video" tags when it's loaded. VideoJS complains about
 * this in the console, but until Brightcove fixes that, there is nothing we can do.
 */

export class BrightcoveVideoPlayer extends VideoPlayer {
  constructor () {
    super()

    this.playing = false
    this.player = null
  }

  connectedCallback () {
    super.init()

    // The following attributes are needed for the brightcove JS to load the proper JS/CSS for their player

    this.player = this.querySelector('video')

    this.accountId = this.player.getAttribute('data-account') || false
    this.playerId = this.player.getAttribute('data-player') || false
    this.videoId = this.player.getAttribute('data-video-id') || false
    // The following attributes are used by the analytics dataLayer
    this.category = this.getAttribute('data-category') || null
    this.credit = this.getAttribute('data-credit') || null
    this.location = this.getAttribute('data-location') || null
    this.ownerSite = this.getAttribute('data-ownerSite') || 'Global'
    this.source = this.getAttribute('data-source') || null
    this.videoUuid = this.getAttribute('data-video-uuid') || false
    this.videoFileType = this.getAttribute('data-video-file-type') || false

    if (this.accountId && this.playerId && this.videoId) {
      this.init()
    } else {
      console.info(
        'Brightcove Video Player: Cannot play video, no account, player or video ID found'
      )
      return false
    }
  }

  // If we get removed, remove the current player from videoJS to prevent a bunch of unused players
  disconnectedCallback () {
    if (window.videojs.getPlayers()['brightcove-video-' + this.videoId]) {
      delete window.videojs.getPlayers()['brightcove-video-' + this.videoId]
    }
  }

  // Load the Brightcove API every time, as the page needs to be rescanned for their video tags :(
  init () {
    this.loadBrightcoveApi(this.accountId, this.playerId)
  }

  // Once the Brightcove API is loaded, we tie into the videoJS "loadedmetadata" event to kick off
  // our event binding. This is the recommended event per their documentation
  onBrightcoveAPIReady () {
    this.videoPlayer = this.querySelector('video')
    this.thePlayer = window.videojs(this.videoPlayer.id)

    this.prerollAdCode()

    window.videojs(this.videoPlayer).on('loadedmetadata', () => {
      this.startVolume = this.thePlayer.volume()
      this.tieIntoEvents()
    })
  }

  getVideoDuration () {
    return this.player.duration || 0
  }

  loadBrightcoveApi (accountId, playerId) {
    let tag = document.createElement('script')
    tag.src =
      'https://players.brightcove.net/' +
      accountId +
      '/' +
      playerId +
      '_default/index.min.js'
    tag.addEventListener('load', this.onBrightcoveAPIReady.bind(this))

    let firstScriptTag = document.getElementsByTagName('script')[0]
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag)
  }

  prerollAdCode () {
    if (this.thePlayer.ima3) {
      this.thePlayer.ima3.ready(() => {
        // https://support.brightcove.com/advertising-ima3-plugin#Implement_using_code
        this.thePlayer.ima3.settings.debug = true

        let serverUrl = 'https://pubads.g.doubleclick.net/gampad/ads?'

        // adding all the parameters manually here, no need for API helpers like the code example
        serverUrl += 'sz=3x3'
        serverUrl +=
          '&iu=' + encodeURIComponent(this.getAttribute('data-ad-slot'))
        serverUrl += '&ciu_szs'
        serverUrl += '&gdfp_req=1'
        serverUrl += '&env=vp'
        serverUrl += '&output=xml_vast3'
        serverUrl += '&ptype=' + this.getAttribute('data-ad-ptype')
        serverUrl += '&unviewed_position_start=1'
        serverUrl += '&player=bc'
        serverUrl += '&pos=pre'
        serverUrl += '&muted=false'
        serverUrl += '&player_width=' + this.videoPlayer.offsetWidth
        serverUrl += '&player_height=' + this.videoPlayer.offsetHeight
        serverUrl += '&exop=false'
        serverUrl += '&pl=false'
        serverUrl += '&vd=' // this was commented out in the code example we got, so leaving it blank
        serverUrl += '&vt=' // this was commented out in the code example we got, so leaving it blank
        serverUrl += '&vc=' // it seems classification isn't used currently (e.g. SomberNews)
        serverUrl += '&clip=' + this.videoId
        serverUrl += '&correlator=' + new Date().getTime()
        serverUrl += '&tile=13'

        // Set the serverUrl in the plugin to make the request.
        this.thePlayer.ima3.settings.serverUrl = serverUrl
      })
    }
  }

  // Here we tie into all the Brightcove based APIs. We are using the "on" syntax of the Brightcove events
  // vs native "addEventListener" of the native video tag, as brighcove does some special stuff with
  // timeupdate that the addEventListeners do not capture
  tieIntoEvents () {
    super.onVideoReady()

    this.thePlayer.on('playing', event => {
      this.onPlayerPlay(event)
    })

    this.thePlayer.on('pause', event => {
      this.onPlayerPause(event)
    })

    this.thePlayer.on('timeupdate', event => {
      this.onPlayerTimeUpdate(event)
    })

    this.thePlayer.on('ended', event => {
      this.onPlayerEnd(event)
    })
  }

  onPlayerPlay (event) {
    if (!this.playing) {
      // to prevent multiple start events when seeking/buffering
      this.playing = true
      super.onVideoStart(event)
    } else {
      super.onVideoPlay(event)
    }
  }

  onPlayerPause (event) {
    super.onVideoPause(event)
  }

  onPlayerTimeUpdate (event) {
    super.onVideoTimeUpdate({
      secondsElapsed: this.thePlayer.currentTime()
    })
  }

  onPlayerEnd (event) {
    this.playing = false
    super.onVideoEnd(event)
  }

  // Implementing play/pause based on Brightcove's API
  play () {
    this.thePlayer.play()
  }

  pause () {
    this.thePlayer.pause()
  }
}
