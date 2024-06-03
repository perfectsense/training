import { VideoPlayer } from '../video/VideoPlayer'
import { loadYouTubeAPI } from './loadYouTubeAPI'

export default class YouTubeVideoPlayer extends VideoPlayer {
  async connectedCallback() {
    super.connectedCallback()

    try {
      await loadYouTubeAPI()
    } catch (error) {
      console.error(error.message)
    }

    this._player = new window.YT.Player(this.querySelector('iframe'), {
      events: {
        onReady: () => this.onReady(),
        onStateChange: (event) => this.onStateChange(event),
      },
    })
  }

  onStateChange(event) {
    const state = event.data

    if (state === window.YT.PlayerState.ENDED) {
      this.onEnded()
    }

    if (state === window.YT.PlayerState.PLAYING) {
      this.onPlay()
    }

    if (state === window.YT.PlayerState.PAUSED) {
      this.onPause()
    }
  }

  play() {
    try {
      this._player.playVideo()
    } catch (err) {
      console.log(err)
    }
  }
}
