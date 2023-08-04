/* global Handlebars:false */
/* eslint-disable no-undef */

Handlebars.registerHelper('concat', function () {
  const stringArray = []
  for (let i = 0; i < arguments.length - 1; i++) {
    stringArray.push(arguments[i])
  }
  return stringArray.join('')
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
