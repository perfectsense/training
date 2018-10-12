export function bindAll (scope, methods) {
  methods.forEach((method) => {
    scope[method] = scope[method].bind(scope)
  })
}
