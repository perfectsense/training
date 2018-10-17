import $ from 'jquery'
import Plyr from '../../../vendor-assets/plyr/plyr.js'
import {bindAll} from '../../util/bindAll.js'

const SEL_DATAVIDEOPLAYER = 'data-video-player'          // selector for video player container
const SEL_DATA_PLYR_STATE = 'data-plyrhelper-state'

export class PlyrFunctions {

  constructor (el) {
    this.elVidContainer = el.hasAttribute(SEL_DATAVIDEOPLAYER) ? el : el.querySelector(`[${SEL_DATAVIDEOPLAYER}]`)
    if (this.elVidContainer) {
      bindAll(this, ['evLoadedData', 'evTimeUpdate'])
    }

    this.plyr = new Plyr(this.elVidContainer)

    // add the API to the DOM element for later use
    this.plyr.on('ready', event => {
       // we've had some edge cases where this is undefined at this point, and we get an error
      if (el.querySelector('.plyr')) {
        el.querySelector('.plyr').plyr = event.detail.plyr
      }
    })

    this.init()
  }

  init () {
    this.setBinds()
  }

  setBinds () {
    this.evLoadedData()
    this.plyr.on('loadeddata', this.evLoadedData)
    this.plyr.on('timeupdate', this.evTimeUpdate)
  }

  onStateChange (state) {
    if (this.elWrapper) {
      this.elWrapper.setAttribute(SEL_DATA_PLYR_STATE, state)
    }
  }

  evLoadedData () {
    this.onStateChange('ready')
  }

  evTimeUpdate () {
    this.onStateChange('timeupdate')
    $('.plyr').trigger('VideoMain:onPlaybackTimeUpdate', {
      secondsElapsed: parseInt(this.plyr.currentTime) * 1000
    })
  }
}
