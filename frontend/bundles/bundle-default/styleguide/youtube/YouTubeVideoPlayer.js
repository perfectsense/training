import { VideoPlayer } from '../video/VideoPlayer'

export class YouTubeVideoPlayer extends VideoPlayer {
  async connectedCallback() {
    super.connectedCallback()

    await this.loadYouTubeVideoPlayerAPI()

    this._player = new window.YT.Player(this.querySelector('iframe'), {
      events: {
        onReady: () => this.onReady(),
        onStateChange: (event) => this.onStateChange(event),
      },
    })
  }

  loadYouTubeVideoPlayerAPI() {
    return new Promise((resolve) => {
      if (window.YouTubeAPIReady) {
        return resolve()
      } else {
        const tag = document.createElement('script')
        const firstScriptTag = document.getElementsByTagName('script')[0]

        const onYouTubeReady = () => {
          window.YouTubeAPIReady = true
          window.dispatchEvent(new window.Event('YouTubeApiReady'))
        }

        tag.src = 'https://www.youtube.com/iframe_api'
        firstScriptTag.parentNode.insertBefore(tag, firstScriptTag)

        window.onYouTubeIframeAPIReady = onYouTubeReady
      }

      window.addEventListener('YouTubeApiReady', resolve)
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
