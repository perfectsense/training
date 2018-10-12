import $ from 'jquery'

export class VideoPage {

  $ctx ($el) {
    this.$ctx = $el
  }

  get selectors () {
    return this.settings.selectors
  }

  set permalink (str) {
    this.$ctx.attr('data-permalink', str)
  }

  get permalink () {
    return this.$ctx.attr('data-permalink')
  }

  get videoId () {
    return this.$ctx.attr('data-video-id')
  }

  get videoUrl () {
    return this.$video.attr('data-hls-url')
  }

  get $video () {
    return this.$ctx.find('video')
  }

  get videoPlayer () {
    return this._videoPlayer
  }

  set videoPlayer (player) {
    this._videoPlayer = player
  }

  constructor ($ctx, options = {}) {
    this.$ctx = $ctx

    this.settings = $.extend({}, {
      selectors: {
        companionContentItem: '.CompanionContentItem',
        videoPageContent: '.VideoPage-content',
        belowContent: '.VideoPage-below',
        updateTarget: '.ScrollableContentContainer'
      }
    }, options)
  }

  fetchView (url) {
    $.ajax({
      url: url,
      async: false
    })
    .done((response) => {
      var content = $(response).find(this.selectors.updateTarget)
      this.updateView(content)
      this.$ctx.trigger('VideoPage:onViewReplaced', {})
    })
    .fail((data) => {
      console.error('VideoPagefetchView() failed')
    })
  }

  updateVideoUrl (url) {
    this.$ctx.trigger('VideoPage:onUpdateVideoUrl', {
      videoUrl: url
    })
  }

  updateView ($html) {
    if (this.$ctx) {
      let $newVideoPageMain = $html.find('.VideoPage-main')
      this.$ctx.attr('id', $newVideoPageMain.attr('id'))
      this.$ctx.attr('data-video-id', $newVideoPageMain.attr('data-video-id'))
      this.$ctx.attr('data-full-video-id', $newVideoPageMain.attr('data-full-video-id'))
      this.videoPlayer = $newVideoPageMain.find('[data-video-player]')
      this.$ctx.find('[data-video-player]').replaceWith(this.videoPlayer)
      this.$ctx.find('.VideoPage-content').replaceWith($html.find('.VideoPage-content'))
      this.$ctx.find('.VideoSaveButtons').replaceWith($html.find('.VideoSaveButtons'))
    } else {
      if ($(this.selectors.updateTarget).length) {
        $(this.selectors.updateTarget).replaceWith($html)
        this.$ctx = $html.find('.VideoPage-main')
        return
      }
    }
    this.init()
  }

  resetVideo () {
    this.$ctx.add('body').removeAttr('data-video-min')
  }

  minimizeVideo () {
    this.$ctx.add('body').attr('data-video-min', '')
  }
}
