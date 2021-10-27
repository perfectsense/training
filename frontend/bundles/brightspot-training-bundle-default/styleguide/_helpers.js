/* global Handlebars:false */

Handlebars.registerHelper('concat', function (string1, string2) {
  return string1 + string2
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

Handlebars.registerHelper('newsletterLink', function (
  str,
  source,
  medium,
  campaign,
  content,
  customTracking
) {
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
})
