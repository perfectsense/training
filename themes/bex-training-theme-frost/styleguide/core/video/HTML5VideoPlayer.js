import $ from 'jquery'
import {VideoPlayer} from 'videoPlayer'

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
        .bind('timeupdate', (e) => {
          this.onVideoTimeUpdate({
            secondsElapsed: e.elapsedSeconds
          })
        })
  }

  onVideoTimeUpdate (event) {
    const secondsElapsed = parseInt($(this.selectors.video)[0].currentTime)
    this.$el.trigger('VideoMain:onPlaybackTimeUpdate', {
      playerId: this.playerId,
      video: this.video,
      secondsElapsed: secondsElapsed * 1000
    })

    this.sendVideoPlaybackMetricEvents(secondsElapsed)
  }
}
