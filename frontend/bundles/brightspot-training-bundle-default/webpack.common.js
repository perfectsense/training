const CopyPlugin = require('copy-webpack-plugin')
const ESLintPlugin = require('eslint-webpack-plugin')
const path = require('path')

module.exports = {
  entry: {
    'styles/style-2/All.min.js': './styleguide/styles/style-2/All.js',
    'styles/style-1/All.min.js': './styleguide/styles/style-1/All.js',
    'util/IEPolyfills.js': './styleguide/util/IEPolyfills.js',
    'newsletter/NewsletterInline.js':
      './styleguide/newsletter/NewsletterInline.js',
    'newsletter/NewsletterEmbed.js':
      './styleguide/newsletter/NewsletterEmbed.js',
    'styles/amp/Amp.js': './styleguide/styles/amp/Amp.js'
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
          from: '**/*',
          context: path.resolve(
            __dirname,
            './styleguide/styles/style-1/assets'
          ),
          to: './styleguide/styles/style-1/assets/',
          info: { minimized: true }
        },
        {
          from: '**/*',
          context: path.resolve(
            __dirname,
            './styleguide/styles/style-2/assets'
          ),
          to: './styleguide/styles/style-2/assets/',
          info: { minimized: true }
        },
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
