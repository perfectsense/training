export class GoogleDfp {
  constructor (item) {
    var id = item.id

    /* global googletag */
    /* global googleTagTargets */

    if (typeof googletag !== 'undefined' && googletag !== null) {
      googletag.cmd.push(function () {
        var slotName = item.getAttribute('data-slot-name')
        var adSizes = item.getAttribute('data-slot-sizes')

        var adSlot = googletag.defineSlot(slotName, JSON.parse(adSizes), id)
          .addService(googletag.pubads())

        if (typeof googleTagTargets !== 'undefined' && googleTagTargets !== null && googleTagTargets.length > 0) {
          for (var i = 0; i < googleTagTargets.length; i++) {
            for (var key in googleTagTargets[i]) {
              if (googleTagTargets[i].hasOwnProperty(key)) {
                googletag.pubads().setTargeting(key, googleTagTargets[i][key])
              }
            }
          }
        }

        var adSizeMap = item.getAttribute('data-slot-adSizeMap')
        if (adSizeMap !== undefined && adSizeMap !== null && adSizeMap.length > 0) {
          var sizeMapping = googletag.sizeMapping()

          var adSizeList = JSON.parse(adSizeMap)
          adSizeList.forEach(function (adSizeItem) {
            var adSizes = adSizeItem.slice(1)

            if (adSizes.length === 1 && adSizes[0][0] === 0 && adSizes[0][1] === 0) {
              adSizes = []
            }

            sizeMapping.addSize(adSizeItem[0], adSizes)
          })

          adSlot.defineSizeMapping(sizeMapping.build())
        }

        googletag.display(id)
        googletag.pubads().refresh([adSlot])
      })
    }
  }
}
