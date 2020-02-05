import { throttle } from '../util/Throttle.js'

export class GoogleDfp {
  constructor () {
    this.selector = '.GoogleDfpAd'

    this.init()
  }

  init () {
    this.currentMediaQuery = this.getMediaQuery()

    // gather the google ads on the page
    this.googleAds = document.querySelectorAll(this.selector)

    // check if ads present to bid for
    if (!this.googleAds.length) {
      return
    }

    // go through each one and lets process the ad
    this.googleAds.forEach(ad => {
      this.processAd(ad)
    })

    this.sendAdserverRequest()
  }

  sendAdserverRequest () {
    // make ad request to DFP
    window.googletag.cmd.push(() => {
      window.googletag.pubads().refresh(window.dfpAdSlots)

      this.createResizeListener()
    })
  }

  processAd (ad) {
    let isHideMobile = ad.getAttribute('data-hide-mobile') || false
    let isHideDesktop = ad.getAttribute('data-hide-desktop') || false
    let isLargeScreen = true

    if (this.getMediaQuery === 'xs' || this.getMediaQuery === 'sm') {
      isLargeScreen = false
    }

    // Remove mobile/desktop inline ads as appropriate
    if ((isLargeScreen && isHideDesktop) || (!isLargeScreen && isHideMobile)) {
      ad.parentNode.remove()
      return
    }

    let id = ad.id
    let slotName = ad.getAttribute('data-slot-name') || ''
    let adSizes = ad.getAttribute('data-slot-sizes') || ''
    let adSizeMap = ad.getAttribute('data-slot-adSizeMap') || ''
    let adSlot

    if (typeof window.googletag !== 'undefined' && window.googletag !== null) {
      window.googletag.cmd.push(() => {
        adSlot = window.googletag
          .defineSlot(slotName, JSON.parse(adSizes), id)
          .addService(window.googletag.pubads())

        // Following vars used to determine if module is hidden via ad size maps (used in auto-positioning)
        // The ad size map window size that's closest to - but not larger than - the current window size applies
        // We only care about width, so we ignore height
        // Thus, for window width smaller than the current window width, we only need to track
        // the largest window size width where it's hidden and the largest where it's visible
        // At the end, the larger of those determines if it's visible or hidden
        let closestHiddenWindowWidth = -1 // only applies if greater than zero
        let closestVisibleWindowWidth = -1 // only applies if greater than zero
        let currentWindowWidth = window.innerWidth

        // Ad Size Mapping. Maps ad sizes to screen sizes to handle responsive ads
        if (
          adSizeMap !== undefined &&
          adSizeMap !== null &&
          adSizeMap.length > 0
        ) {
          let sizeMapping = window.googletag.sizeMapping()
          let adSizeList = JSON.parse(adSizeMap)

          adSizeList.forEach(function (adSizeItem) {
            let adSizes = adSizeItem.slice(1)
            let sizeMapWindowWidth = adSizeItem[0][0]
            let isVisibleBySizeMap = true

            // Check if ad is hidden by ad size map
            if (
              adSizes.length === 1 &&
              adSizes[0][0] === 0 &&
              adSizes[0][1] === 0
            ) {
              adSizes = [] // pass empty array instead of 0x0
              isVisibleBySizeMap = false
            }

            // Check if this window width is one the two closest widths we need to track
            if (
              typeof sizeMapWindowWidth !== 'undefined' &&
              sizeMapWindowWidth <= currentWindowWidth
            ) {
              // only applies if smaller than screen size
              if (
                isVisibleBySizeMap &&
                sizeMapWindowWidth > closestVisibleWindowWidth
              ) {
                closestVisibleWindowWidth = sizeMapWindowWidth
              } else if (
                !isVisibleBySizeMap &&
                sizeMapWindowWidth > closestHiddenWindowWidth
              ) {
                closestHiddenWindowWidth = sizeMapWindowWidth
              }
            }

            sizeMapping.addSize(adSizeItem[0], adSizes)
          })

          adSlot.defineSizeMapping(sizeMapping.build())
        }

        // If the hidden width is closer (bigger) than the visible width, it's hidden
        if (
          closestHiddenWindowWidth >= 0 &&
          closestHiddenWindowWidth > closestVisibleWindowWidth
        ) {
          ad.setAttribute('data-ad-size-map-hidden', true) // to check in DOM
        }

        // we are done with this particular id
        window.googletag.display(id)

        // push the adSlot into our global array of dfpAd slots
        window.dfpAdSlots.push(adSlot)
      })
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

  createResizeListener () {
    window.addEventListener(
      'resize',
      throttle(250, () => {
        if (this.getMediaQuery() !== this.currentMediaQuery) {
          this.currentMediaQuery = this.getMediaQuery()
          // going to just refresh everything here, including dynamic ad slots
          // there would be too much logic to try to only refresh the visible stuff
          // this is a big edge case anyway as no one resizes their browser
          // like this after opening a webpage
          window.googletag.pubads().refresh()
        }
      })
    )
  }
}
