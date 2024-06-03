let apiLoaded = false // Variable to track if the API is already loaded

export const loadVimeoAPI = () => {
  return new Promise((resolve, reject) => {
    if (apiLoaded || typeof window.Vimeo?.Player !== 'undefined') {
      resolve('Vimeo API already loaded')
    } else {
      const tag = document.createElement('script')
      tag.src = 'https://player.vimeo.com/api/player.js'
      const firstScriptTag = document.getElementsByTagName('script')[0]
      tag.onload = () => {
        apiLoaded = true
        vimeoPlayerReady()
          .then(() => {
            resolve('Vimeo API loaded')
          })
          .catch((error) => {
            reject(error)
          })
      }
      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag)

      tag.onerror = () => {
        reject(new Error('Failed to load Vimeo API'))
      }
    }
  })
}

const vimeoPlayerReady = () => {
  return new Promise((resolve) => {
    function checkVimeoPlayer() {
      if (window.Vimeo && typeof window.Vimeo.Player === 'function') {
        resolve()
      } else {
        requestAnimationFrame(checkVimeoPlayer)
      }
    }

    checkVimeoPlayer()
  })
}
