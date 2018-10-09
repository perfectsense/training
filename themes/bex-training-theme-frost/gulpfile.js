const autoprefixer = require('autoprefixer')
const Builder = require('systemjs-builder')
const gulp = require('gulp')
const plugins = require('gulp-load-plugins')()
const styleguide = new (require('brightspot-styleguide'))(gulp)

gulp.task(styleguide.task.less(), () => {
  return gulp.src('styleguide/All.less', {base: '.'})
    .pipe(plugins.sourcemaps.init())
    .pipe(plugins.less())
    .pipe(plugins.postcss([autoprefixer('last 2 versions')]))
    .pipe(plugins.cleanCss())
    .pipe(plugins.rename({extname: '.min.css'}))
    .pipe(plugins.sourcemaps.write('.'))
    .pipe(gulp.dest(styleguide.path.build()))
})

gulp.task(styleguide.task.js(), done => {
  let builder = new Builder()

  builder.config({
    defaultJSExtensions: true,
    baseURL: '../../',

    map: {
      'jquery': 'node_modules/jquery/dist/jquery.js',
      'slick': 'node_modules/slick-carousel/slick/slick.js',
      'js-cookie': 'node_modules/js-cookie/src/js.cookie.js',
      'pluginRegistry': 'node_modules/brightspot-express/styleguide/core/PluginRegistry.js',
      'shareLink': 'node_modules/brightspot-express/styleguide/core/share-bar/ShareLink.js',
      'shareLinkFacebook': 'node_modules/brightspot-express/styleguide/core/share-bar/ShareLinkFacebook.js',
      'banner': 'node_modules/brightspot-express/styleguide/core/banner/Banner.js',
      'core-utils': 'node_modules/brightspot-express/styleguide/core/Utils.js',
      'vex': 'node_modules/vex-js/js/vex.js',

      'jWVideoPlayer': 'node_modules/brightspot-express/styleguide/jwplayer/JWVideoPlayer.js',
      'videoPlaylistItem': 'node_modules/brightspot-express/styleguide/core/video/VideoPlaylistItem.js',
      'videoPlayer': 'node_modules/brightspot-express/styleguide/core/video/VideoPlayer.js',
      'videoController': 'node_modules/brightspot-express/styleguide/core/video/VideoController.js',
      'videoBarPlaylist': 'node_modules/brightspot-express/styleguide/core/video/VideoBarPlaylist.js',

      'plugin-babel': 'node_modules/systemjs-plugin-babel/plugin-babel.js',
      'systemjs-babel-build': 'node_modules/systemjs-plugin-babel/systemjs-babel-browser.js'
    },
    transpiler: 'plugin-babel'
  })

  let buildOptions = {
    minify: false
  }

  builder.buildStatic('themes/bex-training-theme-frost/styleguide/All.js', buildOptions).then(output => {
    gulp.src([])
      .pipe(plugins.file('styleguide/All.js', output.source))
      .pipe(gulp.dest(styleguide.path.build()))
      .pipe(plugins.sourcemaps.init())
      .pipe(plugins.uglify())
      .pipe(plugins.rename({extname: '.min.js'}))
      .pipe(plugins.sourcemaps.write('.'))
      .pipe(gulp.dest(styleguide.path.build()))
      .on('end', done)
  }).catch((err) => {
      console.error(err)
      process.exit(1)
  })
})

gulp.task(styleguide.task.extra('assets'), () => {
  return gulp.src('styleguide/assets/**', {base: '.'})
    .pipe(gulp.dest(styleguide.path.build()))
})

gulp.task(styleguide.task.extra('vendor-assets'), done => {
  gulp.src('vendor-assets/**', { base: '.' })
          .pipe(gulp.dest(styleguide.path.build()))
          .on('end', done)
})
