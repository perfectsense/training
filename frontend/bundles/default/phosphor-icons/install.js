/* eslint-disable @typescript-eslint/no-var-requires */
/* global __dirname: false process:false require:false */
const fs = require('fs-extra')
const path = require('path')
const yaml = require('js-yaml')

const sourceDir = path.resolve('node_modules/@phosphor-icons/core/assets')
const targetDir = path.resolve('styleguide/_icons')
const yamlFile = path.resolve(__dirname, 'icons.yml')

async function installIcons() {
  console.log('Phosphor Icons: Installing icons...')
  try {
    // Clear target directory
    await fs.emptyDir(targetDir)

    // Read icons.yml file
    const fileContents = await fs.readFile(yamlFile, 'utf8')
    const data = yaml.load(fileContents)
    const iconsToInstall = data.icons || []
    const weightsToInstall = data.weights || []

    if (!weightsToInstall.includes('regular')) {
      weightsToInstall.unshift('regular')
    }

    // If no icons specified, log and return
    if (iconsToInstall.length === 0) {
      console.log('Phosphor Icons: No icons specified for installation.')
      return
    }

    // Collect all files to install
    const allFilesToInstall = iconsToInstall
      .flatMap((icon) =>
        weightsToInstall.map((weight) => {
          let filename = `${icon}`
          if (weight !== 'regular') {
            filename += `-${weight}`
          }
          return path.join(weight, `${filename}.svg`)
        })
      )
      .map((iconPath) => path.join(sourceDir, iconPath))

    // Install all files
    for (const srcFile of allFilesToInstall) {
      const destFile = srcFile
        .replace(sourceDir, targetDir)
        .replace('.svg', '.hbs')
      const destDir = path.dirname(destFile)

      await fs.ensureDir(destDir)
      await fs.copyFile(srcFile, destFile)
    }

    console.log(
      `Phosphor Icons: Installed ${iconsToInstall.length} ${
        iconsToInstall.length === 1 ? 'icon' : 'icons'
      } in ${weightsToInstall.length} ${
        weightsToInstall.length === 1 ? 'weight' : 'weights'
      } (${weightsToInstall.join(', ')}).`
    )
  } catch (err) {
    console.error(
      'Phosphor Icons: Installation failed due to an error:',
      err.message || err
    )
    process.exit(1)
  }
}

// Call the installIcons function
installIcons()
