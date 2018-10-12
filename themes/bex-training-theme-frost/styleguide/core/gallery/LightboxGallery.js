import plugins from 'pluginRegistry'

/* eslint-disable no-undef */
export class LightboxGallery {
  constructor (ctx, options) {
    this.ctx = ctx
    this.slider = null
    this.slideClass = options.slideClass

    const mediaQueryForLightbox = window.matchMedia && window.matchMedia('only screen and (min-width: 1024px)')
    const fallbackForObjectFit = ('objectFit' in document.documentElement.style === false)
    const slides = this.ctx.querySelectorAll(this.slideClass)

    if (mediaQueryForLightbox.matches) {
      Array.from(slides).forEach((slide, index) => {
        if (fallbackForObjectFit) {
          const imgContainer = slide.querySelector(`.CarouselSlide-mediaContent`)
          const imgUrl = imgContainer.querySelector(`img`).getAttribute(`src`)

          if (imgUrl) {
            imgContainer.style.backgroundImage = `url(${imgUrl})`
            imgContainer.classList.add(`object-fit-fallback`)
          }
        }

        slide.querySelector(`.CarouselSlide-infoTitle`).insertAdjacentHTML(`afterend`, `<div class="pagination-status">${index + 1} of ${slides.length}</div>`)
        slide.addEventListener(`click`, evt => {
          evt.preventDefault()
          // only launch the lightbox if we're going to render a slideshow
          const contentProp = window.getComputedStyle(this.ctx, ':after').getPropertyValue('content')
          if (contentProp === '"flickity"' || contentProp === 'flickity') {
            this.createLightbox(index)
          }
        })
      })
    }
  }

  createLightbox (index) {
    const lightbox = document.createElement(`div`)
    const slidesClone = document.createElement(`div`)

    Array.from(this.ctx.querySelectorAll(this.slideClass)).forEach((slide) => {
      let temp = document.createElement(`div`)
      temp.setAttribute('class', 'LightboxSlides-slide')
      temp.appendChild(slide.querySelector('.CarouselSlide').cloneNode(true))
      slidesClone.appendChild(temp)
    })
    slidesClone.setAttribute('class', 'LightboxSlides')

    lightbox.insertAdjacentHTML(`afterbegin`, `<a href="#" class="close-button"></a>`)
    lightbox.className = `GalleryPage-lightbox`
    lightbox.appendChild(slidesClone)

    document.body.appendChild(lightbox)
    document.body.classList.add(`lightbox-enabled`)

    this.slider = new Flickity(slidesClone, {
      adaptiveHeight: true,
      cellAlign: `center`,
      imagesLoaded: true,
      initialIndex: +index,
      pageDots: false,
      percentPosition: true
    })

    lightbox.querySelector(`.close-button`).addEventListener(`click`, evt => {
      evt.preventDefault()
      this.removeLightbox()
    })
  }

  removeLightbox () {
    setTimeout(() => {
      this.slider.destroy()
      let lightBox = document.querySelector(`.GalleryPage-lightbox`)
      lightBox.parentNode.removeChild(lightBox)
      document.body.classList.remove(`lightbox-enabled`)
    }, 0)
  }
}

plugins.register(LightboxGallery, '.GalleryPage-main .GalleryPage-slides .ListMasonry-items', 'interactive', { slideClass: '.grid-item' })
