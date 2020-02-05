const Styleguide = require('brightspot-styleguide')
const webpack = require('webpack')
const merge = require('webpack-merge')

module.exports = merge(
  require('./webpack.common.js'),
  Styleguide.webpackDevServerConfig(webpack, {
    mode: 'development',
    devtool: 'inline-source-map',

    module: {
      rules: [
        {
          test: /\.less$/,
          use: [
            'style-loader',
            'css-loader?sourceMap',
            'postcss-loader?sourceMap',
            'less-loader?sourceMap'
          ]
        }
      ]
    }
  })
)
