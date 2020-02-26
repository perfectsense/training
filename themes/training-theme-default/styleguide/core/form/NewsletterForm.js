export class NewsletterForm extends window.HTMLElement {
  connectedCallback () {
    this.form = this.querySelector('form')

    this.form.addEventListener('submit', e => {
      e.preventDefault()

      this.submitForm()
    })

    this.handleClearButton()
  }

  handleClearButton () {
    let clearButton = this.querySelector('.NewsletterForm-success-more')

    clearButton.addEventListener('click', e => {
      e.preventDefault()

      this.clearForm()
    })
  }

  submitForm () {
    let apiUrl = this.form.getAttribute('action') || ''
    let data = new window.FormData(this.form)
    let stringData = Array.from(data, e =>
      e.map(encodeURIComponent).join('=')
    ).join('&')

    window
      .fetch(apiUrl, {
        credentials: 'include',
        method: 'POST',
        body: stringData,
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      })
      .then(response => {
        if (response.status === 200) {
          this.setFormSuccess()

          // Tracking event
          let customEvent = new window.CustomEvent('Newsletter:subscribe', {
            bubbles: true,
            detail: {
              email: data.get('email')
            }
          })
          this.dispatchEvent(customEvent)
        }
      })
      .catch(error => {
        console.log('Newsletter Form Error: ' + error)
        this.setFormError()
      })
  }

  clearForm () {
    this.form.removeAttribute('data-form-success')
    this.form.removeAttribute('data-form-error')

    let inputs = this.form.querySelectorAll('input')

    inputs.forEach(input => {
      input.value = ''
    })
  }

  setFormSuccess () {
    this.form.setAttribute('data-form-success', true)
  }

  setFormError () {
    this.form.setAttribute('data-form-error', true)
  }

  disconnectedCallback () {
    return false
  }
}
