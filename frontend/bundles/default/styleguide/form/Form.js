import { debounce } from '../util/Debounce'

export default class BSPForm extends HTMLElement {
  connectedCallback() {
    this.form = this.querySelector('form')
    if (!this.form) return
    this.init()
  }

  init() {
    if (!this.form) return

    this.form.addEventListener(
      'keyup',
      debounce(1000, ({ target }) => {
        this.setAttribute('invalid-form', 'false')
        this.validateField(target)
      })
    )

    this.form.addEventListener('submit', (evt) => {
      evt.preventDefault()
      this.setAttribute('invalid-form', 'false')
      Array.from(this.form.elements).forEach((input) => {
        this.validateField(input)
      })
      // if form is valid, submit
      if (this.getAttribute('invalid-form') === 'false') {
        this.submitForm()
      }
    })
  }

  submitForm() {
    // when there is an external action, the action attribute is set, so
    // the form is submitted normally. If there is no action, then we are
    // expected to do regular data collection and submit via ajax
    //
    // in that case we will submit to the current URL, and then check the
    // response for success or failure. If the response is a 200, we are
    // displaying the success message and hiding the form. No need to parse
    // the HTML. If the response is not a 200, we are displaying an error
    // message and keeping the form visible to be submitted again

    if (!this.form) return
    const action = this.form.getAttribute('action')

    if (action !== null) {
      // if the form has a target, we will submit it normally and it will honor the target
      this.form.submit()
    } else {
      const formData = new FormData(this.form)
      // this is always there, even if its blank, as it just returns the current URL
      // which is what we actually want
      const url = new URL(this.form.action)

      // sanitize the URL, to make sure there is no SSRF possibility
      if (url.protocol !== 'https:' && url.protocol !== 'http:') {
        console.error('URL parsing error:', url)
        return
      }

      this.setLoadingState()

      fetch(url.href, {
        method:
          this.form.getAttribute('method')?.toString().toUpperCase() || 'POST',
        body: formData,
      })
        .then((response) => {
          if (response.ok) {
            this.setSuccessState()
          } else {
            console.error('Error:', response)
            this.setErrorState()
          }
        })
        .catch((error) => {
          console.error('Error:', error)
          this.setErrorState()
        })
    }
  }

  setLoadingState() {
    this.setAttribute('form-loading', 'loading')
  }

  removeLoadingState() {
    this.removeAttribute('form-loading')
  }

  setErrorState() {
    this.removeLoadingState()
    this.setAttribute('form-status', 'error')
  }

  setSuccessState() {
    this.removeLoadingState()
    this.setAttribute('form-status', 'success')
  }

  validateField(el) {
    if (el.parentElement) {
      el.parentElement.parentElement.removeAttribute('data-invalid')
      const validity = el.validity
      if (validity.valid) {
        return
      }
      el.reportValidity()
      this.setAttribute('invalid-form', 'true')
      el.parentElement.parentElement.setAttribute('data-invalid', 'true')
    }
  }

  disconnectedCallback() {
    return false
  }
}
