import plugins from '../core/PluginRegistry.js'
import AuthenticateNavigation from './navigation/AuthenticateNavigation'
plugins.register(AuthenticateNavigation, '.Navigation')
