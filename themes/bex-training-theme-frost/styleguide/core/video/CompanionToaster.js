import $ from 'jquery'

export class CompanionToaster {

  set companionDuration (duration) {
    this._companionDuration = duration
  }

  get companionDuration () {
    return this._companionDuration
  }

  get selectors () {
    return this.settings.selectors
  }

  constructor ($context, options) {
    this.$context = $context
    this.settings = $.extend({}, {
      selectors: {
        headerLogo: '.Header-logo',
        headerButton: '.Header-button',
        toaster: '.CompanionToaster',
        toasterClose: '.CompanionToaster-close',
        toasterProgressValue: '.CompanionToaster-value',
        toasterContent: '.CompanionToaster-content',
        CompanionContentItem: '.CompanionContentItem',
        CompanionContentItemSelect: '.CompanionContentItem-select',
        progress: '.CompanionToaster-progress',
        dropdownButton: '.Dropdown-item',
        scrollableContent: '.ScrollableContentContainer'
      },
      attributes: {

      }
    }, options)

    this.init(this.$context)
  }

  init ($el) {
    this.companionDuration = 10 // in seconds
    // toggle header menu on smaller breakpoints
    $(this.selectors.headerButton).on({
      'click': (event) => {
        // let searchInput = this.selectors.headerSearch + ' input[type="search"]'
        let $el = this.$context
        if ($el.attr('data-menu') === 'expand') {
          $el.attr('data-menu', 'collapse')
          $('body').css('overflow', 'auto')
        } else {
          $el.attr('data-menu', 'expand')
          $('body').css('overflow', 'hidden')
        }
      }
    })

    // reset to default state after mouse event
    $(this.selectors.headerContainer).on({
      'mouseleave': (event) => {
        let $el = this.$context
        let timer
        let logoEl = $(this.selectors.headerLogo).get()
        let buttonEl = $(this.selectors.headerButton).get()
        window.clearTimeout(timer)

        if (this.$context.attr('data-menu') === 'expand') {
          if (!(logoEl.indexOf(event.relatedTarget) > -1 || buttonEl.indexOf(event.relatedTarget) > -1)) {
            this.$context.attr('data-menu', 'collapse')
            timer = window.setTimeout(function () {
              $el.removeAttr('data-menu')
            }, 500)
          }
        }
      }
    })

    // toggle dropdown on smaller breakpoints
    $(this.selectors.dropdownButton).on({
      'click': (event) => {
        let $this = $(event.target)
        if ($this.parent().attr('data-submenu') === 'open') {
          $this.parent().attr('data-submenu', 'close')
        } else {
          $this.parent().attr('data-submenu', 'open')
        }
      }
    })

    // toggle dropdown on smaller breakpoints
    $(this.selectors.toasterClose).on({
      'click': (event) => {
        this.removeToast()
      }
    })

    // Remove data-menu attribute between smaller and larger breakpoints
    $(window).on({
      'resize': (event) => {
        if ($(window).width() > 985) {
          this.$context.removeAttr('data-menu')
        }
      }
    })

    if ($('[data-background]').length) {
      $(this.selectors.scrollableContent).attr('data-background', 'true')
    }
  }

  delayExpired ($context) {
    $($context.settings.selectors.toasterContent).empty()
    $(this.selectors.toaster).removeAttr('data-animate')
    $(this.selectors.toaster).hide()
  }

  updateProgressbar (timetotal) {
    let progressBarWidth = $(this.selectors.progress).width()
    $(this.selectors.toasterProgressValue).animate({width: progressBarWidth}, { easing: 'linear', duration: timetotal })
  }

  removeToast () {
    // empty out the toaster
    $(this.selectors.toasterContent + ', ' + this.selectors.progress).empty()
    $(this.selectors.toaster).removeAttr('data-animate')
    $(this.selectors.toaster).hide()
  }

  popToast ($companion, isFirstCompanionItem, isCompanionScreenOpen) {
    this.removeToast()
    // insert the toast
    $(this.selectors.toaster).attr('data-block', '')
    $(this.selectors.toaster).show()
    $companion.clone(true, true).prependTo($(this.selectors.toasterContent))

    // reset progress bar
    $(this.selectors.progress).append('<div class="CompanionToaster-value"></div>')
    $(this.selectors.toaster).attr('data-animate', '')

    if (this.companionDelayTimer) {
      clearTimeout(this.companionDelayTimer)
    }

    // start duration for promo companion
    if (this.companionDuration) {
      let totaltime = this.companionDuration * 1000
      this.updateProgressbar(totaltime)

      this.companionDelayTimer = setTimeout(() => {
        // for desktop version, automatically open first companion content for any
        // asset that has companion content. if there is already companion content
        // being viewed, do not open companion content
        // eslint-disable-next-line
        // if (breakpoint() === 'mq-md' || breakpoint() === 'mq-lg' || breakpoint() === 'mq-xl' || breakpoint() === 'mq-xxl') {
        //   if (isFirstCompanionItem === true && !isCompanionScreenOpen) {
        //     $(this.selectors.toasterContent).find(this.selectors.CompanionContentItem).click()
        //   } else {
        //     this.delayExpired(this)
        //   }
        // }

        this.delayExpired(this)
      }, totaltime)
    }
  }

}
