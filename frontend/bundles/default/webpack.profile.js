/* eslint-disable */

const { merge } = require('webpack-merge')

const BundleAnalyzerPlugin =
  require('webpack-bundle-analyzer').BundleAnalyzerPlugin

module.exports = merge(require('./webpack.prod.js'), {
  plugins: [new BundleAnalyzerPlugin()],
})
