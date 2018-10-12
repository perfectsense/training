import $ from 'jquery'
import { PlaylistItem } from './PlaylistItem.js'
import Hammer from 'hammerjs'
import { isTouchScreen, breakpoint } from '../../util/Utils.js'

export class Playlist {

  $ctx ($el) {
    this._$ctx = $el
  }

  set playlistItems ($items) {
    this._playlistItems = $items
  }

  get isOpen () {
    if (this.$ctx[0].hasAttribute(`${this.selectors.drawerOpen}`)) {
      return true
    }
    return false
  }

  get attachedOnBottom () {
    return this._attachedOnBottom
  }

  set attachedOnBottom (bool) {
    this._attachedOnBottom = bool
  }

  get $currentDrawer () {
    return this.$ctx.find(`${this.selectors.selectedPlaylistItem}`)
  }

  set $currentDrawer ($el) {
    this._$currentDrawer = $el
  }

  get playlistItems () {
    return this._playlistItems
  }

  get selectors () {
    return this.settings.selectors
  }

  get $selectedPlaylistItem () {
    return this.$ctx.find(`${this.selectors.selectedPlaylistItem}`)
  }

  constructor ($ctx, options = { }) {
    this.$ctx = $ctx

    this.settings = $.extend(
      {
        selectors: {
          belowContent: '.VideoPage-below',
          companionAnchor: '.CompanionAnchor',
          companionContentItem: '.CompanionContentItem',
          companionContentItemSelect: '.CompanionContentItem-select',
          companionContentItemDismiss: '.CompanionContentItem-dismiss',
          playlistItem: '.PlaylistItem',
          drawerOpen: 'data-is-open',
          moreAnchor: '.MoreAnchor',
          playlistBody: '.Playlist-body',
          playlistTitle: '.Playlist-title',
          scrollableContent: '.ScrollableContentContainer',
          selectedPlaylistItem: '.PlaylistItem[data-selected]'
        }
      },
    options)

    if (this.$ctx.parent('.VideoPage-contentWrapper')) {
      this.init(this.$ctx)
    }
  }

  init ($el) {
    this.playlistItems = []

    // reset playlist on resize or landscape/portrait transition
    this.onResize()

    // in open state by default?
    if (this.isOpen) {
      this.openDrawer(this.$currentDrawer)
      // opens the first drawer by default on page load
      $(this.selectors.playlistItem).first().attr('data-selected', '')
      // this.$ctx.trigger('Playlist:onOpen', { })
      $('body').attr('data-playlist-is-open', '')
      // get companions for first playlist item
      let firstplaylistItem = $('.PlaylistItem').first()
      let playItem = new PlaylistItem(firstplaylistItem, {})
      playItem.getCompanions()
    }

    // on touch screens, setup gesture events
    if (isTouchScreen()) {
      let hammer = new Hammer(this.$ctx.find(`${this.selectors.playlistTitle}`)[0])
      hammer.get('swipe').set({ direction: Hammer.DIRECTION_VERTICAL })
      hammer.on('swipe', (ev) => {
        if (ev.direction === Hammer.DIRECTION_UP ||
            ev.direction === Hammer.DIRECTION_DOWN) {
          this.toggleNavigation()
        }
      })
    }

    if (breakpoint() === 'mq-sm' || breakpoint() === 'mq-md') {
      this.attachedOnBottom = true

      // initializes playlist at bottom of mobile devices
      // rather than popping up then minimizing on page load
      this.initNav()

      // bind clicks on main content for a drawer.
      // if clicked in mobile, close navigation so that
      // video can be viewed
      this.$ctx.find(this.selectors.playlistItem).click(() => {
        this.toggleNavigation(false)
      })
    }

    // open the current drawer by default
    this.openDrawer(this.$currentDrawer)

    // initialize playlist elements
    this.calcProgressBar($el)

    // Article / Video / Gallery Promos
    let $playlistItems = this.$ctx.find('.PlaylistItem')
    if ($playlistItems.length > 0) {
      $playlistItems.each((index, value) => {
        let $promo = $(value)
        this.playlistItems.push(new PlaylistItem($promo, { }))
      })
    }

    // bind the open and close buttons to the toggle method
    $(this.selectors.playlistTitle).on({
      'click': (event) => {
        event.preventDefault()
        event.stopPropagation()
        if (breakpoint() === 'mq-sm' || breakpoint() === 'mq-md') {
          this.toggleNavigation()
        }
      }
    })

    // binds click on PlaylistItem
    this.$ctx.find(this.selectors.playlistItem).on({
      'click': (event) => {
        event.preventDefault()
        this.selectPlaylistItem($(event.currentTarget), true)
        this.calcProgressBar($el)
      }
    })

    // binds click on Companion Promo
    this.$ctx.find(this.selectors.companionContentItem).on({
      'click': (event) => {
        event.preventDefault()
        event.stopPropagation()
        let $companion = $(event.currentTarget)
        // make sure that companion content is not in a flyout,
        // flyouts use the select button for this functionality
        if (!$companion.parent(this.selectors.toasterContent).length) {
          this.toggleNavigation(true)
          this.$ctx.trigger('Playlist:onCompanionContentItemClick', {
            url: $companion.attr('data-ajax-url'),
            mediaType: $companion.attr('data-media-type')
          })
          this.scrollToTop()
        }
      }
    })

    // bind clicks on Companion Select button
    this.$ctx.find(this.selectors.companionContentItemSelect).on({
      'click': (event) => {
        event.preventDefault()
        event.stopPropagation()

        // data is located on the companion promo, which
        // is the parent of the select button
        let $companion = $(event.currentTarget).parent()
        this.toggleNavigation(true)
        this.$ctx.trigger('Playlist:onCompanionContentItemClick', {
          url: $companion.attr('data-ajax-url'),
          mediaType: $companion.attr('data-media-type')
        })
      }
    })

    // bind clicks on Companion Toaster Dismiss button
    this.$ctx.find(this.selectors.companionContentItemDismiss).on({
      'click': (event) => {
        event.preventDefault()
        event.stopPropagation()
        // cancel and remove flyout, so that it does not fire
        // after being hidden
        this.selectors.pageHeader.removeToast()
      }
    })
  }

  onResize () {
    $(window).resize(() => {
      if (breakpoint() === 'mq-xs' || breakpoint() === 'mq-sm' || breakpoint() === 'mq-md') {
        this.attachedOnBottom = true
        this.initNav()
      } else {
        this.attachedOnBottom = false
        this.openNav()
      }
    })
  }

  closeNav () {
    this.$ctx.removeAttr(this.selectors.drawerOpen)
    $('body').attr('data-playlist-is-closed', '')
    $('body').removeAttr('data-playlist-is-open')
    this.$ctx.trigger('Playlist:onClose', { })

    if (this.attachedOnBottom) {
      this.$ctx.animate({
        'top': (window.innerHeight - 50) + 'px'
      }, 300)
    }
  }

  initNav () {
    this.$ctx.removeAttr(this.selectors.drawerOpen)
    $('body').attr('data-playlist-is-closed', '')
    $('body').removeAttr('data-playlist-is-open')
    this.$ctx.trigger('Playlist:onClose', { })

    if (this.attachedOnBottom) {
      this.$ctx.css({
        'top': (window.innerHeight - 50) + 'px'
      })
    }
  }

  openNav () {
    this.$ctx.attr(this.selectors.drawerOpen, '')
    $('body').attr('data-playlist-is-open', '')
    $('body').removeAttr('data-playlist-is-closed')
    this.$ctx.attr('data-is-open', '')
    this.$ctx.trigger('Playlist:onOpen', { })

    if (this.attachedOnBottom) {
      this.$ctx.animate({
        'top': `54px`
      }, 300)
    } else {
      // ensures that desktop playlist does not inherit mobile
      // 'top' value after resizing window
      this.$ctx.css('top', '0')
    }
  }

  toggleNavigation (forceOpen) {
    // close nav
    if (this.$ctx[0].hasAttribute(this.selectors.drawerOpen) && !forceOpen) {
      this.closeNav()
    } else {
      this.openNav()
    }
  }

  checkForCompanion (elapsedSeconds) {
    let context = this.$ctx
    let $companionItems = context.find(this.selectors.selectedPlaylistItem).find(this.selectors.companionContentItem)
    let numItemsSynced = $companionItems.length - $companionItems.not('[data-synced]').length
    let isFirstItem = numItemsSynced === 0

    // all companions that haven't been synced
    $companionItems.not('[data-synced]').each(function (event) {
      let $companionContentItem = $(this)
      let itemTimestamp = $companionContentItem.attr('data-timestamp-seconds')
      if (itemTimestamp) {
        // display the matched companion and come out of collapsed state
        if (+itemTimestamp === Math.floor(+elapsedSeconds)) {
          // mark as synced
          $companionContentItem.attr('data-synced', '')

          context.trigger('Playlist:onCompanionChecked', {
            companion: $companionContentItem,
            isFirstCompanionItem: isFirstItem
          })
        }
      }
    })
  }

  openDrawer ($drawer) {
    $drawer.attr(this.selectors.drawerOpen, '')
  }

  closeDrawer ($drawer) {
    $drawer.removeAttr(this.selectors.drawerOpen)
  }

  toggleDrawer ($drawer) {
    if (this.isDrawerOpen($drawer)) {
      this.closeDrawer($drawer)
    } else {
      this.openDrawer($drawer)
    }
  }

  isDrawerOpen ($drawer) {
    return $drawer[0].hasAttribute(this.selectors.drawerOpen)
  }

  calcProgressBar ($el) {
    let selected = this.getSelectedIndex($el)
    let navLength = this.getNavigationLength($el)
    let progressBarHeight = (1 / navLength) * 100
    let progressBarPosition = (selected / navLength) * 100
    $el.find(this.selectors.progressBar).css({'height': progressBarHeight + '%', 'top': progressBarPosition + '%'})
  }

  getSelectedIndex () {
    let selectPos = this.$ctx.find(`${this.selectors.playlistBody} ${this.selectors.playlistItem}`).index($(this.selectors.selectedPlaylistItem))
    if (selectPos <= 0) {
      selectPos = 0
    }
    return selectPos
  }

  getNavigationLength ($el) {
    return $el.find(this.selectors.playlistBody).children().length
  }

  selectPlaylistItem ($el, userInitiated) {
    // close current drawer
    this.closeDrawer(this.$currentDrawer)

    // un-select current selected drawer
    this.$selectedPlaylistItem.removeAttr('data-selected')

    // select this drawer
    $el.attr('data-selected', '')

    // open the drawer
    this.openDrawer($el)

    let selectedIndex = this.getSelectedIndex()

    if (userInitiated) {
      window.userInitiatedPlayback = true
    }

    // if previous video is a promo, play that first
    if (userInitiated && selectedIndex >= 1) {
      let previousPromo = this.playlistItems[selectedIndex - 1]
      if (previousPromo.isPlaylistItem) {
        let $previousItem = this.$ctx.find(`${this.selectors.playlistItem}`).eq(selectedIndex - 1)
        this.selectPlaylistItem($previousItem)
        return
      }
    }

    this.playlistItems[selectedIndex].select()
  }

  selectNext () {
    let currIndex = this.getSelectedIndex()
    let nextIndex = currIndex + 1
    let $nextItem = this.$ctx.find(`${this.selectors.playlistItem}`).eq(nextIndex)

    if ($nextItem.length <= 0) {
      $nextItem = this.$ctx.find(`${this.selectors.playlistItem}`).eq(0)
      nextIndex = 0
    }

    this.selectPlaylistItem($nextItem)
    this.calcProgressBar(this.$ctx)
  }

  scrollToTop () {
    window.scroll({
      top: 0,
      left: 0,
      behavior: 'smooth'
    })
  }
}
