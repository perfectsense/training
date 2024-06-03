let apiLoaded = false // Variable to track if the API is already loaded

export const loadYouTubeAPI = () => {
  return new Promise((resolve, reject) => {
    if (apiLoaded) {
      resolve('YouTube iFrame API already loaded')
    } else {
      if (
        typeof window.YT !== 'undefined' &&
        typeof window.YT.Player !== 'undefined'
      ) {
        apiLoaded = true
        resolve('YouTube iFrame API already loaded')
      } else {
        const tag = document.createElement('script')
        tag.src = 'https://www.youtube.com/iframe_api'
        const firstScriptTag = document.getElementsByTagName('script')[0]
        firstScriptTag.parentNode.insertBefore(tag, firstScriptTag)

        if (!window.onYouTubeIframeAPIReady) {
          window.onYouTubeIframeAPIReady = () => {
            apiLoaded = true
            resolve('YouTube iFrame API loaded')
          }
        } else {
          const existingFunction = window.onYouTubeIframeAPIReady
          window.onYouTubeIframeAPIReady = () => {
            if (existingFunction) {
              existingFunction()
            }
            apiLoaded = true
            resolve('YouTube iFrame API loaded')
          }
        }

        tag.onerror = () => {
          reject(new Error('Failed to load YouTube iFrame API'))
        }
      }
    }
  })
}
