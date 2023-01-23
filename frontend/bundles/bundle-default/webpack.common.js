const CopyPlugin = require('copy-webpack-plugin')
const ESLintPlugin = require('eslint-webpack-plugin')
const path = require('path')

module.exports = {
  entry: {
    'styles/default/All.min.js': './styleguide/styles/default/All.js',
    'util/IEPolyfills.js': './styleguide/util/IEPolyfills.js',
    'newsletter/NewsletterInline.min.js':
      './styleguide/newsletter/NewsletterInline.js',
    'newsletter/NewsletterEmbed.min.js':
      './styleguide/newsletter/NewsletterEmbed.js',
    'styles/amp/Amp.min.js': './styleguide/styles/amp/Amp.js'
  },

  output: {
    path: path.resolve(__dirname, './build/styleguide'),
    chunkFilename: '[id].[contenthash].js',
    filename: '[name]',
    publicPath: '/'
  },

  plugins: [
    new CopyPlugin({
      patterns: [
        {
          from: 'node_modules/@webcomponents/webcomponentsjs/bundles/*.js',
          to: 'webcomponents-loader/bundles/[name][ext]'
        },
        {
          from:
            'node_modules/@webcomponents/webcomponentsjs/webcomponents-loader.js',
          to: 'webcomponents-loader/'
        }
      ]
    }),
    new ESLintPlugin()
  ],

  module: {
    rules: [
      // Split out large binary files into separate chunks.
      {
        test: /\.(png|jpg|gif|svg|eot|ttf|woff|woff2)$/,
        type: 'asset'
      },

      // Transpile JS.
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: ['babel-loader']
      }
    ]
  }
}
