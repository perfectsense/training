/* global Handlebars:false */
/* eslint-disable no-undef */

Handlebars.registerHelper('anyHasAllProperties', function (list) {
  var len = list.length

  for (var i = 0; i < len; i++) {
    var match = true

    for (var j = 1; j < arguments.length - 1; j++) {
      if (!list[i][arguments[j]]) {
        match = false
      }
    }

    if (match) {
      return true
    }
  }

  return false
})

Handlebars.registerHelper('concat', function () {
  const stringArray = []
  for (let i = 0; i < arguments.length - 1; i++) {
    stringArray.push(arguments[i])
  }
  return stringArray.join('')
})

Handlebars.registerHelper('contains', function (haystack, needle) {
  if (!haystack || !haystack.length || !needle || !needle.length) return false

  return haystack.indexOf(needle) > -1
})

Handlebars.registerHelper('containsCurrentChapter', function (chapter) {
  if (chapter.isCurrent) return true

  if (!chapter.chapters || chapter.chapters.length < 0) return false

  var isCurrent = false
  for (var i = 0; i < chapter.chapters.length; i++) {
    isCurrent = Handlebars.helpers.containsCurrentChapter(chapter.chapters[i])
    if (isCurrent) break
  }

  return isCurrent
})

Handlebars.registerHelper('svgPlaceholder', function (width, height) {
  const svg =
    '<svg xmlns="http://www.w3.org/2000/svg" version="1.1" height="' +
    (parseInt(height, 10) || 1) +
    'px" width="' +
    (parseInt(width, 10) || 1) +
    'px"></svg>'

  if (typeof Buffer !== 'undefined') {
    return 'data:image/svg+xml;base64,' + Buffer.from(svg).toString('base64')
  } else if (typeof btoa !== 'undefined') {
    return 'data:image/svg+xml;base64,' + btoa(svg)
  } else {
    return (
      'data:image/svg+xml;base64,' +
      java.util.Base64.encoder.encodeToString(
        svg.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8)
      )
    )
  }
})

Handlebars.registerHelper(
  'newsletterLink',
  function (str, source, medium, campaign, content, customTracking) {
    const params = []
    if (source) {
      params.push('utm_source=' + source)
    }
    if (medium) {
      params.push('utm_medium=' + medium)
    }
    if (campaign) {
      params.push('utm_campaign=' + campaign)
    }
    if (content) {
      params.push('utm_content=' + content)
    }
    if (customTracking) {
      params.push(customTracking)
    }

    if (params.length) {
      str =
        str.indexOf('?') >= 0
          ? str + '&' + params.join('&')
          : str + '?' + params.join('&')
    }

    return str
  }
)

// Test if an element is in an array
Handlebars.registerHelper('inArray', function (needle, haystack) {
  const haystackArray = JSON.parse(haystack)

  const length = haystackArray.length

  for (let i = 0; i < length; i++) {
    if (haystackArray[i] === needle) return true
  }

  return false
})

// For Search Results. Count the number of selected filters.
Handlebars.registerHelper('countSelected', function (context) {
  let count = 0

  // For Search Results Page and Search Results Module contexts
  if (context.filters) {
    const length = context.filters.length

    for (let i = 0; i < length; i++) {
      count += Handlebars.helpers.countSelected(context.filters[i])
    }

    return count
  }

  // For Search Filter contexts
  if (context.items) {
    const length = context.items.length

    for (let i = 0; i < length; i++) {
      if (context.items[i].selected) count++
    }

    return count
  }

  // For Search Filters array contexts
  if (context.length) {
    const length = context.length

    for (let i = 0; i < length; i++) {
      count += Handlebars.helpers.countSelected(context[i])
    }
  }

  return count
})

/**
 * Handlebars helper to get the path to an SVG icon.
 *
 * Explore available icons at https://phosphoricons.com/
 *
 * @function icon
 * @param {string} name - The name of the icon.
 * @param {string} [weight='regular'] - The weight of the icon. Acceptable values are 'thin', 'light', 'regular', 'bold', 'fill', and 'duotone'. Defaults to 'regular' if not specified.
 * @returns {string} The path to the icon's handlebars file.
 *
 * @example
 * // In Handlebars template:
 * {{include (iconPath 'facebook-logo')}}
 * {{include (iconPath 'twitter-logo' 'bold')}}
 */
Handlebars.registerHelper('iconPath', function () {
  const args = Array.prototype.slice.call(arguments)
  let name = args[0]
  let weight = args[1]

  if (typeof weight !== 'string') {
    weight = 'regular'
  }

  let path = '/_icons/'
  path += weight + '/'

  if (weight !== 'regular') {
    name = name + '-' + weight
  }
  path += name + '.hbs'

  return path
})

Handlebars.registerHelper('mergeObjects', function () {
  var merged = {}
  for (var i = 0; i < arguments.length; i++) {
    var obj = arguments[i]
    if (obj && typeof obj === 'object') {
      for (var prop in obj) {
        merged[prop] = obj[prop]
      }
    }
  }
  return merged
})
