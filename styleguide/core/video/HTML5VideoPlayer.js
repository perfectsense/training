import {VideoPlayer} from './VideoPlayer.js'

export class HTML5VideoPlayer extends VideoPlayer {
  selectors = {
    video: '.HTML5VideoPlayer-video'
  }

  player = null

  get videoDuration () {
    if (this.player.readyState === 0) {
      return 0
    }

    return this.player.duration
  }

  constructor (el) {
    super(el)
    super.init()

    this.init()
  }

  init () {
    this.player = this.$el.find(this.selectors.video)
    this.player
        .off()
        .bind('play', (e) => {
          super.onVideoStart(e)
        })
        .bind('pause', (e) => {
          super.onVideoPause(e)
        })
        .bind('timeupdate', (e) => {
          super.onVideoTimeUpdate({
            secondsElapsed: e.timeStamp / 1000
          })
        })
        .bind('ended', (e) => {
          super.onVideoEnd()
        })
  }

  play () {
    this.player.play()
  }

  pause () {
    this.player.pause()
  }

  updateView ($newVideo) {
    super.updateView($newVideo)

    // update the video source
    this.$el.html($newVideo.html())
    this.init()
  }
}
