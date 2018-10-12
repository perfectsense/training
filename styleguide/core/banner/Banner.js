import $ from 'jquery'

/**
 * This is not using the Toggle utility as it has local storage for closing the bar
 */

export class BannerSettings {
  constructor () {
    this.toggleTrigger = 'data-toggle-trigger'
    this.toggleItem = 'data-toggle-item'
    this.toggleAttribute = 'data-toggle-in'
    this.sessionKey = 'BannerStatus'
  }
}

export class Banner {
  constructor (el) {
    this.$el = $(el)
    this.settings = new BannerSettings()

    this._checkForOpening()
    this._bindCloseButton()

    this.$el.data('api', this)
  }

  _bindCloseButton () {
    this.$el.find('[' + this.settings.toggleTrigger + ']').on('click', () => {
      this.closeBar()
    })
  }

  _shouldBarBeOpen () {
    let value = window.sessionStorage.getItem(this.settings.sessionKey)

    if (value) {
      return false
    } else {
      return true
    }
  }

  _checkForOpening () {
    if (this._shouldBarBeOpen()) {
      this.openBar()
    }
  }

  openBar () {
    this.$el.attr(this.settings.toggleAttribute + '-banner', true)
    $('body').attr(this.settings.toggleAttribute + '-banner', true)
  }

  closeBar () {
    window.sessionStorage.setItem(this.settings.sessionKey, 'true')

    this.$el.removeAttr(this.settings.toggleAttribute + '-banner', true)
    $('body').removeAttr(this.settings.toggleAttribute + '-banner', true)
  }
}
