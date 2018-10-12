/* eslint-disable no-unused-vars */
import plugins from './core/PluginRegistry.js'
import Logger from './core/util/Logger.js'

import jwplayer from './jwplayer/All'
import mpx from './mpx/All.js'
import kaltura from './kaltura/All.js'
import youtube from './youtube/All.js'
import googleAnalytics from './google-analytics/All.js'
import vimeo from './vimeo/All.js'

import core from './core/All.js'
import uncache from './core/util/Uncache.js'

import { GoogleDfp } from './dfp/GoogleDfp.js'

plugins.register(GoogleDfp, '.GoogleDfpAd')

export default {}
/* eslint-enable no-unused-vars */
