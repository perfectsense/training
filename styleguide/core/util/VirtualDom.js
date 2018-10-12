import * as Snabbdom from 'snabbdom'
import {toVNode} from 'snabbdom-tovnode'
import attributesModule from 'snabbdom-attributes'
import styleModule from 'snabbdom-style'

const PATCH = Snabbdom.init([
  attributesModule,
  styleModule
])

/**
 * Takes a DOM Node and create a Virtual Node
 */
export function virtualize (domNode) {
  return toVNode(domNode)
}

export function patch (oldNode, newNode) {
  PATCH(oldNode, newNode)
}
