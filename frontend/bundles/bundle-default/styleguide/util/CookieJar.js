export default class CookieJar {
  /**
   * Creates a cookie based on given params
   *
   * @param  {string}    name The name of the cookie
   * @param  {string}    value The value of the cookie
   * @param  {number}    UTC timestamp when cookie expires
   *
   * @returns {Object}    Returns true if element is in the viewport
   */
  static createCookie(name, value, timestamp, domain) {
    let expires = ''
    let dm = ''
    let date = ''

    if (timestamp) {
      date = new Date(parseInt(timestamp))
    } else {
      // sets the expiration date to 12/31/2030
      date = new Date(1924923600000)
    }

    expires = '; expires=' + date.toUTCString()

    if (domain) {
      dm = `;domain=${domain}`
    }
    document.cookie = name + '=' + value + expires + dm + '; path=/'
  }

  /**
   * Checks whether or not a cookie exists
   *
   * @param  {string}   name The name of the cookie
   *
   * @return {Object}   Returns the cookie as an object if it exists
   */
  static hasCookie(name) {
    return this.getCookie(name) != null
  }

  /**
   * Gets the cooke
   *
   * @param  {string}   name The name of the cookie
   *
   * @return {Object}   Returns the cookie as an object if it exists
   */
  static getCookie(name) {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'))
    if (match) return match[2]
  }

  /**
   * Removes cookie by expiring it
   *
   * @param  {string}   name The name of the cookie
   */
  static eraseCookie(name, domain) {
    // sets the expiration date to 01/01/1980
    this.createCookie(name, '', 315550800000, domain)
  }

  /**
   * If you want to just set the days and aren't worried about the date:
   *
   * @param  {number}   days from now
   *
   * Example: createCookie('cookieName', 'cookieValue', daysInUTC(30))
   *
   */
  static daysInUTC(days) {
    const date = new Date()
    return date.setDate(date.getDate() + parseInt(days))
  }
}
