export default class Input {
  constructor(element) {
    this.element = element
    this.input = this.element.querySelector(
      '.' + this.element.classList[0] + '-input'
    )
    this.bindEvents()
  }

  bindEvents() {
    // Add validate handlers
    if (this.requiresValidation()) {
      this.input.addEventListener('blur', () => {
        setTimeout(() => this.validate(), 10)
      })
    }
  }

  validate() {
    const element = this.element
    const validity = this.input.validity

    if (validity.valid) {
      element.removeAttribute('data-error')
      element.removeAttribute('data-error-message')
      element.setAttribute('data-valid', 'true')
      return
    }

    // TODO: localize....
    let error = 'error'
    let errorMessage = 'Field contains an invalid value.'
    if (validity.badInput) {
      error = 'badInput'
      console.log('bad input')
    } else if (validity.patternMismatch) {
      error = 'patternMismatch'
      console.log('pattern mismatch')
    } else if (validity.rangeOverflow) {
      error = 'rangeOverflow'
      console.log('range overflow')
    } else if (validity.rangeUnderflow) {
      error = 'rangeUnderflow'
      console.log('range underflow')
    } else if (validity.stepMismatch) {
      error = 'stepMismatch'
      console.log('step mistamtch')
    } else if (validity.tooLong) {
      error = 'tooLong'
      errorMessage = 'Field is too long.'
    } else if (validity.tooShort) {
      error = 'tooShort'
      errorMessage = 'Field is too short.'
    } else if (validity.typeMismatch) {
      error = 'typeMismatch'
      errorMessage = 'Field does not match correct type.'
    } else if (validity.valueMissing) {
      error = 'valueMissing'
      errorMessage = 'Field is required.'
    }

    element.removeAttribute('data-valid')
    element.setAttribute('data-error', error)
    element.setAttribute('data-error-message', errorMessage)
  }

  requiresValidation() {
    const input = this.input
    return input.hasAttribute('required')
  }
}
