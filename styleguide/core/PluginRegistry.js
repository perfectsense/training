import Logger from './util/Logger.js'

export const PLUGIN_REGISTRY_ATTR_PLUGIN = 'data-bsp-plugin'
export const PLUGIN_REGISTRY_EVENT_COMPLETE = 'complete'
export const PLUGIN_REGISTRY_EVENT_INTERACTIVE = 'interactive'
export const PLUGIN_REGISTRY_EVENT_READYSTATECHANGE = 'readystatechange'
export const PLUGIN_REGISTRY_EVENT_PLUGIN_LOADED = 'bsp-plugin:load'
export const PLUGIN_REGISTRY_PROP_PLUGIN = 'bspPlugin'

/**
 * Pairs module classes with DOM selectors both at load and
 * to dynamically-created elements.
 *
 * @example
 * // simplest use case, inits a module on
 * // document.readystatechange === 'interactive'
 * // (default)
 * class MyModule {
 *   constructor (el) {
 *     // do stuff
 *   }
 * }
 * plugins.register(MyModule, '.MyModule')
 *
 * @example
 * // identical to the last example, only change
 * // is it inits a module on
 * // document.readystatechange === 'complete'
 * class MyModule {
 *   constructor (el) {
 *     // do stuff
 *   }
 * }
 * plugins.register(MyModule, '.MyModule', 'complete')
 *
 * @example
 * // for more complex use cases, inits a module
 * // when a custom validator function resolves a promise
 * class MyModule {
 *   constructor (el) {
 *     // do stuff
 *   }
 * }
 * // wait two seconds before init
 * plugins.register(MyModule, '.MyModule', (resolve, reject, el) => {
 *   setTimeout(() => {
 *     resolve()
 *   }, 2000)
 * })
 *
 * @see {@link https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise|MDN Promise documentation}
 */
export class PluginRegistry {
  attrPlugin = PLUGIN_REGISTRY_ATTR_PLUGIN
  propPlugin = PLUGIN_REGISTRY_PROP_PLUGIN
  msgPrefix = 'PluginRegistry:'

  constructor () {
    this._plugins = []
    _createObserver.call(this)
  }

  /**
   * Registers a Javascript module to be applied to specified DOM selector
   * @param {Class} Module - the constructor will receive the DOM element as its only argument
   * @param {String} selector - DOM selector, supports comma-separated
   * @param {String|Function} load - strings
   * are {@link https://developer.mozilla.org/en-US/docs/Web/Events/readystatechange|document.readystatechange} states,
   * functions receive the arguments resolve, reject, and el so you can write
   * custom load conditions
   * @param {Object} options - object literal to pass extra config to the plugin
   */
  register (Module, selector, load = PLUGIN_REGISTRY_EVENT_INTERACTIVE, options = {}) {
    let self = this
    _addPluginToRegistry.call(this, {
      load: load,
      Module: Module,
      selector: selector,
      options: options
    })
    /**
     * The mutation observer behaves a little differently in
     * Chrome vs Firefox, so we have to do an additional call
     * on readystatechange to make sure Firefox loads modules
     * written to the page initially
     */
    document.addEventListener(PLUGIN_REGISTRY_EVENT_READYSTATECHANGE, () => {
      if (document.readyState === PLUGIN_REGISTRY_EVENT_INTERACTIVE || document.readyState === PLUGIN_REGISTRY_EVENT_COMPLETE) {
        _loadPlugins.call(self)
      }
    })
  }

  /**
   * Get instantiated plugins from an element
   * @param {HTMLElement} DOM element with plugin instantiated on it
   * @returns {Object} object with instantiated plugins on an object
   */
  static scope (el) {
    return el[PLUGIN_REGISTRY_PROP_PLUGIN]
  }

  /**
   * Get instantiated plugins from an element
   * @param {HTMLElement} DOM element with plugin instantiated on it
   * @returns {Object} object with instantiated plugins on an object
   */
  scope (el) {
    return this.constructor.scope(el)
  }
}

/**
 * Private methods for the PluginRegistry class
 */
/** @ignore */
function _addPluginToRegistry (plugin) {
  this._plugins.push(plugin)
  Logger.log(`${this.msgPrefix} _addPluginToRegistry: added plugin to registry`, plugin)
}

/** @ignore */
function _createObserver () {
  let self = this
  if (window.MutationObserver) {
    let observer = new window.MutationObserver((mutations) => {
      _loadPlugins.call(self)
    })
    if (document.readyState === PLUGIN_REGISTRY_EVENT_INTERACTIVE || document.readyState === PLUGIN_REGISTRY_EVENT_COMPLETE) {
      observer.observe(document.body, { childList: true, subtree: true })
    } else {
      document.addEventListener(PLUGIN_REGISTRY_EVENT_READYSTATECHANGE, () => {
        if (document.readyState === PLUGIN_REGISTRY_EVENT_INTERACTIVE || document.readyState === PLUGIN_REGISTRY_EVENT_COMPLETE) {
          observer.observe(document.body, { childList: true, subtree: true })
        }
      })
    }
  } else {
    document.addEventListener('DOMSubtreeModified', () => {
      _loadPlugins.call(self)
    }, false)
  }
}

/** @ignore */
function _loadPlugins () {
  let self = this
  for (let i = 0; i < self._plugins.length; i++) {
    _loadPlugin.call(self, self._plugins[i])
  }
}

/** @ignore */
function _pluginLoadSelector (selector, moduleName) {
  return `${selector.trim()}:not([${PLUGIN_REGISTRY_ATTR_PLUGIN}~='${moduleName}'])`
}

/** @ignore */
function _loadPlugin (plugin) {
  let nodes = []
  let selectors = plugin.selector.split(',')
  let self = this
  if (selectors.length > 1) {
    for (let i = 0; i < selectors.length; i++) {
      let selector = _pluginLoadSelector.call(self, selectors[i], plugin.Module.name)
      let subNodes = document.querySelectorAll(selector)
      if (subNodes.length) {
        for (let s = 0; s < subNodes.length; s++) {
          nodes.push(subNodes[s])
        }
      }
    }
  } else {
    let selector = _pluginLoadSelector.call(self, plugin.selector, plugin.Module.name)
    nodes = document.querySelectorAll(selector)
  }
  if (nodes.length) {
    Logger.log(`${this.msgPrefix} _loadPlugin: Nodes found using selector ${plugin.selector}${this.selectorSuffix}`, nodes)
  } else {
    Logger.log(`${this.msgPrefix} _loadPlugin: No uninstantiated nodes found using selector ${plugin.selector}${this.selectorSuffix} at this time`)
    return
  }
  for (let i = 0; i < nodes.length; i++) {
    let el = nodes[i]
    /** there are two valid load types, stop if not found */
    if (typeof plugin.load === 'function') {
      el._bspLoadPromise = new Promise((resolve, reject) => {
        plugin.load(resolve, reject, el)
      }).then(() => {
        _doLoadPlugin.call(self, el, plugin)
      })
    } else if (plugin.load === PLUGIN_REGISTRY_EVENT_INTERACTIVE || plugin.load === PLUGIN_REGISTRY_EVENT_COMPLETE) {
      let _shouldDoLoadNow = () => {
        if (plugin.load === document.readyState || (plugin.load === PLUGIN_REGISTRY_EVENT_INTERACTIVE && document.readyState === PLUGIN_REGISTRY_EVENT_COMPLETE)) {
          return true
        }
      }
      if (_shouldDoLoadNow()) {
        _doLoadPlugin.call(self, el, plugin)
      } else {
        document.addEventListener(PLUGIN_REGISTRY_EVENT_READYSTATECHANGE, () => {
          if (_shouldDoLoadNow()) {
            _doLoadPlugin.call(self, el, plugin)
          }
        })
      }
    }
  }
}

/** @ignore */
function _randomizedModuleName (el, plugin) {
  let name = plugin.Module.name

  /** if module name is shorter than 2 characters, it was likely minified and we'll need to generate a name */
  if (name.length < 2 && !plugin.Module.generatedName) {
    name = `Module${Math.ceil(Math.random() * 100000)}`

    /** make sure it's not a duplicate */
    while (el[this.propPlugin][name]) {
      name = `Module${Math.ceil(Math.random() * 100000)}`
    }
  } else if (plugin.Module.generatedName) {
    name = plugin.Module.generatedName
  }

  plugin.Module.generatedName = name

  return name
}

/** @ignore */
function _doLoadPlugin (el, plugin) {
  try {
    if (!el[this.propPlugin]) {
      el[this.propPlugin] = {}
    }

    let self = this
    let moduleName = _randomizedModuleName.call(this, el, plugin)

    Logger.log(`${this.msgPrefix} _doLoadPlugin:`, el, plugin)

    if (!el[this.propPlugin][moduleName]) {
      /** make module instance accessible on the element */
      el[this.propPlugin][moduleName] = new plugin.Module(el, plugin.options)

      /** fire an event on the element after init */
      let e
      let eventData = {
        element: el,
        instance: el[self.propPlugin]
      }
      if (typeof window.CustomEvent === 'function') {
        e = new window.CustomEvent(PLUGIN_REGISTRY_EVENT_PLUGIN_LOADED, {
          detail: eventData
        })
      } else if (document.createEvent) {
        e = document.createEvent('CustomEvent')
        e.initCustomEvent(PLUGIN_REGISTRY_EVENT_PLUGIN_LOADED, true, false, eventData)
      }
      if (el.dispatchEvent) {
        el.dispatchEvent(e)
      }
    }

    /** add an attribute for selector filtering */
    el.setAttribute(this.attrPlugin, pluginAttributeValue(el, plugin.Module))
  } catch (e) {
    Logger.error(e)
  }
}

/** @ignore */
function pluginAttributeValue (el, Module) {
  let newValue = ''
  let attrValue = el.getAttribute(PLUGIN_REGISTRY_ATTR_PLUGIN)
  let moduleName = Module.generatedName
  if (attrValue && attrValue.indexOf(moduleName) > -1) {
    newValue = attrValue
  } else if (attrValue) {
    newValue = attrValue + ' ' + moduleName
  } else {
    newValue = moduleName
  }
  return newValue
}

/**
 * Export pre-baked load conditions
 */

/**
 * Plugin won't load until the element is visible
 * @param {Function} resolve - when the promise is resolved, the module can load
 * @param {Function} reject - if you want the module to fail loading
 * @param {Element} el - the element the plugin will load on
 */
export function loadWhenVisible (resolve, reject, el) {
  var observer = new window.MutationObserver((mutations) => {
    for (let i = 0; i < mutations.length; i++) {
      let mutation = mutations[i]
      if (mutation.attributeName === 'style' || mutation.attributeName === 'class') {
        if (el.clientWidth !== 0 && el.clientHeight !== 0 && el.style.opacity !== 0 && el.style.visibility !== 'hidden') {
          resolve()
          observer.disconnect()
        }
      }
    }
  })
  observer.observe(el, {
    attributes: true
  })
}

/**
 * Plugin won't load until scripts are loaded, this works as a factory
 * that creates a resolve function. Only attempts to load scripts on pages
 * where at least one element with the specified selector is found.
 * @example
 * plugins.register(MyPlugin, '.MyPlugin', loadWhenScriptsReady([
 *   '/some/script1.js',
 *   '/some/script2.js'
 * ]))
 * @param {String|Array} sources - Javascripts to load before plugin init
 * @return {Function} - resolve function
 */
let loadPromises = {}
export function loadWhenScriptsReady (sources) {
  let allPromises = []
  if (typeof sources === 'string') {
    sources = [sources]
  }
  if (sources.constructor !== Array) {
    throw new Error('loadWhenScriptsReady requires an a string or an array as the first argument')
  }
  sources.forEach((src) => {
    if (!loadPromises[src]) {
      loadPromises[src] = new Promise((resolve, reject) => {
        let s = document.createElement('script')
        s.src = src
        s.addEventListener('load', resolve)
        document.querySelector('head').appendChild(s)
      })
    }
    allPromises.push(loadPromises[src])
  })
  return function (resolve, reject) {
    Promise.all(allPromises).then(resolve)
  }
}

/** @ignore */
let shouldLoadLazyModules = false

/**
 * @ignore
 * @todo this is crude and there is probably a better way to do it
 */
window.addEventListener('load', () => {
  setTimeout(() => {
    shouldLoadLazyModules = true
  }, 500)
})

/**
 * Will never resolve unless module is added after window onload
 * @param {Function} resolve - when the promise is resolved, the module can load
 * @param {Function} reject - if you want the module to fail loading
 * @param {Element} el - the element the plugin will load on
 */
export function loadWhenLazy (resolve, reject, el) {
  if (shouldLoadLazyModules) {
    resolve()
  }
}

/**
 * Export a singleton shared across the site
 */
let plugins = new PluginRegistry()
export { plugins }
export default plugins
