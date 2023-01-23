import { VideoPlayer } from './VideoPlayer'

const READY_STATE = {
  HAVE_NOTHING: 0,
  HAVE_METADATA: 1,
  HAVE_CURRENT_DATA: 2,
  HAVE_FUTURE_DATA: 3,
  HAVE_ENOUGH_DATA: 4,
}

export class HTML5VideoPlayer extends VideoPlayer {
  connectedCallback() {
    super.connectedCallback()

    this.playButton = this.querySelector('.HTML5VideoPlayer-playIcon')
    this.videoPlayer = this.querySelector('.HTML5VideoPlayer-video')

    this.videoElement.addEventListener('play', () => {
      this.onPlay()
    })

    this.videoElement.addEventListener('canplay', () => {
      this.onReady()
    })

    this.videoElement.addEventListener('timeupdate', () => {
      this.onTimeUpdate()
    })

    this.videoElement.addEventListener('volumechange', () => {
      this.onVolumneChange()
    })

    this.videoElement.addEventListener('error', () => {
      this.onError()
    })

    this.videoElement.addEventListener('ended', () => {
      this.onEnded()
    })

    this.videoElement.addEventListener('pause', () => {
      this.onPause()
    })

    this.handleClicks()
  }

  get videoElement() {
    return this.querySelector('video')
  }

  get ready() {
    return this.videoElement.readyState >= READY_STATE.HAVE_CURRENT_DATA
  }

  handleClicks() {
    if (this.playButton) {
      this.playButton.addEventListener('click', () => {
        this.hidePlayButton()
        this.videoPlayer.setAttribute('controls', '')
        this.play()
      })
    }

    this.videoPlayer.addEventListener('click', () => {
      this.hidePlayButton()
      this.videoPlayer.setAttribute('controls', '')
      this.playPause()
    })
  }

  hidePlayButton() {
    if (this.playButton) {
      this.playButton.setAttribute('style', 'display:none;')
    }
  }

  play() {
    this.videoElement.play()
  }
}
