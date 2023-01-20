export class PageHeadingVideo extends window.HTMLElement {
  connectedCallback() {
    const videoHeadingObserver = new window.IntersectionObserver(function (
      entries
    ) {
      entries.forEach(function (entry) {
        const videoModule = entry.target
        const videoElement = videoModule.querySelector('video')

        if (entry.isIntersecting) {
          videoElement.play()
        } else {
          videoElement.pause()
        }
      })
    })

    videoHeadingObserver.observe(this)
  }
}
