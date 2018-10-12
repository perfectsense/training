/* eslint-disable no-undef */

export class SearchForm {
  constructor (container) {
    this.container = container
    this.form = this.container.getElementsByTagName(`form`)[0]

    this.selectors = {
      items: `.SearchResultsPage-items`
    }

    container.querySelector(`.SearchResultsPage-panelControls-filters`).onclick = evt => {
      evt.preventDefault()
      const control = evt.currentTarget
      const panel = document.querySelector(`.SearchResultsPage-panels .SearchResultsPage-filters`)

      if (control.getAttribute(`data-selected`) === `true`) {
        this.deselectControl(control)
        this.hidePanel(panel)
        this.container.querySelector(`.SearchResultsPage-panels`).classList.remove(`visible`)
      } else {
        this.selectControl(control)
        this.showPanel(panel)
      }
    }

    container.querySelector(`.SearchResultsPage-panelControls-sorts`).onclick = evt => {
      evt.preventDefault()
      const control = evt.currentTarget
      const panel = document.querySelector(`.SearchResultsPage-panels .SearchResultsPage-sorts`)

      if (control.getAttribute(`data-selected`) === `true`) {
        this.deselectControl(control)
        this.hidePanel(panel)
        this.container.querySelector(`.SearchResultsPage-panels`).classList.remove(`visible`)
      } else {
        this.selectControl(control)
        this.showPanel(panel)
      }
    }

    // autosubmit the form when an input is changed.
    container.querySelectorAll(`.SearchResultsPage-options-wrapper input`).forEach(el => {
      el.onchange = (evt) => {
        window.location.href = el.getAttribute(`data-value`)
      }
    })
  }

  deselectControl (control) {
    control.setAttribute(`data-selected`, ``)
  }

  selectControl (newControl) {
    this.container.querySelectorAll(`.SearchResultsPage-panelControls-control[data-selected='true']`).forEach(control => {
      this.deselectControl(control)
    })

    newControl.setAttribute(`data-selected`, true)
  }

  hidePanel (panel) {
    panel.classList.remove(`visible`)
  }

  showPanel (newPanel) {
    this.container.querySelector(`.SearchResultsPage-panels`).classList.add(`visible`)
    this.container.querySelectorAll(`.SearchResultsPage-panels .panel`).forEach(panel => {
      this.hidePanel(panel)
    })

    newPanel.classList.add(`visible`)
  }
}
