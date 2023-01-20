import Input from './Input.js'

export default class PasswordInput extends Input {
  requiresValidation() {
    const input = this.input
    return (
      super.requiresValidation() ||
      input.getAttribute('maxlength') ||
      input.getAttribute('minlength') ||
      input.getAttribute('pattern')
    )
  }
}
