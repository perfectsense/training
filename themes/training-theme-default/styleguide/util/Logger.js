export const LOG_VARNAME = 'ps-logger-level'
export const LOG_LEVEL_PRODUCTION = 0
export const LOG_LEVEL_VERBOSE = 1

/**
 * Class which proxies the Console API (i.e. console.log), but only sends output
 * to the console if the log level set through a sessionStorage property
 * matches the correct level.
 *
 * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console|MDN Console API Documentation}
 *
 * @example
 * // Unlike console.log, Logger methods are chainable. To only
 * // show a few messages local to your module, you could do this:
 * Logger
 *  .enableVerboseLogging()
 *  .log('I am a message')
 *  .warn('I am a warning')
 *  .error('I am an error')
 *  .disableVerboseLogging()
 */
export default class Logger {
  /**
   * Disable verbose logging (messages and warnings)
   * @returns {Logger}
   */
  static disableVerboseLogging () {
    window.sessionStorage.setItem(LOG_VARNAME, LOG_LEVEL_PRODUCTION)
    return Logger
  }

  /**
   * Enable verbose logging (messages and warnings)
   * @returns {Logger}
   */
  static enableVerboseLogging () {
    window.sessionStorage.setItem(LOG_VARNAME, LOG_LEVEL_VERBOSE)
    return Logger
  }

  /**
   * @returns {Number}
   */
  static currentLogLevel () {
    let level = window.sessionStorage.getItem(LOG_VARNAME) * 1
    return level
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/assert|Console.assert}
   * @returns {Logger}
   */
  static assert () {
    send('assert', arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/clear|Console.clear}
   * @returns {Logger}
   */
  static clear () {
    send('clear', arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/count|Console.count}
   * @returns {Logger}
   */
  static count () {
    send('count', arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/debug|Console.debug}
   * @returns {Logger}
   */
  static debug () {
    Logger.log.apply(null, arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/dir|Console.dir}
   * @returns {Logger}
   */
  static dir () {
    send('dir', arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/dirxml|Console.dirxml}
   * @returns {Logger}
   */
  static dirxml () {
    send('dirxml', arguments)
    return Logger
  }

  /**
   * {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/error|Console.error}
   * @returns {Logger}
   */
  static error () {
    send('error', arguments, LOG_LEVEL_PRODUCTION)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/exception|Console.exception}
   * @returns {Logger}
   */
  static exception () {
    Logger.error.apply(null, arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/group|Console.group}
   * @returns {Logger}
   */
  static group () {
    send('group', arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/groupCollapsed|Console.groupCollapsed}
   * @returns {Logger}
   */
  static groupCollapsed () {
    send('groupCollapsed', arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/groupEnd|Console.groupEnd}
   * @returns {Logger}
   */
  static groupEnd () {
    send('groupEnd', arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/info|Console.info}
   * @returns {Logger}
   */
  static info () {
    send('info', arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/log|Console.log}
   * @returns {Logger}
   */
  static log () {
    send('log', arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/profile|Console.profile}
   * @returns {Logger}
   */
  static profile () {
    send('profile', arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/profileEnd|Console.profileEnd}
   * @returns {Logger}
   */
  static profileEnd () {
    send('profileEnd', arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/table|Console.table}
   * @returns {Logger}
   */
  static table () {
    send('table', arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/time|Console.time}
   * @returns {Logger}
   */
  static time () {
    send('time', arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/timeEnd|Console.timeEnd}
   * @returns {Logger}
   */
  static timeEnd () {
    send('timeEnd', arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/timeStamp|Console.timeStamp}
   * @returns {Logger}
   */
  static timeStamp () {
    send('timeStamp', arguments)
    return Logger
  }

  /**
   * @returns {Logger}
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/trace|Console.trace}
   */
  static trace () {
    send('trace', arguments)
    return Logger
  }

  /**
   * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Console/warn|Console.warn}
   * @returns {Logger}
   */
  static warn () {
    send('warn', arguments)
    return Logger
  }

  /**
   * Writes a colorful welcome message to the console
   */
  static welcomeMessage () {
    let colorMarker = `%c`
    if (isIE()) {
      colorMarker = ''
    }
    let body = `${colorMarker}To enable verbose logging from Perfect Sense Javascript, set the session storage variable ${colorMarker}ps-logger-level ${colorMarker}to 1`
    let styleTitleShared = `background-color: #eee; font-size: 18px`
    let styleTitleDefault = `${styleTitleShared}; color: #333333`
    let styleTitleBlueLetters = `${styleTitleShared}; color: #23c8ff`
    let styleTitleRedLetters = `${styleTitleShared}; color: #ff1e3c`
    let styleBodyShared = `background-color: #eee; font-size: 11px`
    let styleBodyDefault = `${styleBodyShared}; color: #333333`
    let styleBodyBold = `${styleBodyDefault}; font-weight: bold`
    if (isIE()) {
      console.log(body)
    } else {
      console.log(
        styleTitleDefault,
        styleTitleBlueLetters,
        styleTitleRedLetters,
        styleTitleBlueLetters,
        styleTitleRedLetters,
        styleTitleDefault
      )
      console.log(body, styleBodyDefault, styleBodyBold, styleBodyDefault)
    }
  }
}

/**
 * @ignore
 */
function isIE () {
  return /Trident\/|MSIE/.test(window.navigator.userAgent)
}

/**
 * @ignore
 */
function send (method, args, minLogLevel = LOG_LEVEL_VERBOSE) {
  if (window.console) {
    if (window.console[method] && Logger.currentLogLevel() >= minLogLevel) {
      window.console[method].apply(null, args)
    } else if (Logger.currentLogLevel() >= minLogLevel && window.console.log) {
      window.console.log.apply(null, args)
    }
  }
}
