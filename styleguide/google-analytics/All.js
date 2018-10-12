import plugins from '../core/PluginRegistry.js'

import { GoogleVideoAnalytics } from './GoogleVideoAnalytics.js'
import { GoogleCtaButtonAnalytics } from './GoogleCtaButtonAnalytics'
import { GoogleScrollingAnalytics } from './GoogleScrollingAnalytics'

plugins.register(GoogleVideoAnalytics, '[data-google-video-analytics]')
plugins.register(GoogleScrollingAnalytics, '[data-google-scrolling-analytics]')
plugins.register(GoogleCtaButtonAnalytics, '[data-google-link-analytics=cta]')
