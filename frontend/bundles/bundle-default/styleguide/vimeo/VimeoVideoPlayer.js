import { VideoPlayer } from '../video/VideoPlayer'

export class VimeoVideoPlayer extends VideoPlayer {
  async connectedCallback() {
    super.connectedCallback()

    const { default: VimeoPlayer } = await import('@vimeo/player')

    this._player = new VimeoPlayer(this.querySelector('iframe'))

    this._player.ready().then(() => {
      this.onReady()
    })

    this._player.on('playing', () => {
      this.onPlay()
    })

    this._player.on('ended', () => {
      this.onEnded()
    })

    this._player.on('pause', () => {
      this.onPause()
    })
  }

  play() {
    try {
      this._player.play()
    } catch (err) {
      console.log(err)
    }
  }
}
