module.exports = function (api) {
  api.cache(true)

  const presets = [
    [
      '@babel/preset-env',
      {
        useBuiltIns: false
      }
    ]
  ]
  const plugins = [
    '@babel/plugin-proposal-class-properties',
    '@babel/plugin-syntax-dynamic-import',
    '@babel/plugin-transform-runtime'
  ]

  return {
    presets,
    plugins
  }
}
