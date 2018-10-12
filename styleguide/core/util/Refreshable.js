import * as VirtualDom from './VirtualDom.js'

const parser = new window.DOMParser()

/**
 * A Class which represents a component
 * which can update or "refresh" DOM nodes
 */
export default class Refreshable {

  /**
   * Subclasses must invoke {@code super()}
   * for the Refreshable to initialize.
   */
  constructor (element) {
    this.element = element
    this.virtualElement = VirtualDom.virtualize(element)

    this.refresh = this.refresh.bind(this)
    this.then = this.then.bind(this)

    this.when().then((responseText) => {
      this.then(responseText)
    })
  }

  /**
   * Returns a Promise. The instance's refresh method
   * will be invoked on resolve.
   */
  when () { }

  /**
   * Executed as "resolve" for the Promise produced
   * by when().
   */
  then (responseText) {
    if (!responseText) {
      return
    }

    this.elementClass = this.element.classList[0]
    const elementsOfClass = document.querySelectorAll(`.${this.elementClass}`)
    this.elementsOfClassIndex = [...elementsOfClass].indexOf(this.element)

    const newDocument = parser.parseFromString(responseText, 'text/html')
    const newElementsOfClass = newDocument.querySelectorAll(`.${this.elementClass}`)
    const newDomNode = [...newElementsOfClass][this.elementsOfClassIndex]

    this.refresh(newDomNode)
  }

  /**
   * Implementations can override this method
   * to add a VNodeStyle, which will be used  (https://github.com/snabbdom/snabbdom/blob/master/src/modules/style.ts#L4).
   *
   */
  getRemoveStyle () { }

  /**
   * Implementations can override this method
   * to add a VNodeStyle (https://github.com/snabbdom/snabbdom/blob/master/src/modules/style.ts#L4).
   *
   */
  getUpdateStyle () { }

  /**
   * Performs the refresh.
   */
  refresh (newDomNode) {
    const removeStyle = this.getRemoveStyle()
    const updateStyle = this.getUpdateStyle()
    const newNode = VirtualDom.virtualize(newDomNode)

    if (removeStyle) {
      this.virtualElement.data.style = removeStyle
    }

    if (updateStyle) {
      newNode.data.style = updateStyle
    }

    if (newDomNode) {
      VirtualDom.patch(this.virtualElement, VirtualDom.virtualize(newDomNode))
    }
  }
}
