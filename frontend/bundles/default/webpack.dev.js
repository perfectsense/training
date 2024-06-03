/* eslint-disable */

const styleguide = require('@brightspot/styleguide')
const webpack = require('webpack')
const { merge } = require('webpack-merge')

module.exports = merge(
  require('./webpack.common.js'),
  styleguide.webpack('./styleguide', webpack, {
    mode: 'development',
    devtool: 'inline-source-map',
    target: 'web',

    module: {
      rules: [
        {
          test: /\.less$/,
          use: [
            'style-loader',
            {
              loader: 'css-loader',
              options: {
                sourceMap: true,
              },
            },
            {
              loader: 'postcss-loader',
              options: {
                sourceMap: true,
              },
            },
            {
              loader: 'less-loader',
              options: {
                sourceMap: true,
              },
            },
          ],
        },
      ],
    },
  })
)
