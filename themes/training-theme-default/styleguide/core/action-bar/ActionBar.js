export class ActionBar extends window.HTMLElement {
  connectedCallback () {
    this.querySelector('[data-dropdown-trigger]').addEventListener(
      'click',
      e => {
        e.preventDefault()
        this.toggleDropdown()
      },
      true
    )

    this.querySelector('[data-dropdown-close]').addEventListener(
      'click',
      e => {
        e.preventDefault()
        this.hideDropdown()
      },
      true
    )

    // Accessibility: escape key hides dropdown
    document.addEventListener('keydown', e => {
      let isEscape = false

      if ('key' in e) {
        isEscape = e.key === 'Escape' || e.key === 'Esc'
      } else {
        isEscape = e.keyCode === 27
      }

      if (isEscape) {
        this.hideDropdown()
      }
    })

    this.copyLinkFunctionality()
  }

  copyLinkFunctionality () {
    let copyLink = this.querySelector('[data-social-service="copylink"]')

    if (copyLink) {
      copyLink.addEventListener(
        'click',
        e => {
          e.preventDefault()

          let tempTextarea = document.createElement('textarea')
          document.body.appendChild(tempTextarea)
          tempTextarea.value = window.location.href
          tempTextarea.select()
          document.execCommand('copy')
          document.body.removeChild(tempTextarea)

          copyLink.setAttribute('data-copied', true)
        },
        true
      )
    }
  }

  toggleDropdown () {
    if (this.isDropdownShowing()) {
      this.hideDropdown()
    } else {
      this.showDropdown()
    }
  }

  isDropdownShowing () {
    let dropdown = this.querySelector('.ActionBar-dropdown')

    if (dropdown) {
      if (dropdown.getAttribute('data-showing') === 'true') {
        return true
      } else {
        return false
      }
    } else {
      return false
    }
  }

  showDropdown () {
    this.querySelector('.ActionBar-dropdown').setAttribute('data-showing', true)
  }

  hideDropdown () {
    this.querySelector('.ActionBar-dropdown').setAttribute(
      'data-showing',
      false
    )
  }

  disconnectedCallback () {
    return false
  }
}
