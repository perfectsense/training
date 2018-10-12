import plugins from './PluginRegistry.js'

import PageHeader from './page/PageHeader.js'
import PageHeaderSearch from './page/PageHeaderSearch.js'
import PageNavigationItems from './navigation/NavigationItems.js'
import { HTML5VideoPlayer } from './video/HTML5VideoPlayer.js'
import { Banner } from './banner/Banner.js'
import { Tabs } from './tab/Tabs.js'

plugins.register(Banner, '.Banner')
plugins.register(HTML5VideoPlayer, '.HTML5VideoPlayer')

plugins.register(PageHeader, '.PageHeader')
plugins.register(PageHeaderSearch, '.PageHeaderSearch')
plugins.register(PageNavigationItems, '.PageNavigationItem-items')
plugins.register(Tabs, '.Tabs')

export default {}
