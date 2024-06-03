export class ComponentLoader {
  customElementsMap = new Map()

  mutationObserver = new MutationObserver((mutationList) => {
    for (const mutation of mutationList) {
      for (const addedNode of mutation.addedNodes) {
        if (!(addedNode instanceof Element)) continue

        for (const [elementName, component] of this.customElementsMap) {
          if (
            addedNode.matches(elementName) ||
            addedNode.querySelector(elementName)
          ) {
            this.registerCustomElementWithDynamicImport(component)
          }
        }
      }
    }
  })

  constructor() {
    this.mutationObserver.observe(document.body, {
      childList: true,
      subtree: true,
    })
  }

  registerComponent(component) {
    this.customElementsMap.set(component.elementName, component)
    if (document.querySelector(component.elementName)) {
      this.registerCustomElementWithDynamicImport(component)
    }
  }

  async registerCustomElementWithDynamicImport({ elementName, importModule }) {
    try {
      this.customElementsMap.delete(elementName)
      const { default: componentClass } = await importModule()
      window.customElements.define(elementName, componentClass)
    } catch (error) {
      console.error(`Error importing "${elementName}":`, error)
    }
  }
}
