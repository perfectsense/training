import Input from './Input.js'

export default class TextArea extends Input {
  requiresValidation() {
    const input = this.input
    return (
      super.requiresValidation() ||
      input.getAttribute('maxlength') ||
      input.getAttribute('minlength')
    )
  }
}
