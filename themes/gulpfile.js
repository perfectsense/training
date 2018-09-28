const fs = require('fs-extra')
const gulp = require('gulp')
const minimist = require('minimist')
const path = require('path')
const replace = require('gulp-string-replace');
const capitalize = require('capitalize');

gulp.task('theme', () => {
  const options = minimist(process.argv.slice(2))
  const name = options.name

  if (!name) {
    console.log('Usage: gulp theme --name <name>')
    process.exit(1)
  }

  const themeDirectory = 'bex-training-theme-' + name

  function copy(name, sourceDir, targetDir) {
    const target = path.join(targetDir, name)

    if (!fs.existsSync(target)) {
      const source = path.join(sourceDir, name)

      fs.mkdirsSync(targetDir)
      fs.copySync(source, target)
      console.log(`Copied to ${target}`)
    }
  }

  function copyAndReplaceVariables(name, sourceDir, targetDir, fileName) {
    const target = path.join(targetDir, fileName)
    const options = {
      logs: {
        enabled: false
      }
    };

    const source = path.join(sourceDir, fileName)
    gulp.src([source])
      .pipe(replace(/\$theme_name/g, name, options))
      .pipe(replace(/\$name/g, capitalize.words(name), options))
      .pipe(gulp.dest(targetDir))

    console.log(`Copied to ${target}`)
  }

  copyAndReplaceVariables(name, '.template', themeDirectory, '.brightspot-theme.properties')
  copy('_config.json', '.template', path.join(themeDirectory, 'styleguide'))
  copyAndReplaceVariables('bex-training', '.template', path.join(themeDirectory, 'styleguide', 'bex-training'), '_theme.json')
  copyAndReplaceVariables(name, '.template', path.join(themeDirectory, 'styleguide'), '_wrapper.json')
  copy('All.js', '.template', path.join(themeDirectory, 'styleguide'))
  copy('All.less', '.template', path.join(themeDirectory, 'styleguide'))
  copyAndReplaceVariables(name, '.template', themeDirectory, 'gulpfile.js')
  copyAndReplaceVariables(name, '.template', themeDirectory, 'pom.xml')
})
