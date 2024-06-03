import '../../util/scrollIntoViewIfNeeded'

export default class GoogleCaptcha extends HTMLElement {
  static DATA_FORM_DISABLE_SELECTOR = 'data-form-disabled'
  static RECAPTCHA_UNCHECKED_CLASSNAME = 'CaptchaInput-error'
  static RECAPTCHA_ENABLE_EVENT = new Event('recaptchaEnable')
  static RECAPTCHA_DISABLE_EVENT = new Event('recaptchaDisable')
  submissionInProgress = false

  get invisible() {
    return this.getAttribute('data-size') === 'invisible'
  }

  get form() {
    return this.closest('form')
  }

  static loadRecaptcha() {
    return new Promise((resolve, reject) => {
      const scriptTag = document.createElement('script')
      scriptTag.setAttribute(
        'src',
        'https://www.google.com/recaptcha/api.js?onload=recaptchaLoaded'
      )
      scriptTag.setAttribute('async', 'true')

      window.recaptchaLoaded = resolve

      window.recaptchaEnable = () =>
        document.dispatchEvent(GoogleCaptcha.RECAPTCHA_ENABLE_EVENT)

      window.recaptchaDisable = () =>
        document.dispatchEvent(GoogleCaptcha.RECAPTCHA_DISABLE_EVENT)

      scriptTag.onerror = () => {
        reject(new Error('Failed to load reCAPTCHA script'))
      }

      document.querySelector('head').appendChild(scriptTag)
    })
  }

  connectedCallback() {
    GoogleCaptcha.loadRecaptcha().then(() => {
      this.onRecaptchaDisable()
    })

    document.addEventListener(GoogleCaptcha.RECAPTCHA_ENABLE_EVENT.type, this)
    document.addEventListener(GoogleCaptcha.RECAPTCHA_DISABLE_EVENT.type, this)
    this.form.addEventListener('submit', this, true)
  }

  disconnectedCallback() {
    document.removeEventListener(
      GoogleCaptcha.RECAPTCHA_ENABLE_EVENT.type,
      this
    )
    document.removeEventListener(
      GoogleCaptcha.RECAPTCHA_DISABLE_EVENT.type,
      this
    )
    if (this.form) this.form.removeEventListener('submit', this, true)
  }

  handleEvent(event) {
    if (event.type === 'submit') {
      this.onFormSubmit(event)
    }

    if (event === GoogleCaptcha.RECAPTCHA_ENABLE_EVENT) {
      this.onRecaptchaEnable()
    }

    if (event === GoogleCaptcha.RECAPTCHA_DISABLE_EVENT) {
      this.onRecaptchaDisable()
    }
  }

  onRecaptchaEnable() {
    this.form.toggleAttribute(GoogleCaptcha.DATA_FORM_DISABLE_SELECTOR, false)
    this.classList.remove(GoogleCaptcha.RECAPTCHA_UNCHECKED_CLASSNAME)

    if (this.submissionInProgress) {
      this.form.dispatchEvent(new Event('submit'))
      this.submissionInProgress = false
    }
  }

  onRecaptchaDisable() {
    this.form.toggleAttribute(GoogleCaptcha.DATA_FORM_DISABLE_SELECTOR, true)
    this.submissionInProgress = false
  }

  onFormSubmit(event) {
    if (window.grecaptcha && window.grecaptcha.getResponse().length !== 0) {
      return
    }

    if (this.invisible) {
      this.submissionInProgress = true
      window.grecaptcha.execute()
    }

    this.blockSubission(event)
  }

  blockSubission(event) {
    event.preventDefault()
    event.stopImmediatePropagation()
    event.stopPropagation()

    if (!this.invisible) {
      this.classList.add(GoogleCaptcha.RECAPTCHA_UNCHECKED_CLASSNAME)
      this.scrollIntoViewIfNeeded(false)
    }
  }
}
