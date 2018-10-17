// import $ from 'jquery'

export default class PageHeaderSearch {
  constructor ($el, options = {}) {
    this.$el = $el
    this.init()
  }

  init () {
    let $button = this.$el.find('button')
    let $input = this.$el.find('input')

    // using attr / removeAttr instead .prop so that CSS
    // can also be applied
    $input.on('focus mouseenter keyup blur mouseleave', () => {
      let $value = $input.val()
      if ($value && $value.length > 0) {
        $button.removeAttr('disabled')
      } else {
        $button.attr('disabled', '')
      }
    })

    $button.on('mouseenter', () => {
      let $value = $input.val()
      if ($value && $value.length > 0) {
        $input.addClass('PageHeader-search-input-wide')
      } else {
        $input.removeClass('PageHeader-search-input-wide')
      }
    })
  }
}
