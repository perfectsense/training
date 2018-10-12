// IE9+ alternative to jQuery's ready()
// http://youmightnotneedjquery.com/#ready
export function ready (callback) {
  if (document.attachEvent ? document.readyState === 'complete' : document.readyState !== 'loading') {
    callback()
  } else {
    document.addEventListener('DOMContentLoaded', callback)
  }
}
