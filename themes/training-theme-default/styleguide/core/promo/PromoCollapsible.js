export class BSPAccordion extends window.HTMLElement {
  connectedCallback () {
    //   Select the data attribute on the element we want and add an event listener
    this.querySelector('[data-accordion-trigger]').addEventListener(
      'click',
      e => {
        e.preventDefault()
        this.toggleAccordion()
      },
      true
    )

    // Accessibility: escape key hides Accordion
    document.addEventListener('keydown', e => {
      let isEscape = false

      if ('key' in e) {
        isEscape = e.key === 'Escape' || e.key === 'Esc'
      } else {
        isEscape = e.keyCode === 27
      }

      if (isEscape) {
        this.hideAccordion()
      }
    })
  }

  // Toggle will call methods to change the data attribute on the element to give use a hook we can bind into with our css
  toggleAccordion () {
    if (this.isAccordionShowing()) {
      this.hideAccordion()
    } else {
      this.showAccordion()
    }
  }

  isAccordionShowing () {
    let accordion = this.querySelector('.PromoCollapsible-accordion')

    if (accordion) {
      if (accordion.getAttribute('data-showing') === 'true') {
        return true
      } else {
        return false
      }
    } else {
      return false
    }
  }

  showAccordion () {
    this.querySelector('.PromoCollapsible-accordion').setAttribute(
      'data-showing',
      true
    )
  }

  hideAccordion () {
    this.querySelector('.PromoCollapsible-accordion').setAttribute(
      'data-showing',
      false
    )
  }
}
