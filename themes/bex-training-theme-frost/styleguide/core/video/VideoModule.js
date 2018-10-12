import $ from 'jquery'

export class VideoModule {

  get $ctx () {
    return this._$ctx
  }

  set $ctx ($el) {
    this._$ctx = $el
  }

  get selectors () {
    return this.settings.selectors
  }

  constructor (_$ctx, options = {}) {
    this.$ctx = _$ctx

    this.settings = $.extend(
      {
        selectors: {
          playlistItem: '.PlaylistItem',
          player: '.VideoModule-player',
          videoModule: '.VideoModule',
          videoPagePlayer: '.VideoPage-player'
        }
      },
    options)
    this.init()
  }

  init () {
    // sets the on now state for the first playlist item in the video module
    $(this.$ctx).find(this.selectors.playlistItem).first().attr('data-selected', '')

    $(this.$ctx).find(this.settings.selectors.playlistItem).on({
      'click': (event) => {
        event.stopPropagation()
        event.preventDefault()
        $('.PlaylistItem').removeAttr('data-selected')
        $(event.currentTarget).attr('data-selected', '')
        let ajaxUrl = $(event.currentTarget).attr('data-ajax-url')
        this.getSelected(event.currentTarget, ajaxUrl)
      }
    })
  }

  // When playlist Item is clicked from the Video Module
  getSelected (promo, url) {
    if (url) {
      // disableScroll(scrollableContainer)
      $(this.selectors.player).html('')
      this.fetchVideoModuleView(url)
    }
  }

  fetchVideoModuleView (url) {
    $.ajax({
      url: url,
      async: false
    })
    .done((response) => {
      var content = $(response).find(this.selectors.videoPagePlayer)
      content.addClass('VideoModule-player')
      content.removeClass('VideoPage-player')
      this.updateVideoModuleView(content)
    })
    .fail((data) => {
      console.error('"VideoModule.fetchVideoModuleView()" failed')
    })
  }

  updateVideoModuleView ($html) {
    if ($(this.selectors.player).length) {
      $(this.selectors.player).replaceWith($html)
      this.$ctx = $html.find(this.selectors.videoModule)
      return
    }
  }
}
