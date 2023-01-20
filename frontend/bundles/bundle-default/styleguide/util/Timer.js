export class Timer {
  timerId
  start
  remaining

  constructor(callback, delay) {
    this.callback = callback
    this.delay = delay
    this.remaining = this.delay
    this.timerId = ''
    this.resume()
  }

  pause() {
    window.clearTimeout(this.timerId)
    this.remaining -= Date.now() - this.start
  }

  resume() {
    this.start = Date.now()
    window.clearTimeout(this.timerId)
    this.timerId = window.setTimeout(this.callback, this.remaining)
  }

  cancel() {
    window.clearTimeout(this.timerId)
    this.remaining = this.delay
  }
}
