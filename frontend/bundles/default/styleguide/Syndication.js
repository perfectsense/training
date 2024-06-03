import documentReady from './util/documentReady'

const registerSyndicatedContentElements = () => {
  const contentPlacholders = document.querySelectorAll(
    '[data-brightspot-content-src]'
  )

  const groupedContentPlacholders = {}

  contentPlacholders.forEach((placeholder) => {
    const src = placeholder.getAttribute('data-brightspot-content-src')

    if (src in groupedContentPlacholders) {
      groupedContentPlacholders[src].push(placeholder)
    } else {
      groupedContentPlacholders[src] = [placeholder]
    }
  })

  const appendedHeadElements = new Set()

  for (const [src, contentPlacholders] of Object.entries(
    groupedContentPlacholders
  )) {
    fetch(src)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`${response.status}: ${response.statusText}`)
        }
        return response.text()
      })
      .then((html) => {
        const parser = new DOMParser()
        const dom = parser.parseFromString(html, 'text/html')
        return dom
      })
      .then((syndicatedDOM) => {
        const syndicatedHead = syndicatedDOM.querySelector('head')
        const headNodes = syndicatedHead.querySelectorAll(
          'script, link[rel="stylesheet"], style'
        )

        for (const node of headNodes) {
          if (appendedHeadElements.has(node.outerHTML)) {
            continue
          }

          if (node.tagName === 'SCRIPT') {
            const script = document.createElement('script')
            if (node.text) script.text = node.text
            if (node.src) script.src = node.src
            if (node.async) script.async = true
            if (node.defer) script.defer = true
            document.head.appendChild(script)
            appendedHeadElements.add(node.outerHTML)
          } else {
            document.head.appendChild(node)
            appendedHeadElements.add(node.outerHTML)
          }
        }

        for (const contentPlacholder of contentPlacholders) {
          const contentSelector = contentPlacholder.getAttribute(
            'data-brightspot-content-selector'
          )

          let syndicatedContent

          if (contentSelector) {
            syndicatedContent = syndicatedDOM.querySelectorAll(contentSelector)
          } else {
            syndicatedContent = syndicatedDOM.querySelector('body').children
          }

          contentPlacholder.replaceWith(...syndicatedContent)
        }
      })
      .catch(console.error)
  }
}

documentReady.then(() => {
  registerSyndicatedContentElements()
})
