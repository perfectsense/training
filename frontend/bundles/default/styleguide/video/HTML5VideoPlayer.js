import { VideoPlayer } from './VideoPlayer'

const READY_STATE = {
  HAVE_NOTHING: 0,
  HAVE_METADATA: 1,
  HAVE_CURRENT_DATA: 2,
  HAVE_FUTURE_DATA: 3,
  HAVE_ENOUGH_DATA: 4,
}

export default class HTML5VideoPlayer extends VideoPlayer {
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
        this.play()
      })
    }

    this.videoElement.addEventListener('click', (event) => {
      event.preventDefault()
      this.playPause()
    })
  }

  removePlayButton() {
    if (this.playButton) {
      this.playButton.remove()
    }
  }

  play() {
    this.videoElement.play()
  }

  playPause() {
    if (this.videoElement.paused) {
      this.videoElement.play()
    } else {
      this.videoElement.pause()
    }
  }

  onPlay() {
    this.removePlayButton()
    this.videoPlayer.setAttribute('controls', '')
    super.onPlay()
  }
}
