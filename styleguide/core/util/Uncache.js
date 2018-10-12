import * as Dom from './Dom.js'

/* global fetch */
/* global URL */

export default new Promise((resolve, reject) => {
  Dom.ready(() => {
    const cacheMeta = document.querySelector('meta[name="brightspot.cached"]')
    if (!cacheMeta || cacheMeta.getAttribute('content') !== 'true') {
      return
    }

    const url = new URL(window.location.href)
    const params = {'_skipCacheControl': true}
    Object.keys(params).forEach(key => url.searchParams.append(key, params[key]))

    fetch(url, {
      method: 'GET',
      credentials: 'include'
    }).then((response) => {
      const status = response.status

      if (status === 204) {
        return null
      } else if (status !== 200) {
        throw new Error(response.statusText)
      }

      return response.text()
    }).then((responseText) => {
      resolve(responseText)
    }).catch((error) => {
      reject(error)
    })
  })
})
