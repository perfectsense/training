import $ from 'jquery'

export class PlaylistItem {
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

  get isPlaylistItem () {
    return this.$ctx[0].hasAttribute('data-promo')
  }

  constructor (_$ctx, options = {}) {
    this.$ctx = _$ctx

    this.settings = $.extend(
      {
        selectors: {
          belowContent: '.VideoPage-below',
          companionAnchor: '.CompanionAnchor',
          companionContentItem: '.CompanionContentItem',
          companions: '.PlaylistItem-companions',
          companionToasterContent: '.CompanionHeader-toasterContent',
          footer: '.Footer',
          moreAnchor: '.MoreAnchor',
          playlist: '.Playlist',
          scrollableContent: '.ScrollableContentContainer',
          videoPageCompanions: '.VideoPage-companions',
          videoContent: '.VideoPage-content'
        }
      },
    options)
    this.blockName = options.blockName || 'PlaylistItem'
    this.init()
  }

  init () {
    if (!this.$ctx) {
      return
    }
  }

  getCompanions () {
    let isSelected = false
    for (var i = 0; i < this.$ctx[0].attributes.length; i++) {
      if (this.$ctx[0].attributes[i].name === 'data-selected') {
        isSelected = true
      }
    }

    if (isSelected) {
      if (this.$ctx.find(this.selectors.companions)) {
        $(this.selectors.companionContentItem).on({
          'click': (event) => {
            event.preventDefault()
            event.stopPropagation()
            let $companion = $(event.currentTarget)
            // make sure that companion content is not in a flyout,
            // flyouts use the select button for this functionality
            if (!$companion.parent(this.selectors.companionToasterContent).length) {
              this.$ctx.trigger('Playlist:onCompanionContentItemClick', {
                url: $companion.attr('data-ajax-url'),
                mediaType: $companion.attr('data-media-type')
              })
            }
          }
        })
      }
    }
  }

  select () {
    if ((this.$ctx.data('type', 'video') || this.$ctx.data('type', 'Video'))) {
      this.$ctx.trigger('PlaylistItem:onSelect', {
        $promo: this.$ctx,
        ajaxUrl: this.ajaxUrl
      })
    }
  }
}
