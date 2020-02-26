const Styleguide = require('brightspot-styleguide')
const autoprefixer = require('autoprefixer')
const gulp = require('gulp')
const plugins = require('gulp-load-plugins')()
const size = require('gulp-size')

const styleguide = new Styleguide(gulp, { webpack: true, parent: '../../' })

gulp.task(styleguide.task.extra('assets'), () => {
  return gulp
    .src(
    [
      'styleguide/PreviewPage.css',
      'styleguide/assets/**',
      'styleguide/examples/**/*.js'
    ],
      { base: '.' }
    )
    .pipe(gulp.dest(styleguide.path.build()))
})

gulp.task(styleguide.task.extra('amp'), () => {
  return gulp
    .src(['styleguide/Amp.less'], { base: 'styleguide' })
    .pipe(plugins.less())
    .pipe(
      plugins.postcss([
        autoprefixer('last 2 versions'),
        require('postcss-amp'),
        require('postcss-remove-media-query-ranges')({
          max: 767,
          removeMin: true
        })
      ])
    )
    .pipe(plugins.cleanCss())
    .pipe(plugins.rename({ extname: '.min.css.amp.hbs' })) // using the .hbs extension so that we can abuse the existing `include` helper to inline the styles
    .pipe(size({ title: `AMP CSS (shouldn't exceed 50k)` }))
    .pipe(gulp.dest(styleguide.path.build()))
})
