/* eslint-disable */

const webpack = require('webpack')
const { merge } = require('webpack-merge')

module.exports = merge(require('./webpack.prod.js'), {
  plugins: [
    new webpack.optimize.LimitChunkCountPlugin({
      maxChunks: 1,
    }),
  ],
})
