/* eslint-disable no-unused-vars */
// All.js
import $ from 'jquery'
import plugins from 'pluginRegistry'

import { psdToggler } from './util/psd-toggler'
import lazysizes from 'lazysizes'
import AnchorTabs from './core/tab/AnchorTabs.js'
import Carousel from './core/carousel/Carousel.js'
import { googleAnalytics } from 'googleAnalytics'
import LightboxGallery from './core/gallery/LightboxGallery'
import ListMasonry from './core/list/ListMasonry'
import { PlyrFunctions } from './core/video/PlyrFunctions.js'
import SearchOverlay from './core/search/SearchOverlay.js'
import { Tabs } from 'tabs'
import VideoEvents from './core/video/VideoEvents.js'
import VideoLead from './core/video/VideoLead.js'

plugins.register(Tabs, '[data-widget=Tabs]')
plugins.register(PlyrFunctions, '[data-embeddedvideo-container]')
