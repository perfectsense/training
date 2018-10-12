import plugins from 'pluginRegistry'

// we have to assume that the video players are already all registered somewhere
// else, as they do not have any events or anything for us to listen to

export class VideoLead {
  constructor (container) {
    this.el = container
    this.videoPlayer = this.el.querySelector('[data-video-player]')
    this.poster = this.el.querySelector('.VideoLead-poster-content')
    this.playButton = this.el.querySelector('.VideoLead-play')

    if (this.poster) {
      if (this.whatKindOfPlayer() === 'HTML5') {
        let plugin = (plugins.scope(this.videoPlayer))

        // need a better method for getting the actual API vs the first prop
        // if there are two plugins on this element, this could fail
        for (var prop in plugin) {
          this.videoApi = plugin[prop]
        }
      }

      this.bindPlayButton()
    }
  }

  whatKindOfPlayer () {
    if (this.videoPlayer.classList.contains('HTML5VideoPlayer')) {
      return 'HTML5'
    }

    // in case the plyr got initialized or somehow a straight youtube iframe made it in
    if (this.el.querySelector('iframe')) {
      if (this.el.querySelector('iframe').getAttribute('src').indexOf('youtube') > 0) {
        return 'YouTube'
      }
    }

    if (this.videoPlayer.classList.contains('YouTubeVideoPlayer')) {
      return 'YouTube'
    }

    return false
  }

  bindPlayButton () {
    this.playButton.addEventListener('click', () => {
      this.setPlayState()

      if (this.whatKindOfPlayer() === 'HTML5') {
        let video = this.videoPlayer.querySelector('video')

        if (video) {
          video.play()
        }
      }

      // if we are YouTube, we are working with plyr, so use their API to play the video
      if (this.whatKindOfPlayer() === 'YouTube') {
        let plyrVideo = this.el.querySelector('.plyr')

        if (plyrVideo) {
          if (plyrVideo.plyr) {
            plyrVideo.plyr.play()
          }
        }
      }

      return false
    })
  }

  setPlayState () {
    this.el.setAttribute('data-playing-video', true)
  }
}
