export default class LanguageMenu extends HTMLElement {
  connectedCallback() {
    this.navDropdown()
    this.switchLanguage()
  }

  navDropdown() {
    const moreLink = this.querySelector('.LanguagesMenu')

    moreLink.addEventListener('click', () => {
      const items = this.querySelector('.LanguagesMenu-items')
      if (items.classList.contains('is-active')) {
        items.classList.remove('is-active')
      } else {
        items.classList.add('is-active')
      }
    })
  }

  switchLanguage() {
    const moreLink = this.querySelector('.LanguagesMenu-text-link')
    const languageItems = this.querySelectorAll(
      '.LanguagesMenuItem-items-item-link'
    )

    languageItems.forEach((language) => {
      language.addEventListener('click', () => {
        const languageCode = language.getAttribute('language-code')
        moreLink.innerHTML = languageCode
      })
    })
  }
}
