import Modal from '../util/Modal.ts'
import Flickity from 'flickity'

export default class HotspotModule extends HTMLElement {
  private modalDiv: HTMLDialogElement = this.querySelector('[data-bsp-modal]')!
  private modal: Modal = new Modal(this.modalDiv)
  private carouselSlides = this.modalDiv.querySelector(
    '.HotspotModule-modal-carousel'
  ) as HTMLElement | null

  connectedCallback(): void {
    const hotspotCount = this.querySelector('.HotspotModule-hotspotCount')
    const hotspots = this.querySelectorAll('.HotspotModule-hotspot')

    if (!this.carouselSlides) {
      return
    }

    if (hotspotCount) {
      hotspotCount.addEventListener('click', () => {
        this.openModal(0)
      })
    }

    hotspots.forEach((hotspot, index) => {
      hotspot.addEventListener('click', () => {
        this.openModal(index)
      })
    })
  }

  openModal(hotspotIndex: number): void {
    if (!this.carouselSlides) {
      return
    }

    this.modal.open()

    const flickity: Flickity = new Flickity(this.carouselSlides, {
      adaptiveHeight: false,
      imagesLoaded: true,
      pageDots: false,
      lazyLoad: 1,
      initialIndex: hotspotIndex,
    })

    setTimeout(() => {
      flickity?.resize()
      this.setAttribute('data-carousel-ready', '')
    }, 200)

    this.modal.on('close', () => {
      flickity.destroy()
      this.removeAttribute('data-carousel-ready')
    })
  }
}
