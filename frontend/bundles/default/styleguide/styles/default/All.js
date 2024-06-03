/* eslint-disable no-new */
import './All.less'

import { ComponentLoader } from '../../util/ComponentLoader'
import documentReady from '../../util/documentReady'
import { GoogleDfp } from '../../dfp/GoogleDfp'
import { ModuleAnimations } from '../../util/ModuleAnimations'
import { Unfocus } from '../../util/Unfocus'

const components = [
  {
    elementName: 'bsp-accordion',
    importModule: () => import('../../util/Accordion.ts'),
  },
  {
    elementName: 'bsp-audio-player',
    importModule: () => import('../../audio/AudioPlayer.ts'),
  },
  {
    elementName: 'bsp-banner',
    importModule: () => import('../../banner/Banner.ts'),
  },
  {
    elementName: 'bsp-book',
    importModule: () => import('../../book/Book'),
  },
  {
    elementName: 'bsp-brightcove-player',
    importModule: () => import('../../brightcove/BrightcoveVideoPlayer'),
  },
  {
    elementName: 'bsp-carousel',
    importModule: () => import('../../carousel/Carousel.ts'),
  },
  {
    elementName: 'bsp-chapter',
    importModule: () => import('../../book/Chapter'),
  },
  {
    elementName: 'bsp-code-block',
    importModule: () => import('../../code/CodeBlock'),
  },
  {
    elementName: 'bsp-form',
    importModule: () => import('../../form/Form'),
  },
  {
    elementName: 'bsp-cookie-input',
    importModule: () => import('../../form/input/HiddenCookieInput'),
  },
  {
    elementName: 'bsp-document-referrer-input',
    importModule: () => import('../../form/input/HiddenDocumentReferrerInput'),
  },
  {
    elementName: 'bsp-gallery-slide',
    importModule: () => import('../../gallery/GallerySlide'),
  },
  {
    elementName: 'bsp-gallery-fullscreen',
    importModule: () => import('../../gallery/GalleryFullscreen'),
  },
  {
    elementName: 'bsp-hotspot-module',
    importModule: () => import('../../hotspot/HotspotModule.ts'),
  },
  {
    elementName: 'bsp-load-more',
    importModule: () => import('../../global/LoadMore'),
  },
  {
    elementName: 'bsp-overflow-container',
    importModule: () => import('../../global/OverflowContainer'),
  },
  {
    elementName: 'bsp-jw-player',
    importModule: () => import('../../jwplayer/JwVideoPlayer'),
  },
  {
    elementName: 'bsp-language',
    importModule: () => import('../../languages/LanguageMenu'),
  },
  {
    elementName: 'bsp-copy-link',
    importModule: () => import('../../link/CopyLink'),
  },
  {
    elementName: 'bsp-liveblog-feed',
    importModule: () => import('../../liveblog/includes/LiveBlogFeed'),
  },
  {
    elementName: 'bsp-liveblog',
    importModule: () => import('../../liveblog/LiveBlog'),
  },
  {
    elementName: 'bsp-liveblog-post',
    importModule: () => import('../../liveblog/LiveBlogPost'),
  },
  {
    elementName: 'bsp-liveblog-new-post-link',
    importModule: () => import('../../liveblog/includes/LiveBlogNewPostLink'),
  },
  {
    elementName: 'bsp-section-nav',
    importModule: () => import('../../navigation/SectionNavigation'),
  },
  {
    elementName: 'bsp-list-auto-rotate',
    importModule: () => import('../../page/list/PageListAutoRotate'),
  },
  {
    elementName: 'bsp-listcarousel',
    importModule: () => import('../../page/list/PageListCarousel'),
  },
  {
    elementName: 'bsp-list-loadmore',
    importModule: () => import('../../page/list/PageListLoadMore'),
  },
  {
    elementName: 'bsp-google-captcha',
    importModule: () => import('../../form/input/GoogleCaptcha.js'),
  },
  {
    elementName: 'bsp-header',
    importModule: () => import('../../page/Page-header'),
  },
  {
    elementName: 'bsp-search-overlay',
    importModule: () => import('../../page/Page-searchOverlay'),
  },
  {
    elementName: 'bsp-background-video',
    importModule: () => import('../../global/BackgroundVideo'),
  },
  {
    elementName: 'bsp-product-promo-gallery',
    importModule: () =>
      import('../../product/promo/includes/ProductPromoGallery'),
  },
  {
    elementName: 'bsp-search-results-module',
    importModule: () => import('../../search/SearchResultsModule'),
  },
  {
    elementName: 'bsp-stat-list',
    importModule: () => import('../../stat/StatList'),
  },
  {
    elementName: 'bsp-tabs',
    importModule: () => import('../../tab/Tabs'),
  },
  {
    elementName: 'bsp-tabs-horizontal-navigation',
    importModule: () => import('../../tab/TabsHorizontalNavigation'),
  },
  {
    elementName: 'bsp-toggler',
    importModule: () => import('../../util/Toggler'),
  },
  {
    elementName: 'bsp-html5player',
    importModule: () => import('../../video/HTML5VideoPlayer'),
  },
  {
    elementName: 'bsp-video-playlist-module',
    importModule: () => import('../../video/VideoPlaylistModule'),
  },
  {
    elementName: 'bsp-vimeo-player',
    importModule: () => import('../../vimeo/VimeoVideoPlayer'),
  },
  {
    elementName: 'bsp-youtube-player',
    importModule: () => import('../../youtube/YouTubeVideoPlayer'),
  },
]

documentReady.then(() => {
  const componentLoader = new ComponentLoader()
  components.forEach((component) =>
    componentLoader.registerComponent(component)
  )

  new GoogleDfp()
  new Unfocus()
  new ModuleAnimations()
})

export default {}
