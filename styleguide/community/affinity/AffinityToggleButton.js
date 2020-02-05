const ACTION_PARAMETER = 'action'
const ADD_ACTION = 'ADD'
const REMOVE_ACTION = 'REMOVE'
const ACTIVE_STATE = 'ACTIVE'
const INACTIVE_STATE = 'INACTIVE'
const TARGET_PARAMETER = 'target'
const TYPE_PARAMETER = 'type'

const fetch = window.fetch
const FormData = window.FormData

/**
 * Used by affinity button implementations with only two states (e.g. on/off)
 */
export default class AffinityToggleButton {

  constructor (element) {
    this.element = element
    this.submitUrl = element.getAttribute('data-url')
    this.authUrl = element.getAttribute('data-auth-url')
    this.bindEvents()
  }

  bindEvents () {
    this.element.addEventListener('click', (event) => this.submit(event))
  }

  createFormData () {
    const formData = new FormData()
    if (this.element.getAttribute('data-state') === ACTIVE_STATE) {
      formData.append(ACTION_PARAMETER, REMOVE_ACTION)
    } else {
      formData.append(ACTION_PARAMETER, ADD_ACTION)
    }
    formData.append(TARGET_PARAMETER, this.element.getAttribute('data-target'))
    formData.append(TYPE_PARAMETER, this.element.getAttribute('data-type'))
    return formData
  }

  beforeSubmit () {
    // toggle action/state
    const currentState = this.element.getAttribute('data-state')
    if (currentState === INACTIVE_STATE) {
      this.element.setAttribute('data-state', ACTIVE_STATE)
    } else {
      this.element.setAttribute('data-state', INACTIVE_STATE)
    }

    // increment/decrement count
    const countElement = this.element.querySelector('[class$=count]')
    if (!countElement) {
      return
    }

    const countText = countElement.textContent
    if (!countText) {
      return
    }

    if (currentState === INACTIVE_STATE) {
      countElement.textContent = Number.parseInt(countText) + 1
    } else {
      countElement.textContent = Number.parseInt(countText) - 1
    }
  }
  submit () {
    if (this.authUrl && this.authUrl.length > 0) {
      // redirect to authentication login flow
      window.location.href = this.authUrl
    } else {
      // ajax call to affinity servlet
      let body = this.createFormData()
      this.beforeSubmit()
      fetch(this.submitUrl, {
        method: 'POST',
        credentials: 'include',
        body: body
      }).then((response) => {
        if (response.status !== 200) {
          throw new Error(response.statusText)
        }

        return response.text()
      }).then((html) => {
        this.afterSubmit(html)
      }).catch((error) => {
        console.log(error)
      })
    }
  }

  afterSubmit (html) {
    const temporaryTemplate = document.createElement('template')
    temporaryTemplate.innerHTML = html

    const newHtmlNode = temporaryTemplate.content.firstElementChild
    this.element.parentNode.replaceChild(newHtmlNode, this.element)
  }
}
