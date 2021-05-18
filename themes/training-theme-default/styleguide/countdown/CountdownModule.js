export class CountdownModule extends window.HTMLElement {
  connectedCallback () {
    this.countdown = this.querySelector('.CountdownModule-countdown')
    this.countdownDays = this.querySelector('.CountdownModule-countdown-days')

    this.countdownDays.innerHTML = this.numberOfDaysUntilEnd
  }

  get endDate () {
    return this.countdown.getAttribute('data-end-date')
  }

  get numberOfDaysUntilEnd () {
    let currentDate = new Date()

    if (this.endDate) {
      return Math.round((this.endDate - currentDate) / 86400000)
    }
  }
}
