import { VideoPlayer } from '../video/VideoPlayer'
import { loadVimeoAPI } from './loadVimeoAPI'

export default class VimeoVideoPlayer extends VideoPlayer {
  async connectedCallback() {
    super.connectedCallback()

    try {
      await loadVimeoAPI()
    } catch (error) {
      console.error(error.message)
    }

    this._player = new window.Vimeo.Player(this.querySelector('iframe'))

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
