export default class Modal {
  private modal: HTMLDialogElement
  private modalId: string = ''
  private closeButton: HTMLElement | null = null

  constructor(modalDiv: HTMLDialogElement) {
    this.modal = modalDiv

    if (!this.modal) {
      return
    }

    this.modalId = this.modal.getAttribute('id') || ''
    this.closeButton = this.modal.querySelector('[data-bsp-modal-close]')

    if (this.closeButton) {
      this.closeButton.addEventListener('click', () => {
        this.close()
      })
    }

    this.modal.addEventListener('click', (e) => {
      console.log(e.target)
      if (e.target === this.modal) {
        this.close()
      }
    })

    document.addEventListener('keydown', (e) => {
      if (e.key === 'Escape') {
        this.close()
      }
    })
  }

  open() {
    this.modal.showModal()
    this.modal.setAttribute('data-bsp-modal-open', 'true')
    document.body.setAttribute('data-bsp-modal-open', this.modalId)
    this.modal.dispatchEvent(new Event('open'))
  }

  close() {
    this.modal.close()
    this.modal.removeAttribute('data-bsp-modal-open')
    document.body.removeAttribute('data-bsp-modal-open')
    this.modal.dispatchEvent(new Event('close'))
  }

  on(event: string, callback: () => void): void {
    this.modal.addEventListener(event, callback)
  }
}
