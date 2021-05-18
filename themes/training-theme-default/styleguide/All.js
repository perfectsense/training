/* eslint-disable no-new */
import './All.less'

import { ActionBar } from 'actionbar'
import { Banner } from 'banner'
import { Carousel } from 'carousel'
import { CountdownModule } from 'countdownModule'
import { GalleryPage } from 'galleryPage'
import { GoogleDfp } from 'googleDfp'
import { BrightcoveVideoPlayer } from 'brightcoveVideoPlayer'
import { HTML5VideoPlayer } from 'html5videoPlayer'
import { LazyLoadImages } from 'lazyLoadImages'
import { ListLoadMore } from 'listLoadMore'
import { NewsletterForm } from 'newsletterForm'
import { PageHeader } from 'pageHeader'
import { PsToggler } from 'psToggler'
import { SearchFilters } from 'searchFilters'
import { SearchResultsModule } from 'searchResultsModule'
import { SectionNavigation } from 'sectionNavigation'
import { YouTubeVideoPlayer } from 'youTubeVideoPlayer'
import { VimeoVideoPlayer } from 'vimeoVideoPlayer'
import { VideoPlaylist } from 'videoPlaylist'

function registerCustomElements () {
  window.customElements.define('ps-actionbar', ActionBar)
  window.customElements.define('ps-banner', Banner)
  window.customElements.define('ps-carousel', Carousel)
  window.customElements.define('ps-countdown-module', CountdownModule)
  window.customElements.define('ps-gallery-page', GalleryPage)
  window.customElements.define('ps-brightcoveplayer', BrightcoveVideoPlayer)
  window.customElements.define('ps-html5player', HTML5VideoPlayer)
  window.customElements.define('ps-list-loadmore', ListLoadMore)
  window.customElements.define('ps-form-newsletter', NewsletterForm)
  window.customElements.define('ps-header', PageHeader)
  window.customElements.define('ps-section-nav', SectionNavigation)
  window.customElements.define('ps-search-filters', SearchFilters)
  window.customElements.define('ps-search-results-module', SearchResultsModule)
  window.customElements.define('ps-toggler', PsToggler)
  window.customElements.define('ps-youtubeplayer', YouTubeVideoPlayer)
  window.customElements.define('ps-vimeoplayer', VimeoVideoPlayer)
  window.customElements.define('ps-video-playlist', VideoPlaylist)
  new GoogleDfp()
  new LazyLoadImages()
}

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', registerCustomElements)
} else {
  registerCustomElements()
}

export default {}
