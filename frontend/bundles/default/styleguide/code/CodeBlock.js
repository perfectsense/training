import Prism from 'prismjs'

const languageMap = {
  'text/plain': '',
  'text/x-java': 'java',
  'text/x-markdown': 'markdown',
  'text/x-handlebars-template': 'handlebars',
  'application/json': 'json',
  'application/x-sh': 'sh-session',
  'text/css': 'css',
  'text/html': 'html',
  'text/javascript': 'javascript',
  'text/x-less': 'less',
  'text/x-mysql': 'sql',
  'text/x-properties': 'properties',
  'application/xml': 'xml',
  'application/x-jsp': 'java',
  'text/x-sql': 'sql',
  'application/graphql': 'graphql',
  'text/x-c++src': 'cpp',
  'text/x-python': 'python',
}

Prism.manual = true

export default class CodeBlock extends HTMLElement {
  get toggleButton() {
    return this.querySelector('.CodeBlock-toggle')
  }

  connectedCallback() {
    this.initialisePrism()
    this.initialiseBehaviour()
  }

  initialiseBehaviour() {
    if (this.toggleButton) {
      this.toggleButton.addEventListener('click', () => {
        const collapsed = this.toggleAttribute('data-collapsed')
        if (!collapsed) Prism.highlightAllUnder(this)
      })
    }
  }

  initialisePrism() {
    const preElement = this.querySelector('pre')
    const language = this.getAttribute('data-language')
    const languageClass = languageMap[language] ? languageMap[language] : ''
    preElement.classList.replace('language-', `language-${languageClass}`)
    Prism.highlightAllUnder(this)

    // used to determine height so the left numbered column background color is the same height as it's parent when highlight is explicitly defined
    const parent = this.querySelector('code')
    const child = this.querySelector('.line-numbers-rows')
    if (child && parent) {
      const parentHeight = parent.offsetHeight
      child.style.height = `${parentHeight}px`
    }
  }
}
