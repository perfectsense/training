import plugins from 'pluginRegistry'
import { Tabs } from 'tabs'

export class AnchorTabs extends Tabs {

  constructor (el) {
    super(el)
    super.init()

    this.setBinds()
  }

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
        e.preventDefault()
        const hash = e.currentTarget.hash.substr(1)
        const hasId = this.ids.some((id) => {
          document.getElementById(hash).scrollIntoView({
            behavior: 'smooth',
            alignToTop: true,
            block: 'start'
          })
          return hash === id
        })

        if (!hasId) {
          return
        }

        if (hasId) {
          this.activePanel = hash
        }
      })
    })
  }
}

plugins.register(AnchorTabs, '[data-widget=anchortabs]')
