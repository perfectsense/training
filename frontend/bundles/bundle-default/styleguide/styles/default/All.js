/* eslint-disable no-new */
import './All.less'

import { Accordion } from '../../util/Accordion'
import { AudioPlayer } from '../../audio/AudioPlayer'
import { Banner } from '../../banner/Banner'
import { BrightcoveVideoPlayer } from '../../brightcove/BrightcoveVideoPlayer'
import { Carousel } from '../../carousel/Carousel'
import { CopyLink } from '../../link/CopyLink'
import { FaqQuestion } from '../../faq/FaqQuestion'
import { BSPForm } from '../../form/Form'
import { HTML5VideoPlayer } from '../../video/HTML5VideoPlayer'
import { JwVideoPlayer } from '../../jwplayer/JwVideoPlayer'
import { LanguageMenu } from '../../languages/LanguageMenu'
import { LiveBlog } from '../../liveblog/LiveBlog'
import { LiveBlogFeed } from '../../liveblog/includes/LiveBlogFeed'
import { LiveBlogPost } from '../../liveblog/LiveBlogPost'
import { LoadMore } from '../../global/LoadMore'
import { GalleryPageCarousel } from '../../gallery/GalleryPageCarousel'
import { GalleryPage } from '../../gallery/GalleryPage'
import { GoogleDfp } from '../../dfp/GoogleDfp'
import { HiddenCookieInput } from '../../form/input/HiddenCookieInput.js'
import { HiddenDocumentReferrerInput } from '../../form/input/HiddenDocumentReferrerInput.js'
import { ModuleAnimations } from '../../util/ModuleAnimations'
import { PageHeadingVideo } from '../../page/PageHeadingVideo'
import { PageListAutoRotate } from '../../page/list/PageListAutoRotate'
import { PageListCarousel } from '../../page/list/PageListCarousel'
import { PageListLoadMore } from '../../page/list/PageListLoadMore'
import { PageHeader } from '../../page/Page-header'
import { SearchFilters } from '../../search/SearchFilters'
import { SearchResultsModule } from '../../search/SearchResultsModule'
import { SearchOverlay } from '../../page/Page-searchOverlay'
import { SectionNavigation } from '../../navigation/SectionNavigation'
import { StatList } from '../../stat/StatList'
import { Tabs } from '../../tab/Tabs'
import { Toggler } from '../../util/Toggler'
import { Unfocus } from '../../util/Unfocus'
import { VideoPlaylistModule } from '../../video/VideoPlaylistModule'
import { VimeoVideoPlayer } from '../../vimeo/VimeoVideoPlayer'
import { YouTubeVideoPlayer } from '../../youtube/YouTubeVideoPlayer'

function registerCustomElements() {
  window.customElements.define('bsp-accordion', Accordion)
  window.customElements.define('bsp-audio-player', AudioPlayer)
  window.customElements.define('bsp-banner', Banner)
  window.customElements.define('bsp-brightcove-player', BrightcoveVideoPlayer)
  window.customElements.define('bsp-carousel', Carousel)
  window.customElements.define('bsp-copy-link', CopyLink)
  window.customElements.define('bsp-faq-question', FaqQuestion)
  window.customElements.define('bsp-form', BSPForm)
  window.customElements.define('bsp-gallery-page-carousel', GalleryPageCarousel)
  window.customElements.define('bsp-gallery-page', GalleryPage)
  window.customElements.define('bsp-header', PageHeader)
  window.customElements.define('bsp-cookie-input', HiddenCookieInput)
  window.customElements.define(
    'bsp-document-referrer-input',
    HiddenDocumentReferrerInput
  )
  window.customElements.define('bsp-html5player', HTML5VideoPlayer)
  window.customElements.define('bsp-jw-player', JwVideoPlayer)
  window.customElements.define('bsp-language', LanguageMenu)
  window.customElements.define('bsp-load-more', LoadMore)
  window.customElements.define('bsp-list-auto-rotate', PageListAutoRotate)
  window.customElements.define('bsp-listcarousel', PageListCarousel)
  window.customElements.define('bsp-list-loadmore', PageListLoadMore)
  window.customElements.define('bsp-liveblog', LiveBlog)
  window.customElements.define('bsp-liveblog-feed', LiveBlogFeed)
  window.customElements.define('bsp-liveblog-post', LiveBlogPost)
  window.customElements.define('bsp-page-heading-video', PageHeadingVideo)
  window.customElements.define('bsp-section-nav', SectionNavigation)
  window.customElements.define('bsp-toggler', Toggler)
  window.customElements.define('bsp-search-filters', SearchFilters)
  window.customElements.define('bsp-search-results-module', SearchResultsModule)
  window.customElements.define('bsp-search-overlay', SearchOverlay)
  window.customElements.define('bsp-stat-list', StatList)
  window.customElements.define('bsp-tabs', Tabs)
  window.customElements.define('bsp-video-playlist-module', VideoPlaylistModule)
  window.customElements.define('bsp-vimeo-player', VimeoVideoPlayer)
  window.customElements.define('bsp-youtube-player', YouTubeVideoPlayer)

  new GoogleDfp()
  new Unfocus()
  new ModuleAnimations()
}

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', registerCustomElements)
} else {
  registerCustomElements()
}

export default {}
