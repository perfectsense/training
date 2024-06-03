/* eslint-disable */
const ESLintPlugin = require('eslint-webpack-plugin')
const path = require('path')
const ForkTsCheckerWebpackPlugin = require('fork-ts-checker-webpack-plugin');

module.exports = {
  entry: {
    'styles/default/All.min.js': './styleguide/styles/default/All.js',
    'newsletter/NewsletterInline.min.js':
      './styleguide/newsletter/NewsletterInline.js',
    'newsletter/NewsletterEmbed.min.js':
      './styleguide/newsletter/NewsletterEmbed.js',
    'Syndication.min.js': './styleguide/Syndication.js',
  },

  output: {
    path: path.resolve(__dirname, './build/styleguide'),
    chunkFilename: '[name].[contenthash].js',
    filename: '[name]',
    publicPath: 'auto',
  },

  plugins: [
    new ESLintPlugin({
      extensions: ['js', 'ts'],
    }),
    new ForkTsCheckerWebpackPlugin()
  ],

  module: {
    rules: [
      // Split out large binary files into separate chunks.
      {
        test: /\.(png|jpg|gif|svg|eot|ttf|woff|woff2)$/,
        type: 'asset',
      },
      // Transpile JS.
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: ['babel-loader'],
      },
      // Transpile TS.
      {
        test: /\.ts$/,
        exclude: /node_modules/,
        use: ['babel-loader'],
      },
    ],
  },
}
