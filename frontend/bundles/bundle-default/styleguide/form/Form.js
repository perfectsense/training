export class BSPForm extends window.HTMLElement {
  connectedCallback() {
    this.cacheElements()
    this.init()
  }

  cacheElements() {
    this.form = this.querySelector('form')
    this.formSubmit = this.querySelector('button[type=submit]')
    this.inputs = this.querySelector('form').elements
  }

  init() {
    let timeout = null
    // on Keyup
    this.form.addEventListener(
      'keyup',
      (evt) => {
        clearTimeout(timeout)
        this.setAttribute('invalid-form', 'false')
        timeout = setTimeout(() => {
          this.validateField(evt.target)
        }, 1000)
      },
      true
    )

    // on submit
    this.formSubmit.addEventListener(
      'click',
      (evt) => {
        evt.preventDefault()
        this.setAttribute('invalid-form', 'false')
        Array.from(this.querySelector('form').elements).forEach((input) => {
          this.validateField(input)
        })
        // if form is valid, submit
        if (this.getAttribute('invalid-form') === 'false') {
          this.form.submit()
        }
      },
      true
    )
  }

  validateField(el) {
    el.parentElement.parentElement.removeAttribute('data-invalid')
    const validity = el.validity
    if (validity.valid) {
      return
    }
    el.reportValidity()
    this.setAttribute('invalid-form', 'true')
    el.parentElement.parentElement.setAttribute('data-invalid', 'true')
  }

  disconnectedCallback() {
    return false
  }
}
