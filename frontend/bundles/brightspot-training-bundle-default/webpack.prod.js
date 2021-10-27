const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const path = require('path')
const merge = require('webpack-merge')

module.exports = merge(require('./webpack.common.js'), {
  mode: 'production',

  plugins: [
    new MiniCssExtractPlugin({
      filename: pathData => {
        if (
          pathData.chunk.name === 'styles/amp/Amp.js' ||
          pathData.chunk.name === 'newsletter/NewsletterInline.js' ||
          pathData.chunk.name === 'newsletter/NewsletterEmbed.js'
        ) {
          return pathData.chunk.name.replace('.js', '') + '.css.hbs'
        } else {
          return pathData.chunk.name.replace('.js', '') + '.css'
        }
      }
    })
  ],

  module: {
    rules: [
      {
        test: path.resolve(__dirname, './styleguide/styles/style-1/All.less'),
        use: [
          MiniCssExtractPlugin.loader,
          'css-loader',
          'postcss-loader',
          'less-loader'
        ]
      },
      {
        test: path.resolve(__dirname, './styleguide/styles/style-2/All.less'),
        use: [
          MiniCssExtractPlugin.loader,
          'css-loader',
          'postcss-loader',
          'less-loader'
        ]
      },
      {
        test: path.resolve(
          __dirname,
          './styleguide/newsletter/NewsletterInline.less'
        ),
        use: [
          MiniCssExtractPlugin.loader,
          'css-loader',
          'postcss-loader',
          'less-loader'
        ]
      },
      {
        test: path.resolve(
          __dirname,
          './styleguide/newsletter/NewsletterEmbed.less'
        ),
        use: [
          MiniCssExtractPlugin.loader,
          'css-loader',
          'postcss-loader',
          'less-loader'
        ]
      },
      {
        test: path.resolve(__dirname, './styleguide/styles/amp/Amp.less'),
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
