const path = require('path')

module.exports = {
  entry: {
    'styleguide/All.min.js': './styleguide/All.js',
    'styleguide/util/IEPolyfills.js': './styleguide/util/IEPolyfills.js'
  },

  output: {
    filename: '[name]',
    publicPath: '/'
  },

  module: {
    rules: [
      // Split out large binary files into separate chunks.
      {
        test: /\.(png|jpg|gif|svg|eot|ttf|woff|woff2)$/,
        loader: 'url-loader',
        options: {
          limit: 10000
        }
      },

      // Transpile JS.
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: ['babel-loader', 'eslint-loader']
      }
    ]
  },

  resolve: {
    alias: {
      // root styleguide javascripts
      actionbar: path.join(
        __dirname,
        './styleguide/core/action-bar/ActionBar.js'
      ),
      banner: path.join(__dirname, './styleguide/core/banner/Banner.js'),
      carousel: path.join(__dirname, './styleguide/core/carousel/Carousel.js'),
      countdownModule: path.join(
        __dirname,
        './styleguide/countdown/CountdownModule.js'
      ),
      talkCustom: path.join(
        __dirname,
        './styleguide/community/commenting/TalkCustom.js'
      ),
      galleryPage: path.join(
        __dirname,
        './styleguide/core/gallery/GalleryPage.js'
      ),
      galleryOverlay: path.join(
        __dirname,
        './styleguide/core/gallery/GalleryOverlay.js'
      ),
      glightbox: path.join(
        __dirname,
        './styleguide/util/glightbox/glightbox.js'
      ),
      googleDfp: path.join(__dirname, './styleguide/dfp/GoogleDfp.js'),
      lazyLoadImages: path.join(
        __dirname,
        './styleguide/core/image/LazyLoadImages.js'
      ),
      listLoadMore: path.join(
        __dirname,
        './styleguide/core/list/ListLoadMore.js'
      ),
      newsletterForm: path.join(
        __dirname,
        './styleguide/core/form/NewsletterForm.js'
      ),
      pageHeader: path.join(__dirname, './styleguide/core/page/Page-header.js'),
      liveBanner: path.join(__dirname, './styleguide/core/promo/PromoLive.js'),
      psToggler: path.join(__dirname, './styleguide/util/ps-toggler.js'),
      searchFilters: path.join(
        __dirname,
        './styleguide/core/search/SearchFilters.js'
      ),
      searchResultsModule: path.join(
        __dirname,
        './styleguide/core/search/SearchResultsModule.js'
      ),
      sectionNavigation: path.join(
        __dirname,
        './styleguide/core/navigation/SectionNavigation.js'
      ),
      storyStackPage: path.join(
        __dirname,
        './styleguide/storystack/StoryStackPage.js'
      ),
      videoPlaylist: path.join(
        __dirname,
        './styleguide/core/video/VideoPlaylist.js'
      ),

      videoAnalytics: path.join(
        __dirname,
        './styleguide/core/video/VideoAnalytics.js'
      ),
      html5videoPlayer: path.join(
        __dirname,
        './styleguide/core/video/HTML5VideoPlayer.js'
      ),
      brightcoveVideoPlayer: path.join(
        __dirname,
        './styleguide/brightcove/BrightcoveVideoPlayer.js'
      ),
      vimeoVideoPlayer: path.join(
        __dirname,
        './styleguide/vimeo/VimeoVideoPlayer.js'
      ),
      youTubeVideoPlayer: path.join(
        __dirname,
        './styleguide/youtube/YouTubeVideoPlayer.js'
      )
    }
  }
}
