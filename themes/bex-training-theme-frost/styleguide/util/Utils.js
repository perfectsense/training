export function isPDKloaded () {
  if (typeof $pdk !== 'undefined' && window.$pdk.controller.ready) {
    return true
  }
  return false
}

export function isTouchScreen () {
  return (('ontouchstart' in window) || (navigator.MaxTouchPoints > 0) || (navigator.msMaxTouchPoints > 0))
}

export function breakpoint () {
  // eslint-disable-next-line
  return window.getComputedStyle(document.querySelector('body'), ':before').getPropertyValue('content').replace(/\"/g, '')
}

export function parseQueryString () {
  try {
    var a = window.location.search.split(/\?/)
    var b = a[1].split('&')
    var c = {}
    for (var i = 0; i < b.length; i++) {
      var d = b[i].split('=')
      c[d[0]] = d[1]
    }
    return c
  } catch (ex) {
    return null
  }
}

export function isMobileUA () {
  var userAgent = navigator.userAgent || navigator.vendor || window.opera

  var kindleStrings = [
    'Kindle',
    'Silk',
    'KFTT',
    'KFOT',
    'KFJWA',
    'KFJWI',
    'KFSOWI',
    'KFTHWA',
    'KFTHWI',
    'KFAPWA',
    'KFAPWI',
    'KFASWI',
    'KFTBWI',
    'KFMEWI',
    'KFFOWI',
    'KFSAWA',
    'KFSAWI',
    'KFARWI' ]

  let isKindle = false

  for (let index = 0; index < kindleStrings.length; index++) {
    let matchRegExp = new RegExp(kindleStrings[index])
    if (matchRegExp.test(userAgent)) {
      isKindle = true
      break
    }
  }

  if (userAgent.match(/iPad/i) || userAgent.match(/iPhone/i) || userAgent.match(/iPod/i)) {
    return 'iOS'
  } else if (userAgent.match(/Android/i)) {
    return 'Android'
  } else if (isKindle) {
    return 'Kindle'
  } else {
    return false
  }
}
