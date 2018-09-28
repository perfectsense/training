const autoprefixer = require('autoprefixer')
const Builder = require('systemjs-builder')
const gulp = require('gulp')
const plugins = require('gulp-load-plugins')()
const styleguide = new (require('brightspot-styleguide'))(gulp)
const zip = require('gulp-zip')


gulp.task(styleguide.task.less(), () => {
  return gulp.src('styleguide/All.less', { base: '.' })
    .pipe(plugins.sourcemaps.init())
    .pipe(plugins.less({ paths: [ styleguide.path.build() ] }))
    .pipe(plugins.postcss([ autoprefixer('last 2 versions') ]))
    .pipe(plugins.cleanCss())
    .pipe(plugins.rename({ extname: '.min.css' }))
    .pipe(plugins.sourcemaps.write('.'))
    .pipe(gulp.dest(styleguide.path.build()))
})

gulp.task(styleguide.task.js(), (done) => {
  let builder = new Builder()

  builder.config({
    defaultJSExtensions: true,
    map: {
      'jquery': 'node_modules/jquery/dist/jquery.js',
      'pluginRegistry': 'node_modules/brightspot-express/styleguide/core/PluginRegistry.js',
      'banner': 'node_modules/brightspot-express/styleguide/core/banner/Banner.js',
      'core-utils': 'node_modules/brightspot-express/styleguide/core/Utils.js',
      'vex': 'node_modules/vex-js/js/vex.js',

      'jWVideoPlayer': 'node_modules/brightspot-express/styleguide/jwplayer/JWVideoPlayer.js',
      'kalturaVideoPlayer': 'node_modules/brightspot-express/styleguide/kaltura/KalturaVideoPlayer.js',
      'videoPlaylistItem': 'node_modules/brightspot-express/styleguide/core/video/VideoPlaylistItem.js',
      'videoPlayer': 'node_modules/brightspot-express/styleguide/core/video/VideoPlayer.js',

      'plugin-babel': 'node_modules/systemjs-plugin-babel/plugin-babel.js',
      'systemjs-babel-build': 'node_modules/systemjs-plugin-babel/systemjs-babel-browser.js'
    },
    transpiler: 'plugin-babel'
  })

  let buildOptions = {
    minify: false
  }

  builder.buildStatic('styleguide/All.js', buildOptions).then((output) => {
    gulp.src([ ])
      .pipe(plugins.file('styleguide/All.js', output.source))
      .pipe(gulp.dest(styleguide.path.build()))
      .pipe(plugins.sourcemaps.init())
      .pipe(plugins.uglify())
      .pipe(plugins.rename({ extname: '.min.js' }))
      .pipe(plugins.sourcemaps.write('.'))
      .pipe(gulp.dest(styleguide.path.build()))
      .on('end', done)
  }).catch((err) => {
      console.error(err)
      process.exit(1)
  })
})

gulp.task(styleguide.task.extra('assets'), () => {
  return gulp.src('styleguide/assets/**', { base: '.' })
    .pipe(gulp.dest(styleguide.path.build()))
})

gulp.task('package', ['default'], () => {
  return gulp.src('target/bex-training-1.0.0-SNAPSHOT/**')
    .pipe(zip('bex-training-1.0.0-SNAPSHOT.zip'))
    .pipe(gulp.dest('target'))
})
