/* eslint-disable */
const path = require('path')

module.exports = {
  mode: 'production',

  entry: {
    '_helpers.js': './styleguide/_helpers.js',
  },

  output: {
    path: path.resolve(__dirname, './build/styleguide'),
    chunkFilename: '[name].[contenthash].js',
    filename: '[name]',
    publicPath: 'auto',
    environment: {
      // ...
      arrowFunction: false
    },
  },

  module: {
    rules: [
      {
        test: /\_helpers.js$/,
        use: {
          loader: 'babel-loader',
          options: {
            'presets': [
              [
                '@babel/preset-env',
                {
                  'targets': {
                    "ie": '9'
                  },
                  'useBuiltIns': 'usage',
                  'corejs': '3',
                }
              ]
            ]
          },
        },
      }
    ],
  },
}
