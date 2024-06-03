export default class OverflowContainer extends HTMLElement {
  connectedCallback() {
    this.resizeObserver = new ResizeObserver(() => this.setOverflowState())
    this.resizeObserver.observe(this)
    this.addEventListener('scroll', () => this.setOverflowState())
  }

  setOverflowState() {
    this.toggleAttribute(
      'data-overflow-left',
      this.scrollWidth > this.clientWidth && this.scrollLeft > 0
    )

    this.toggleAttribute(
      'data-overflow-right',
      this.scrollWidth > this.clientWidth &&
        this.scrollLeft + this.clientWidth < this.scrollWidth
    )

    this.toggleAttribute(
      'data-overflow-top',
      this.scrollHeight > this.clientHeight && this.scrollTop > 0
    )

    this.toggleAttribute(
      'data-overflow-bottom',
      this.scrollHeight > this.clientHeight &&
        this.scrollTop + this.clientHeight < this.scrollHeight
    )
  }
}
