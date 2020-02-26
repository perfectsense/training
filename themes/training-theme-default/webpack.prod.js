const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const path = require('path')
const merge = require('webpack-merge')

const entryLessFile = path.resolve(__dirname, './styleguide/All.less')

module.exports = merge(require('./webpack.common.js'), {
  mode: 'production',

  plugins: [
    new MiniCssExtractPlugin({
      filename: 'styleguide/All.min.css'
    })
  ],

  module: {
    rules: [
      {
        test: entryLessFile,
        use: [
          MiniCssExtractPlugin.loader,
          'css-loader',
          'postcss-loader',
          'less-loader'
        ]
      }
    ]
  }
})
