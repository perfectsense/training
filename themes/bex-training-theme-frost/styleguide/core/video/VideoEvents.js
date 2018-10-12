import $ from 'jquery'
import plugins from 'pluginRegistry'
import { debounce } from 'debounce'
import { CompanionScreen } from './CompanionScreen.js'
import { CompanionToaster } from './CompanionToaster.js'
import { HTML5VideoPlayer } from './HTML5VideoPlayer.js'
import { isTouchScreen } from '../../util/Utils.js'
import { Playlist } from './Playlist.js'
import { PlaylistItem } from './PlaylistItem.js'
import { VideoModule } from './VideoModule.js'
import { VideoPage } from './VideoPage.js'
import { YouTubeVideoPlayer } from '../../youtube/YouTubeVideoPlayer.js'

plugins.register(HTML5VideoPlayer, '.HTML5VideoPlayer')
plugins.register(VideoModule, '.VideoModule')
plugins.register(YouTubeVideoPlayer, '.YouTubeVideoPlayer')

$(document).ready(function () {
  this.currentlyPlayingVideoScopeId = null

  let $body = $('body')
  let playlist
  let companionScreen
  let toaster
  let scrollableContainer = '.ScrollableContentContainer'

  if (isTouchScreen()) {
    $body.attr('data-is-touch', '')
  }

  enableScroll(scrollableContainer)

  $('body').on({

    'CompanionScreen:onClose': (event, data) => {
      playlist.closeNav()
    },

    'CompanionScreen:onMinimize': (event, data) => {
      window.videoMain.resetVideo()
    },

    'CompanionScreen:onMaximize': (event, data) => {
      // data media type == article
      window.videoMain.minimizeVideo()
    },

    // VideoPromo is selected
    'PlaylistItem:onSelect': (event, data) => {
      if (data.ajaxUrl) {
        $body.attr('data-fadeout', true)
        disableScroll(scrollableContainer)
        // empties out content from article or gallery
        $(scrollableContainer).html('')
        window.videoMain = new VideoPage(null, { updateTarget: '.ScrollableContentContainer' })
        window.videoMain.fetchView(data.ajaxUrl)
        // get Companion content after fetchView
        let playItem = new PlaylistItem(data.$promo, { })
        playItem.getCompanions()
      }
    },

    // When a companion promo is clicked within playlist
    'Playlist:onCompanionContentItemClick': (event, data) => {
      companionScreen.fetchSecondScreen(data.url, data.mediaType)
    },

    // Check for companion Promo
    'Playlist:onCompanionChecked': (event, data) => {
      toaster.popToast(data.companion, data.isFirstCompanionItem, companionScreen.isOpen())
    },

    'Promo:onScrollEnd': (event, data) => {
      disableScroll(scrollableContainer)
      // select the next playlist item
      playlist.selectNext()
    },

    'ScrollableContent:loaded': (event, data) => {
      // set attributes to new scrollable content
      // styles are dependant on the attr
      $('.ScrollableContentContainer').attr({'data-background': '', 'data-playlistoffset': ''})

      enableScroll(scrollableContainer)
      $(scrollableContainer).animate({ scrollTop: 0 }, 0)
      setTimeout(function (e) {
        $body.removeAttr('data-fadeout')
      }, 100)
    },

    // When the videoMain's view is replaced by an ajax request
    'VideoPage:onViewReplaced': (event, data) => {
      console.log('The VideoMain view has been replaced')
    },

    // While a video is playing this is periodically broadcast with the current timestamp
    'VideoMain:onPlaybackTimeUpdate': (event, data) => {
      // check for existence of playlist before checking for companion content
      if (playlist) {
        playlist.checkForCompanion(data.secondsElapsed)
      }
    }
  })

  // VideoMain binding
  let $videoMain = $('.VideoPage-main')
  if ($videoMain.length) {
    window.videoMain = new VideoPage($videoMain, { })
  }

  let $toaster = $('.CompanionToaster')
  if ($toaster.length) {
    toaster = new CompanionToaster($toaster, { })
  }

    // Playlist (on right)
  let $playlist = $('.Playlist')
  if ($playlist.length) {
    playlist = new Playlist($playlist, {toaster})
    $('.ScrollableContentContainer').attr('data-playlistoffset', '')
    $('.Companion-controls').attr('data-controls-showing', '')
  }

  // SecondScreen - Companion Screen
  let $companionScreen = $('.CompanionScreen')
  if ($companionScreen.length) {
    companionScreen = new CompanionScreen($companionScreen, { })
  }

  $('iframe').each(function () {
    let srcAttr = $(this).attr('src')
  //  IC-143: if the iframe's source looks like a YouTube url
    if (srcAttr && srcAttr.indexOf('youtube.com') > 0) {
      let wmode = 'wmode=transparent'
      if (srcAttr.indexOf('?') !== -1) {
        $(this).attr('src', srcAttr + '&' + wmode)
      } else {
        $(this).attr('src', srcAttr + '?' + wmode)
      }
    }
  })

  function disableScroll (scrollArea) {
    $(scrollArea).off('scroll')
  }

  function enableScroll (scrollArea) {
    $(scrollArea).off('scroll').on('scroll', debounce(700, function (e) {
      if ($(e.target).scrollTop() + $(e.target).innerHeight() >= $(e.target)[0].scrollHeight) {
        $(this).trigger('Promo:onScrollEnd', {})
      }
    }))
  }
})
