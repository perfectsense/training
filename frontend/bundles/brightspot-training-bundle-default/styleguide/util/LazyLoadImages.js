/**
 * Whenever we AJAX something, we should call an event on document.body that this class picks up
 *
 * let customEvent = new window.CustomEvent('Ajax:Rendered', {
 *   bubbles: true
 * })
 *
 * document.body.dispatchEvent(customEvent)
 */

export class LazyLoadImages {
  // with our All.js instantiation, this does not get called until DOM ready, so no check
  constructor () {
    if ('IntersectionObserver' in window) {
      this.modernBrowsers()

      document.body.addEventListener('Ajax:Rendered', () => {
        this.modernBrowsers()
      })
    } else {
      this.oldskool()

      document.body.addEventListener('Ajax:Rendered', () => {
        this.oldskool()
      })
    }
  }

  modernBrowsers () {
    const lazyImages = [].slice.call(
      document.querySelectorAll('[data-lazy-load]')
    )

    const lazyImageObserver = new window.IntersectionObserver(function (
      entries
    ) {
      entries.forEach(function (entry) {
        if (entry.isIntersecting) {
          const lazyImage = entry.target
          if (lazyImage.dataset.src) {
            lazyImage.src = lazyImage.dataset.src
          }
          if (lazyImage.dataset.srcset) {
            lazyImage.srcset = lazyImage.dataset.srcset
          }
          lazyImage.removeAttribute('data-lazy-load')
          lazyImageObserver.unobserve(lazyImage)
        }
      })
    })

    lazyImages.forEach(image => {
      lazyImageObserver.observe(image)
    })
  }

  oldskool () {
    let active = false

    let lazyImages = [].slice.call(
      document.querySelectorAll('[data-lazy-load]')
    )

    const lazyLoad = function () {
      if (active === false) {
        active = true

        setTimeout(function () {
          lazyImages.forEach(function (lazyImage) {
            if (
              lazyImage.getBoundingClientRect().top <= window.innerHeight &&
              lazyImage.getBoundingClientRect().bottom >= 0 &&
              window.getComputedStyle(lazyImage).display !== 'none'
            ) {
              if (lazyImage.dataset.src) {
                lazyImage.src = lazyImage.dataset.src
              }
              if (lazyImage.dataset.srcset) {
                lazyImage.srcset = lazyImage.dataset.srcset
              }

              lazyImage.removeAttribute('data-lazy-load')

              lazyImages = lazyImages.filter(function (image) {
                return image !== lazyImage
              })

              if (lazyImages.length === 0) {
                document.removeEventListener('scroll', lazyLoad)
                window.removeEventListener('resize', lazyLoad)
                window.removeEventListener('orientationchange', lazyLoad)
              }
            }
          })

          active = false
        }, 200)
      }
    }

    document.addEventListener('scroll', lazyLoad)
    window.addEventListener('resize', lazyLoad)
    window.addEventListener('orientationchange', lazyLoad)

    lazyLoad()
  }
}
