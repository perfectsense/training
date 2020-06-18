export class VideoPlaylist extends window.HTMLElement {
  connectedCallback () {
    this.cacheElements()

    if (this.playlist) {
      this.handlePlaylistOpening()

      if (this.isVideoPage) {
        this.markCurrentPlaylistItem(window.location.href)
      }

      this.handlePlaylistItemClicks()
      this.reactToVideoEnd()
    }
  }

  cacheElements () {
    this.playlist = this.querySelector('[class$="-playlist"]')
    this.PlaylistItems = this.querySelectorAll('.PlaylistItem')

    if (this.isVideoPage) {
      this.videoWrapper = this.querySelector('[data-video-wrapper]')
    } else {
      this.videoWrapper = this.querySelector('[data-video-player-wrapper]')
    }
  }

  get isVideoPage () {
    if (document.querySelector('html').classList.contains('VideoPage')) {
      return true
    } else {
      return false
    }
  }

  // React to the Video Ended event. We grab the current item in the playlist and check for existence
  // of a next item, if it exists, we load it via our own API
  reactToVideoEnd () {
    this.querySelector('[data-video-player]').addEventListener(
      'Video:onVideoEnded',
      event => {
        let currentPlaylistItem = this.playlist.querySelector(
          '[data-current-item="true"]'
        )
        let nextItem = currentPlaylistItem.nextElementSibling

        if (!nextItem) {
          nextItem = currentPlaylistItem.parentElement.nextElementSibling.querySelector(
            '.PlaylistItem'
          )
        }

        if (nextItem) {
          // Sometimes we only show a few of the playlist items, do not play hidden ones
          if (nextItem.offsetParent !== null) {
            let nextItemUrl = nextItem.getAttribute('data-item-url')

            this.loadVideo(nextItemUrl)
          }
        }
      }
    )
  }

  // Handles clicks for each individual PlaylistItem. If you clicked on something that's not currently
  // playing, we load it. We also close the Playlist always, which just sets data attributes and the
  // desktop level CSS just ignores those
  handlePlaylistItemClicks () {
    this.PlaylistItems.forEach(item => {
      let itemUrl = item.getAttribute('data-item-url')

      item.addEventListener('click', event => {
        event.preventDefault()

        // only setup the loadvideo if there is a URL, but always suppress
        // the click, which is why this conditional is in here
        if (itemUrl) {
          if (item.getAttribute('data-current-item') !== 'true') {
            this.loadVideo(itemUrl)
            this.closePlaylist()
          }
        }
      })
    })
  }

  // While this sort of assumes that the videoWrapper is blank to look great, it doesn't need to be
  // as this will just append a loading icon and set the page in a loading state
  setLoadingState () {
    let loadingIcon = document.createElement('div')
    loadingIcon.classList.add('loading-icon')

    this.videoWrapper.appendChild(loadingIcon)
    this.setAttribute('data-loading', true)
  }

  removeLoadingState () {
    let loadingIcon = this.videoWrapper.querySelector('.loading-icon')
    loadingIcon.parentNode.removeChild(loadingIcon)
    this.removeAttribute('data-loading')
  }

  // Clears out the page to get it ready for a new upcoming video
  clearOldVideo () {
    this.videoWrapper.innerHTML = ''
  }

  // Helper function that calls API functions and deals with the getItem promise
  loadVideo (itemUrl) {
    this.clearOldVideo()
    this.setLoadingState()

    this.getItem(itemUrl).then(response => {
      this.removeLoadingState()
      this.insertVideo(response, itemUrl)
    })
  }

  // Simple feth helper
  getItem (itemUrl) {
    return new Promise((resolve, reject) => {
      window
        .fetch(itemUrl, {
          credentials: 'include'
        })
        .then(response => {
          resolve(response.text())
        })
        .catch(() => {
          reject()
        })
    })
  }

  // Helper function to deal with an incoming video. It filters the response to make sure we just
  // have the video itself and then also deals with history entries as it has all the data from
  // the response. Finally, after we render the video, we re-check the playlist to mark the new item
  insertVideo (response, url) {
    // filter HTML response
    let filterDiv = document.createElement('div')
    filterDiv.innerHTML = response

    let videoPageWrapperSelector = '[data-video-player-wrapper]'

    if (this.isVideoPage) {
      videoPageWrapperSelector = '[data-video-wrapper]'
    }

    let videoPageFromResponse = filterDiv.querySelector(
      videoPageWrapperSelector
    ).innerHTML

    if (videoPageFromResponse) {
      this.videoWrapper.innerHTML = videoPageFromResponse
    }

    if (this.isVideoPage) {
      // replace the title and URL after rendering
      let newTitle = filterDiv.querySelector('title').innerHTML

      window.history.replaceState({}, newTitle, url)
      document.title = newTitle
    }

    // mark the item current now that we have the right URL
    this.markCurrentPlaylistItem(url)

    // redo this event, as we have a new video player
    this.reactToVideoEnd()
  }

  // We always run this handler and the desktop media queries just ignore the data attributes
  handlePlaylistOpening () {
    if (this.playlist) {
      let playlistHeader = this.playlist.querySelector(
        '.VideoPage-playlist-header'
      )

      if (playlistHeader) {
        playlistHeader.addEventListener('click', () => {
          this.togglePlaylist()
        })
      }
    }
  }

  // this checks the playlist items against the current URL and marks the right one
  markCurrentPlaylistItem (url) {
    this.PlaylistItems.forEach(item => {
      item.removeAttribute('data-current-item')

      let itemUrl = item.getAttribute('data-item-url')

      if (itemUrl) {
        if (url.indexOf(itemUrl) > -1) {
          item.setAttribute('data-current-item', true)
        }
      }
    })
  }

  // API functions to deal with playlist opening and closing. These data attributes can always be set
  // and the desktop media queries just ignore them
  openPlaylist () {
    document.body.setAttribute('data-toggle-video-playlist', true)
    this.setAttribute('data-toggle-video-playlist', true)
  }

  closePlaylist () {
    document.body.removeAttribute('data-toggle-video-playlist')
    this.removeAttribute('data-toggle-video-playlist')
  }

  togglePlaylist () {
    if (document.body.getAttribute('data-toggle-video-playlist')) {
      this.closePlaylist()
    } else {
      this.openPlaylist()
    }
  }
}
