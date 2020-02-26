/* eslint-disable no-unused-vars */

import Flickity from 'flickity'
import FlickityHash from 'flickity-hash'

const arrowShape =
  'M22.4566257,37.2056786 L-21.4456527,71.9511488 C-22.9248661,72.9681457 -24.9073712,72.5311671 -25.8758148,70.9765924 L-26.9788683,69.2027424 C-27.9450684,67.6481676 -27.5292733,65.5646602 -26.0500598,64.5484493 L20.154796,28.2208967 C21.5532435,27.2597011 23.3600078,27.2597011 24.759951,28.2208967 L71.0500598,64.4659264 C72.5292733,65.4829232 72.9450684,67.5672166 71.9788683,69.1217913 L70.8750669,70.8956413 C69.9073712,72.4502161 67.9241183,72.8848368 66.4449048,71.8694118 L22.4566257,37.2056786 Z'

export class GalleryPage extends window.HTMLElement {
  connectedCallback () {
    window.addEventListener('load', () => {
      this.initCarousel()
      this.handleStartButton()
      this.handleAsideTrigger()
      this.flickity.on('change', index => {
        this.handleSlideChange(index)
      })
    })
  }

  initCarousel () {
    let carouselSlides = this.querySelector('.GalleryPage-slides')
    let startSlide = this.getAttribute('data-start-slide') || 0
    let startSlideFromHash = parseInt(window.location.hash.slice(7))

    if (startSlideFromHash > 0) {
      startSlide = startSlideFromHash
    }

    let carouselOptions = {
      arrowShape: arrowShape,
      adaptiveHeight: false,
      hash: true,
      imagesLoaded: true,
      initialIndex: startSlide,
      pageDots: false,
      lazyLoad: 2
    }

    if (startSlide > 0) {
      this.setState('gallery')
    }

    this.flickity = new Flickity(carouselSlides, carouselOptions)

    if (this.getMediaQuery() === 'mq-lg' || this.getMediaQuery() === 'mq-xl') {
      if (
        this.getAttribute('data-with-aside') === 'true' &&
        this.getAttribute('data-state') === 'gallery'
      ) {
        this.setAttribute('data-showing-aside', true)
      }
    }

    this.handleSlideChange(startSlide)

    // make sure images are all loaded
    window.addEventListener('load', () => {
      this.flickity.resize()
    })
  }

  handleSlideChange (index) {
    if (index > 0) {
      let actionLinks = this.querySelectorAll('.ActionLink')

      let slideTitle = this.flickity.selectedElement.querySelector(
        '.GallerySlide-title'
      )
      let slideDescription = this.flickity.selectedElement.querySelector(
        '.GallerySlide-infoDescription'
      )
      let slideMedia = this.flickity.selectedElement.querySelector(
        '.GallerySlide-media'
      )
      let galleryAsideContent = this.querySelector(
        '.GalleryPage-aside-slideContent'
      )
      let galleryCurrentSlide = this.querySelector('.GalleryPage-currentSlide')

      galleryAsideContent.innerHTML = ''

      if (slideTitle && galleryAsideContent) {
        galleryAsideContent.innerHTML += slideTitle.outerHTML
      }

      if (slideDescription && galleryAsideContent) {
        galleryAsideContent.innerHTML += slideDescription.outerHTML
      }

      if (galleryCurrentSlide) {
        galleryCurrentSlide.innerHTML = index
      }

      if (actionLinks) {
        actionLinks.forEach(item => {
          let link = item.getAttribute('href')

          if (link) {
            link = link.split('?')[1]
          }

          if (link) {
            if (link.indexOf('#slide') > -1) {
              item.setAttribute(
                'href',
                item
                  .getAttribute('href')
                  .replace(/#slide-\d+/, window.location.hash)
              )
            } else {
              item.setAttribute(
                'href',
                item
                  .getAttribute('href')
                  .replace(
                    window.location.pathname,
                    window.location.pathname + window.location.hash
                  )
              )
            }
          }
        })
      }

      document.body.dispatchEvent(
        new window.CustomEvent('Gallery:onSlideUpdate', {
          detail: {
            activeSlide: {
              index: index,
              title: slideTitle
                ? slideTitle.getAttribute('data-info-title')
                : '',
              attribution: slideTitle
                ? slideTitle.getAttribute('data-info-attribution')
                : '',
              bspId: slideMedia.getAttribute('data-image-bsp-id')
            }
          }
        })
      )

      this.preloadNextImage(index)
      this.setState('gallery')
    } else {
      this.setState('start')
      this.setAttribute('data-showing-aside', true)
    }
  }

  preloadNextImage (index) {
    let nextSlide = index + 1
    let slideElement = null
    let imageToPreload = null

    if (nextSlide <= this.flickity.cells.length) {
      slideElement = this.flickity.cells[nextSlide].element
    } else {
      return
    }

    if (slideElement) {
      imageToPreload = slideElement.querySelector('[data-lazy-load="true"]')

      if (imageToPreload) {
        if (imageToPreload.dataset.src) {
          imageToPreload.src = imageToPreload.dataset.src
          imageToPreload.removeAttribute('data-src')
          imageToPreload.removeAttribute('data-lazy-load')
        }
        if (imageToPreload.dataset.srcset) {
          imageToPreload.srcset = imageToPreload.dataset.srcset
          imageToPreload.removeAttribute('data-srcset')
          imageToPreload.removeAttribute('data-lazy-load')
        }
      }
    }
  }

  getMediaQuery () {
    let mqValue =
      window
        .getComputedStyle(document.querySelector('body'), '::before')
        .getPropertyValue('content') || false

    if (mqValue) {
      return mqValue.replace(/["']/g, '')
    } else {
      return false
    }
  }

  setState (state) {
    this.setAttribute('data-state', state)

    if (this.getAttribute('data-state') === 'start') {
      this.removeAttribute('data-showing-aside')
    }
  }

  handleAsideTrigger () {
    let infoButton = this.querySelector('.GalleryPage-infoButton')

    infoButton.addEventListener('click', e => {
      e.preventDefault()

      this.toggleAside()
    })
  }

  toggleAside () {
    if (this.getAttribute('data-showing-aside') === 'true') {
      this.setAttribute('data-showing-aside', false)
    } else {
      this.setAttribute('data-showing-aside', true)
    }
  }

  handleStartButton () {
    let startButton = this.querySelector('.GalleryPage-start')

    if (startButton) {
      startButton.addEventListener('click', e => {
        e.preventDefault()

        this.flickity.next()
      })
    }
  }
}
