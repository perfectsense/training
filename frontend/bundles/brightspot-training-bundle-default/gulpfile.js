gulp.task(styleguide.task.extra('newsletterInline'), () => {
  return gulp
    .src(['styleguide/newsletter/NewsletterInline.scss'], {
      base: 'styleguide'
    })
    .pipe(plugins.sass())
    .pipe(plugins.postcss([autoprefixer('last 2 versions')]))
    .pipe(plugins.cleanCss())
    .pipe(plugins.rename({ extname: '.min.css.hbs' })) // using the .hbs extension so that we can abuse the existing `include` helper to inline the styles
    .pipe(gulp.dest(styleguide.path.build()))
})

gulp.task(styleguide.task.extra('newsletterEmbed'), () => {
  return gulp
    .src(['styleguide/newsletter/NewsletterEmbed.scss'], { base: 'styleguide' })
    .pipe(plugins.sass())
    .pipe(plugins.postcss([autoprefixer('last 2 versions')]))
    .pipe(plugins.cleanCss())
    .pipe(plugins.rename({ extname: '.min.css.hbs' })) // using the .hbs extension so that we can abuse the existing `include` helper to inline the styles
    .pipe(gulp.dest(styleguide.path.build()))
})

gulp.task(styleguide.task.extra('amp'), () => {
  return gulp
    .src(['styleguide/styles/amp/Amp.less'], { base: 'styleguide' })
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
    .pipe(size({ title: "AMP CSS (shouldn't exceed 50k)" }))
    .pipe(gulp.dest(styleguide.path.build()))
})
