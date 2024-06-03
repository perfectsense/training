export class SearchSorts extends HTMLElement {
  connectedCallback() {
    this.sortInputs = this.querySelectorAll('.SearchSortInput')
    this.dialogElement = this.querySelector('dialog')
    this.buttonElement = this.querySelector('button')
    this.buttonTextElement = this.buttonElement.querySelector('span')
    this.inputElement = this.querySelector('input')
    this.sortInputs.forEach((sortInput) => {
      sortInput.addEventListener('click', () => {
        this.buttonTextElement.innerText = sortInput.innerText
        this.inputElement.value = sortInput.value
        this.closest('bsp-search-results-module').submitSearch()
      })
    })
    this.buttonElement.addEventListener('click', () => {
      this.toggle()
    })
    document.addEventListener('click', this)
    document.addEventListener('keydown', this)
  }

  handleEvent(event) {
    switch (event.type) {
      case 'click':
        this.handleClick(event)
        break
      case 'keydown':
        this.handleKeydown(event)
    }
  }

  handleClick(event) {
    if (event.target !== this && !this.contains(event.target)) {
      this.close()
    }
  }

  handleKeydown(event) {
    if (event.key === 'Escape') {
      this.close()
    }
  }

  open() {
    this.toggleAttribute('data-open', true)
    this.dialogElement.show()
  }

  close() {
    this.toggleAttribute('data-open', false)
    this.dialogElement.close()
  }

  toggle() {
    if (this.dialogElement.open) {
      this.close()
    } else {
      this.open()
    }
  }
}
