/* eslint-disable no-unused-vars */
// IEPolyfills.js
import './polyfill/All.js'
import picturefill from 'picturefill'
import cssVars from 'css-vars-ponyfill'

cssVars({
  include: '[data-cssvarsponyfill="true"]',
})

// Polyfill CustomEvent, including this here as its so small vs separate import
function CustomEvent(event, params) {
  params = params || { bubbles: false, cancelable: false, detail: null }
  const evt = document.createEvent('CustomEvent')
  evt.initCustomEvent(event, params.bubbles, params.cancelable, params.detail)
  return evt
}

if (typeof window.CustomEvent !== 'function') {
  CustomEvent.prototype = window.Event.prototype

  window.CustomEvent = CustomEvent
}
