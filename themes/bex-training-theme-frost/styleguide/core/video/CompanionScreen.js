import $ from 'jquery'

export class CompanionScreen {

  get $ctx () {
    return this._$ctx
  }

  get ajaxUrl () {
    return this._$ctx.attr('data-ajax-url')
  }

  set $ctx ($el) {
    this._$ctx = $el
  }

  get selectors () {
    return this.settings.selectors
  }

  constructor ($ctx, options = { }) {
    this.$ctx = $ctx

    this.settings = $.extend(
      {
        selectors: {
          companionScreen: '.CompanionScreen',
          screenContent: '.CompanionScreen-content',
          screenControls: '.CompanionScreen-controls',
          drawerOpen: 'data-is-open',
          controlsBackButton: '.CompanionScreen-controls-backButton',
          controlsCloseButton: '.CompanionScreen-controls-closeButton',
          controlsExpandButton: '.CompanionScreen-controls-expandButton',
          scrollableContainer: '.ScrollableContentContainer'
        }
      },
    options)

    this.init(this.$ctx)
  }

  init ($el) {
    // 2nd screen "close" button
    $(this.selectors.controlsCloseButton).on({
      'click': (event) => {
        this.$ctx
            .removeAttr(this.selectors.drawerOpen)
            .removeAttr('data-media-type')
        $('body').removeAttr('data-secondscreen-is-open')

        $(this.selectors.screenContent).empty()

        this.$ctx.removeAttr('data-article-max')
        this.$ctx.trigger('CompanionScreen:onMinimize', { })
        this.$ctx.find(this.selectors.controlsExpandButton + ' span').text('EXPAND')
      }
    })

    // 2nd screen "back" button
    $(this.selectors.controlsBackButton).on({
      'click': (event) => {
        this.$ctx
            .removeAttr(this.selectors.drawerOpen)
            .removeAttr('data-media-type')
        $(this.selectors.screenContent).empty()
        $('body').removeAttr('data-secondscreen-is-open')

        this.$ctx.trigger('CompanionScreen:onClose', { })
        this.$ctx.removeAttr('data-article-max')
        this.$ctx.trigger('CompanionScreen:onMinimize', { })
      }
    })

    // Expand button toggle
    $(this.selectors.controlsExpandButton).on({
      'click': (event) => {
        this.toggleSecondSecreen()
      }
    })
  }

  toggleSecondSecreen () {
    if (this.$ctx.attr('data-article-max') === 'true') {
      this.$ctx.removeAttr('data-article-max')
      this.$ctx.trigger('CompanionScreen:onMinimize', { })
      this.$ctx.find(this.selectors.controlsExpandButton + ' span').text('EXPAND')
    } else {
      this.$ctx.attr('data-article-max', 'true')
      this.$ctx.trigger('CompanionScreen:onMaximize', { })
      this.$ctx.find(this.selectors.controlsExpandButton + ' span').text('COLLAPSE')
      // Fix Carousel size on Companion Screen Expanded View
      if ($('.Carousel').length > 0) {
        $('.Carousel').resize()
      }
    }
  }

  // check to see if user is currently viewing something in companion screen
  isOpen () {
    let open = this.$ctx.attr(this.selectors.drawerOpen)

    // For some browsers, `attr` is undefined; for others,
    // `attr` is false.  Check for both.
    return (typeof open !== typeof undefined && open !== false)
  }

  fetchSecondScreen (url, mediaType) {
    $.ajax({
      url: url
    })
    .done((data) => {
      var scrollContainer = $(data).find(this.selectors.scrollableContainer)
      let $ctxContent = this.$ctx.find(this.selectors.screenContent)

      $ctxContent
          .empty()
          .append(scrollContainer)
      window.scrollTo(0, 0)

      // this.$ctx.find('.MPXVideoPlayer').each((index, value) => {
      //   this.videoPlayer = new MPXVideoPlayer($(value), {
      //     autoplay: false,
      //     companionPlayer: true
      //   })
      //   window.registerPlayer(this.videoPlayer, this.videoPlayer.playerId)
      // })

      setTimeout(() => {
        this.$ctx
            .attr(this.selectors.drawerOpen, true)
            .attr('data-media-type', mediaType)
        $('body').attr('data-secondscreen-is-open', '')
        this.$ctx.attr('data-second-screen', true)
      }, 250)
    })
    .fail((data) => {
      console.error('second-screen load failed')
    })
  }

}
