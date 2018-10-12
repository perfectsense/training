const DEFAULTS = {
  dataActive: 'data-active',
  dataVisible: 'data-visible',
  selTab: '[data-tab]',
  selPanel: '[data-tab-panel]',
  dataExpanded: 'data-expanded'
}

/**
 * @module Tabs
 * Transforms a set of DOM elements into a tabbed widget, which is
 * a horizontal navigation (tabs) that shows/hides content in
 * container Elements (elTabPanels) but is capable of displaying in accordion
 * mode if needed for responsive design.
 *
 * Panels respond to URL hash changes and not event click handlers,
 * so that when the page is first loaded, the correct panel will be displayed if there is
 * a #id in the URL that matches an on a Tabs-panel. When the user
 * clicks on a navigation link in a Tab, the natural behavior of
 * the browser takes over and appends the href # to the URL. This
 * allows the page with the open tab to be stored in history, bookmarkable,
 * and shareable.
 *
 * The `data-expanded` attribute allows for the widget to have toggling ability for
 * when the user clicks on the active tab in multiple succession. This is useful
 * for when rendering in an accordion view.
 *
 * Note: The Tabs Tab text is denoted in the JSON as 'label'. 'title'
 * represents the title of the module to be displayed on the UI.
 *
 * @example - Minimum Markup Needed
 * <div class="Tabs" data-bsp-plugin="Tabs">
 *  <ul class="Tabs-tabs">
 *    <li>
 *      <a class="Tabs-tab" href="#1" data-active="true">Kiaf</a>
 *    </li>
 *  </ul>
 *  <div class="Tabs-content">
 *    <div class="Tabs-panel" id="1" data-visible="true">
 *      <div class="TabItem">
 *        <div class="TabItem-content"></div>
 *      </div>
 *    </div>
 *  </div>
 * </div>
 */
export class Tabs {
  constructor (el, settings) {
    /**
     * @property {!Element} - The root Tabs DOM Element.
     */
    this.el = el
    this.settings = Object.assign({}, DEFAULTS, settings)
    this.init()
    this.setBinds()
  }

  init () {
    /**
     * @property {Array.<Element>} - The tab DOM Elements that
     * the user selects to swap the content views.
     */
    this.elTabs = [...this.el.querySelectorAll(this.settings.selTab)]

    /**
     * @property {Array.<Element>} - The tab panels DOM Elements,
     * which contain the Tab content.
     */
    this.elTabPanels = [...this.el.querySelectorAll(this.settings.selPanel)]

    if (!this.elTabs || !this.elTabs.length || !this.elTabPanels || !this.elTabPanels.length) {
      console.error('DOM elements for Tabs are missing, failing fast.')
    }

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

    /**
     * @property {number|text|null} - The tab panels ID that is active/visible
     * to the user.
     */
    this.activePanel = window.location.hash.length && hasId
      ? window.location.hash.substr(1)
      : this.elTabPanels[0].id
  }

  /**
   * Attaches the onchashchange listener to the window and
   * sets the activePanel class property to the hash value, then
   * calls the method to set the active tabs and panels.
   */
  setBinds () {
    window.onhashchange = () => {
      const hash = window.location.hash.substr(1)
      const hasId = this.ids.some((id) => {
        return hash === id
      })

      if (hasId) {
        this.activePanel = hash
      }
    }

    this.elTabs.forEach((elTab) => {
      elTab.addEventListener('click', (e) => {
        const hash = window.location.hash.substr(1)
        const hasId = this.ids.some((id) => {
          return hash === id
        })

        if (!hasId) {
          return
        }

        if (elTab.getAttribute('href').substr(1) === this.activePanel) {
          e.preventDefault()

          const isExpanded = elTab.getAttribute(DEFAULTS.dataExpanded)
          if (isExpanded) {
            elTab.removeAttribute(DEFAULTS.dataExpanded)
          } else {
            elTab.setAttribute(DEFAULTS.dataExpanded, true)
          }
        } else {
          elTab.removeAttribute(DEFAULTS.dataExpanded)
        }

        this.elTabPanels.forEach((elTabPanel) => {
          if (elTabPanel.id === this.activePanel) {
            const isExpanded = elTabPanel.getAttribute(DEFAULTS.dataExpanded)

            if (isExpanded) {
              elTabPanel.removeAttribute(DEFAULTS.dataExpanded)
            } else {
              elTabPanel.setAttribute(DEFAULTS.dataExpanded, true)
            }
          } else {
            elTabPanel.removeAttribute(DEFAULTS.dataExpanded)
          }
        })
      })
    })
  }

  /**
   * Sets the active/visibility of the tabs and panels based on whether
   * the tab/panel is the same as the active panel.
   *
   * Note: This could be abstracted
   * out into one generic for each function, but leaving this broken out into
   * two calls for readability.
   */
  set activePanel (val) {
    if (this._activePanel !== val) {
      this._activePanel = val

      this.elTabs.forEach((elTab) => {
        const isActive = val === elTab.getAttribute('href').substr(1)
        const parent = elTab.parentNode
        elTab.setAttribute(this.settings.dataActive, isActive)
        if (isActive) {
          elTab.setAttribute(DEFAULTS.dataExpanded, true)
        } else {
          elTab.removeAttribute(DEFAULTS.dataExpanded)
        }
        parent.setAttribute(this.settings.dataActive, isActive)
      })

      this.elTabPanels.forEach((elTabPanel) => {
        const isActive = val === elTabPanel.id
        elTabPanel.setAttribute(this.settings.dataVisible, isActive)
        if (isActive) {
          elTabPanel.setAttribute(DEFAULTS.dataExpanded, true)
        } else {
          elTabPanel.removeAttribute(DEFAULTS.dataExpanded)
        }
      })
    }

    return this._activePanel
  }

  get activePanel () {
    return this._activePanel
  }

}
