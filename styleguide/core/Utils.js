export function isTouchScreen () {
  return (('ontouchstart' in window) || (navigator.MaxTouchPoints > 0) || (navigator.msMaxTouchPoints > 0))
}

export function breakpoint () {
  return window.getComputedStyle(document.querySelector('body'), ':before').getPropertyValue('content').replace(/"/g, '')
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

export function camelCaseToSeparator (str, separator = '-') {
  let strDashes = ''
  str = str.charAt(0).toLowerCase() + str.slice(1)
  let i = 0
  while (i <= (str.length - 1)) {
    if (str.charAt(i) === str.charAt(i).toUpperCase()) {
      strDashes += separator + str.charAt(i).toLowerCase()
    } else {
      strDashes += str.charAt(i)
    }
    i++
  }
  return strDashes
}
export function methodNamesFromClassAndParents (classToParse, last = Object, filter = /.*/, filterPrivate = /^_.*/) {
  let obj = classToParse
  let props = []
  do {
    if (!obj) {
      break
    }
    props = props.concat(Object.getOwnPropertyNames(obj))
    obj = Object.getPrototypeOf(obj)
  } while (last !== Object.getPrototypeOf(this))
  return props
    .sort()
    .filter((propertyName) => {
      return typeof Object[propertyName] === 'undefined'
    })
    .filter((propertyName) => {
      return typeof classToParse[propertyName] === 'function'
    })
    .filter((elem, pos, arr) => {
      return arr.indexOf(elem) === pos
    })
    .filter((propertyName) => {
      return filter.test(propertyName)
    })
    .filter((propertyName) => {
      return !filterPrivate.test(propertyName)
    })
}
