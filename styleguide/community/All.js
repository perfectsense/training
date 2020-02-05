import Commenting from './commenting/Commenting.js'
import AffinityToggleButton from './affinity/AffinityToggleButton.js'
import plugins from '../core/PluginRegistry.js'

plugins.register(Commenting, '.Commenting')
plugins.register(AffinityToggleButton, '.FavoriteButton')
plugins.register(AffinityToggleButton, '.BookmarkButton')
plugins.register(AffinityToggleButton, '.FollowButton')

export default {}
