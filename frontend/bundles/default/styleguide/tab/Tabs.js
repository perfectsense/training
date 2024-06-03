const dataActive = 'data-active'
const dataVisible = 'data-visible'
const selTab = '[data-tab]'
const selPanel = '[data-tab-panel]'

export default class Tabs extends HTMLElement {
  connectedCallback() {
    this.init()
    this.setBinds()
  }

  init() {
    /**
     * @property {Array.<Element>} - The tab DOM Elements that
     * the user selects to swap the content views.
     */
    this.elTabs = [...this.querySelectorAll(selTab)]

    /**
     * @property {Array.<Element>} - The tab panels DOM Elements,
     * which contain the Tab content.
     */
    this.elTabPanels = [...this.querySelectorAll(selPanel)]

    /**
     * @property {Array.<number|string>} - All of the ids of the panels in this widget. We keep
     * track of this so that we can distinguish if the hash changes pertain to this instance
     * rather than another instance present in the DOM.
     */
    this.ids = this.elTabPanels.map((tab) => {
      return tab.id
    })

    const hash = window.location.hash.substr(1)
    const hasId = this.ids.some((id) => {
      return hash === id
    })

    const activeLink = this.querySelector(`[href$="${hash}"]`)
    window.addEventListener('load', () => {
      if (activeLink) {
        activeLink.parentNode.scrollIntoView({ block: 'nearest' })
      }
    })

    /**
     * @property {number|text|null} - The tab panels ID that is active/visible
     * to the user.
     */
    this.activePanel =
      window.location.hash.length && hasId
        ? window.location.hash.substr(1)
        : this.elTabPanels[0].id
  }

  /**
   * Attaches the onchashchange listener to the window and
   * sets the activePanel class property to the hash value, then
   * calls the method to set the active tabs and panels.
   */
  setBinds() {
    window.addEventListener('hashchange', () => {
      const hash = window.location.hash.substr(1)
      const hasId = this.ids.some((id) => {
        return hash === id
      })

      if (hasId) {
        this.activePanel = hash
      }
    })

    this.elTabs.forEach((elTab) => {
      elTab.addEventListener('click', (e) => {
        const hash = e.currentTarget.href.split('#').pop()

        if (!hash) return

        history.pushState(null, null, e.currentTarget.href)

        const hasId = this.ids.some((id) => {
          return hash === id
        })

        if (!hasId) {
          return
        }

        if (hasId) {
          e.preventDefault()
          this.activePanel = hash
        }
      })
    })
  }

  get activePanel() {
    return this._activePanel
  }

  /**
   * Sets the active/visibility of the tabs and panels based on whether
   * the tab/panel is the same as the active panel.
   *
   * Note: This could be abstracted
   * out into one generic for each function, but leaving this broken out into
   * two calls for readability.
   */
  set activePanel(val) {
    if (this._activePanel !== val) {
      this._activePanel = val

      this.elTabs.forEach((elTab) => {
        const isActive = val === elTab.getAttribute('href').substr(1)
        isActive
          ? elTab.setAttribute(dataActive, '')
          : elTab.removeAttribute(dataActive)
      })

      this.transitionPanels(val)
    }

    window.dispatchEvent(new Event('resize'))
  }

  transitionPanels(val) {
    this.elTabPanels.forEach((elTabPanel) => {
      const isActive = val === elTabPanel.id

      isActive
        ? elTabPanel.setAttribute(dataVisible, '')
        : elTabPanel.removeAttribute(dataVisible)
    })
  }

  clearTransitionProperties(panel) {
    panel.style.removeProperty('height')
    panel.style.removeProperty('opacity')
    panel.style.removeProperty('overflow')
    panel.style.removeProperty('display')
    panel.style.removeProperty('transition')
  }
}
