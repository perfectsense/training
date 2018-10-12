/* eslint-disable no-unused-vars */
import $ from 'jquery'
import plugins from '../core/PluginRegistry.js'
import { YouTubeVideoPlayer } from './YouTubeVideoPlayer.js'

plugins.register(YouTubeVideoPlayer, '.YouTubeVideoPlayer')

export default {}
