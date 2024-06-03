import CookieJar from '../../util/CookieJar'

export default class HiddenCookieInput extends HTMLElement {
  get cookieName() {
    return this.getAttribute('cookie-name')
  }

  get input() {
    return this.querySelector('input')
  }

  updateValue() {
    this.input.value = CookieJar.getCookie(this.cookieName)
  }

  connectedCallback() {
    this.updateValue()
  }
}
