import {GoogleTrackableType} from './GoogleTrackableType'
import {throttle} from 'styleguide/core/util/Throttle.js'

export class GoogleScrollingAnalytics {

  constructor (element) {
    this.element = element
    this.document = document
    // calculation variables
    this.h = document.documentElement
    this.b = document.body
    this.percentScrolled = 0.0
    // optimization bools
    this.firstQuartile = false
    this.secondQuartile = false
    this.thirdQuartile = false
    this.fourthQuartile = false
    this.init()
  }

  init () {
    window.addEventListener('scroll', (event) => {
      throttle(500, this.checkScroll(event))
    })
  }

  send (data) {
    GoogleTrackableType.sendEvent({
      category: 'scroll tracking',
      action: data,
      label: this.document.title,
      value: null })
  }

  checkScroll (event) {
    if (!this.fourthQuartile) {
      this.percentScrolled = ((this.h['scrollTop'] || this.b['scrollTop']) / ((this.h['scrollHeight'] || this.b['scrollHeight']) - this.h.clientHeight) * 100)
      if (this.percentScrolled >= 100) {
        this.fourthQuartile = true
        this.send('scroll 100%')
        return
      } else if (!this.thirdQuartile && this.percentScrolled >= 75) {
        this.thirdQuartile = true
        this.send('scroll 75%')
        return
      } else if (!this.secondQuartile && this.percentScrolled >= 50) {
        this.secondQuartile = true
        this.send('scroll 50%')
        return
      } else if (!this.firstQuartile && this.percentScrolled >= 25) {
        this.firstQuartile = true
        this.send('scroll 25%')
        return
      }
    }
  }
}
