export default class BackgroundVideo extends HTMLElement {
  connectedCallback() {
    const video = this.querySelector('video')
    new IntersectionObserver(([entry]) =>
      entry.isIntersecting ? video.play() : video.pause()
    ).observe(video)
  }
}
