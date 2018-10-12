/* eslint-disable no-unused-vars */
import $ from 'jquery'
import plugins from '../core/PluginRegistry.js'
import { KalturaVideoPlayer } from './KalturaVideoPlayer.js'

plugins.register(KalturaVideoPlayer, '.KalturaVideoPlayer')

export default {}
