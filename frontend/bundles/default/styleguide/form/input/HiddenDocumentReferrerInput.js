export default class HiddenDocumentReferrerInput extends HTMLElement {
  get input() {
    return this.querySelector('input')
  }

  connectedCallback() {
    this.input.value = document.referrer
  }
}
