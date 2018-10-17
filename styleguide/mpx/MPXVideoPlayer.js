/* global $pdk */

import {isMobileUA} from 'core-utils'
import {VideoPlayer} from 'styleguide/core/video/VideoPlayer.js'

// PDK Event reference: https://docs.theplatform.com/help/article/link/player-pdkevent-reference

export class MPXVideoPlayer extends VideoPlayer {
  selectors = {
    blockName: 'MPXVideoPlayer',
    target: '#MPXVideoPlayer-%id%'
  }

  playerXml = `<?xml version='1.0'?>
      <layout>
          <controls>
              <region id='tpBottomFloatRegion'>
                  <row paddingTop="20">
                      <spacer width="10%" />
                      <control id='tpPlay' />
                      <control id='tpMute' />
                      <group>
                          <control id='tpCurrentTime'/>
                          <control id='tpScrubber'/>
                          <control id='tpTotalTime'/>
                      </group>
                      <control id='tpFullScreen' scaleIcon='true' />
                      <spacer width="10%" />
                  </row>
              </region>
          </controls>
      </layout>`
  player = null
  loading = false
  duration = 0

  get videoDuration () {
    return this.duration
  }

  constructor (el) {
    super(el)
    super.init()

    const $player = this.$el.find(this.selectors.target.replace('%id%', this.playerId))
    $player
        .off('load')
        .on('load', () => {
          let checkForPDKReady = setInterval(() => {
            this.player = $pdk.bind($player[0])

            $pdk.controller.setIFrame($player[0], true)

            if (this.isPDKloaded()) {
              clearInterval(checkForPDKReady)
              this.player.addEventListener('OnReleaseStart', (e) => {
                this.onMediaReady(e)
              })
              this.player.addEventListener('OnMediaStart', (e) => {
                this.onMediaStart(e)
              })
              this.player.addEventListener('OnMediaPlaying', (e) => {
                this.onMediaPlaying(e)
              })
              this.player.addEventListener('OnReleaseEnd', (e) => {
                this.onMediaEnded(e)
              })
              this.player.addEventListener('OnMute', (e) => {
                this.onPlayerMute(e)
              })
              this.player.addEventListener('OnMediaPause', (e) => {
                this.onPlayerPause(e)
              })
              this.player.addEventListener('OnMediaUnpause', (e) => {
                this.onPlayerUnPause(e)
              })

              this.init()
            }
          }, 100)
        })
  }

  init () {
    this.loading = true

    this.player.mute(this.settings.muted)

    if (!this.settings.autoplay || isMobileUA()) {
      this.player.loadReleaseURL(this.$el.attr('data-hls-url'), true)
    } else {
      this.player.setReleaseURL(this.$el.attr('data-hls-url'), true)
    }
  }

  isPDKloaded () {
    if (typeof $pdk !== 'undefined' && $pdk.controller.ready) {
      return true
    }
    return false
  }

  onPlayerMute (event) {
    if (event.data) {
      this.onVideoMute()
    } else {
      this.onVideoUnMute()
    }
  }

  onPlayerPause (event) {
    this.onVideoPause(event)
  }

  onPlayerUnPause (event) {
    this.onVideoUnPause(event)
  }

  onMediaReady (event) {
    this.onVideoReady(event)
  }

  onMediaStart (event) {
    if (this.autoplay === false || isMobileUA()) {
      this.player.setPlayerLayoutXml(this.playerXml)
    }

    let seekTo = this.$el.attr('data-seek-seconds')
    if (seekTo) {
      seekTo *= 1000
      // XXX This is hacky, is there a better way?
      setTimeout(() => this.player.seekToPosition(seekTo), 500)
    }

    this.loading = false

    super.onVideoStart(event)
  }

  onMediaPlaying (event) {
    let data = event.data

    this.duration = data.duration / 1000

    this.onVideoTimeUpdate({
      secondsElapsed: data.currentTime / 1000
    })
  }

  onMediaEnded (event) {
    if (this.loading) {
      return
    }

    this.onVideoEnd(event)
  }

  play () {
    this.player.play()
  }

  pause () {
    this.player.pause()
  }

  updateView ($newVideo) {
    super.updateView($newVideo)

    this.settings.autoplay = true

    // update the HLS url
    this.$el.attr('data-hls-url', $newVideo.attr('data-hls-url'))

    // update the seek time
    this.$el.attr('data-seek-seconds', $newVideo.attr('data-seek-seconds') || '')

    // re-init
    this.init()
  }
}
