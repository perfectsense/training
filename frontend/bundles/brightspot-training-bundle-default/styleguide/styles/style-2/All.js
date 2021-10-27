/* eslint-disable no-new */
import './All.less'

import { Banner } from '../../banner/Banner'
import { Carousel } from '../../carousel/Carousel'
import { BSPForm } from '../../form/Form'
import { GalleryPageCarousel } from '../../gallery/GalleryPageCarousel'
import { GalleryPage } from '../../gallery/GalleryPage'
import { GoogleDfp } from '../../dfp/GoogleDfp'
import { LazyLoadImages } from '../../util/LazyLoadImages'
import { LiveBlog } from '../../liveblog/LiveBlog'
import { LiveBlogFeed } from '../../liveblog/includes/LiveBlogFeed'
import { LiveBlogPost } from '../../liveblog/LiveBlogPost'
import { PageListAutoRotate } from '../../page/list/PageListAutoRotate'
import { PageListCarousel } from '../../page/list/PageListCarousel'
import { PageListLoadMore } from '../../page/list/PageListLoadMore'
import { PageHeader } from '../../page/Page-header'
import { SearchFilters } from '../../search/SearchFilters'
import { SearchResultsModule } from '../../search/SearchResultsModule'
import { SearchOverlay } from '../../page/Page-searchOverlay'
import { SectionNavigation } from '../../navigation/SectionNavigation'
import { Toggler } from '../../util/Toggler'
import { Unfocus } from '../../util/Unfocus'

function registerCustomElements () {
  window.customElements.define('bsp-banner', Banner)
  window.customElements.define('bsp-carousel', Carousel)
  window.customElements.define('bsp-form', BSPForm)
  window.customElements.define('bsp-gallery-page-carousel', GalleryPageCarousel)
  window.customElements.define('bsp-gallery-page', GalleryPage)
  window.customElements.define('bsp-header', PageHeader)
  window.customElements.define('bsp-list-auto-rotate', PageListAutoRotate)
  window.customElements.define('bsp-listcarousel', PageListCarousel)
  window.customElements.define('bsp-list-loadmore', PageListLoadMore)
  window.customElements.define('bsp-liveblog', LiveBlog)
  window.customElements.define('bsp-liveblog-feed', LiveBlogFeed)
  window.customElements.define('bsp-liveblog-post', LiveBlogPost)
  window.customElements.define('bsp-section-nav', SectionNavigation)
  window.customElements.define('bsp-toggler', Toggler)
  window.customElements.define('bsp-search-filters', SearchFilters)
  window.customElements.define('bsp-search-results-module', SearchResultsModule)
  window.customElements.define('bsp-search-overlay', SearchOverlay)

  new GoogleDfp()
  new LazyLoadImages()
  new Unfocus()
}

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', registerCustomElements)
} else {
  registerCustomElements()
}

export default {}
