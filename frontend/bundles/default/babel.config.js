/* eslint-disable */
module.exports = function (api) {
  api.cache(true)

  const presets = [
    [
      '@babel/preset-env',
      {
        useBuiltIns: false,
      },
    ],
    '@babel/preset-typescript'
  ]
  const plugins = [
    '@babel/plugin-proposal-class-properties',
    '@babel/plugin-syntax-dynamic-import',
    '@babel/plugin-transform-runtime',
    [
      'prismjs',
      {
        languages: [
          'javascript',
          'css',
          'java',
          'markdown',
          'markup',
          'handlebars',
          'json',
          'sh-session',
          'html',
          'less',
          'sql',
          'properties',
          'xml-doc',
          'graphql',
          'cpp',
          'python'
        ],
        plugins: ['copy-to-clipboard', 'line-numbers', 'line-highlight']
      }
    ]
  ]

  return {
    presets,
    plugins,
  }
}
