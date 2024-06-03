export const events = {
  active: 'Chapter:active',
}

export default class Chapter extends HTMLElement {
  connectedCallback() {
    document.addEventListener('click', this)
    this.addEventListener('click', this)
    document.addEventListener(events.active, this)
    window.addEventListener('popstate', this)
    this.setAnchorChaptersBehaviour()

    if (this.isCurrent) {
      this.emit(events.active)
    }
  }

  async setAnchorChaptersBehaviour() {
    window.addEventListener('scroll', this, true)
    window.addEventListener('resize', this)

    await customElements.whenDefined(this.localName)
    this.anchorChapters = [...this.childChapters].filter((chapter) => {
      return !!chapter.anchorTarget
    })

    this.setAnchorChaptersState()

    if (this.anchorChapters.length > 0) {
      window.addEventListener('scroll', this, true)
      window.addEventListener('resize', this)
    } else {
      window.removeEventListener('scroll', this, true)
      window.removeEventListener('resize', this)
    }
  }

  setAnchorChaptersState() {
    const targetChapter = this.anchorChapters
      .filter((chapter) => chapter.anchorTarget.offsetParent !== null)
      .reduce((accumulator, current) => {
        if (
          current.anchorTarget.getBoundingClientRect().top <=
          this.scrollDisplacement + 1
        ) {
          return current
        }

        return accumulator
      }, null)

    const previouslyActiveLinks = this.querySelectorAll('[data-active]')
    previouslyActiveLinks.forEach((link) => {
      link.removeAttribute('data-active')
    })

    this.anchorChapters.forEach((chapter) => {
      chapter.isCurrent = chapter === targetChapter
    })
  }

  removeExpandedAttribute() {
    if (this.book && this.book.hasAttribute('expanded')) {
      document.body.style.overflow = 'visible'
      this.book.removeAttribute('expanded')
    }
  }

  handleEvent(event) {
    if (event.type === 'resize') {
      this.onResize()
    }

    if (event.type === 'scroll') {
      this.onScroll(event)
    }

    if (event.type === 'click') {
      this.onClick(event)
    }

    if (event.type === 'popstate') {
      this.onPopstate(event)
    }

    if (event.type === events.active) {
      this.onActive(event)
    }
  }

  get header() {
    return this.querySelector(':scope > .Chapter-header')
  }

  get link() {
    return this.header.querySelector('.Chapter-title a')
  }

  get url() {
    if (this.link && this.link.href) {
      return new URL(this.link.href)
    }

    return null
  }

  get book() {
    return this.closest('bsp-book')
  }

  set isCurrent(bool) {
    bool
      ? this.setAttribute('data-current', '')
      : this.removeAttribute('data-current')
  }

  get isCurrent() {
    return this.hasAttribute('data-current')
  }

  get isExternal() {
    return this.hasAttribute('data-external')
  }

  set isExpanded(bool) {
    bool
      ? this.setAttribute('data-expanded', '')
      : this.removeAttribute('data-expanded')
  }

  get isExpanded() {
    return this.hasAttribute('data-expanded')
  }

  get childChapters() {
    return this.querySelectorAll(':scope > ul > li > bsp-chapter')
  }

  get anchorTarget() {
    if (!this.url) return null
    const hash = this.url.hash
    if (!hash) return null

    const anchorTarget = document.querySelector(hash)

    if (
      this.url.pathname === location.pathname &&
      this.url.hash &&
      !anchorTarget
    ) {
      return null
    }

    return anchorTarget
  }

  async getContent(isPoppedEvent = false) {
    if (this.isCurrent) return

    const url = new URL(this.url)
    url.searchParams.set('fragment', true)
    const response = await fetch(url)
    const html = await response.text()
    const parser = new DOMParser()
    const newDocument = parser.parseFromString(html, 'text/html')
    const newContent = newDocument.querySelector('[data-companion-content]')
    const oldContent = document.querySelector('[data-companion-content]')
    oldContent.parentNode.replaceChild(newContent, oldContent)
    const newSupplementaryContent = newDocument.querySelector(
      '[data-supplementary-content]'
    )
    const oldSupplementaryContent = document.querySelector(
      '[data-supplementary-content]'
    )
    if (newSupplementaryContent && oldSupplementaryContent) {
      oldSupplementaryContent.innerHTML = newSupplementaryContent.innerHTML
    }

    document.title = newDocument.title

    this.isCurrent = true
    this.emit(events.active)

    const targetElement =
      url.hash && url.hash.length > 1 ? document.querySelector(url.hash) : null

    if (targetElement) {
      targetElement.scrollIntoView()
    } else {
      const margin = 40
      const scrollDisplacement = margin

      const positionY =
        newContent.getBoundingClientRect().top +
        document.documentElement.scrollTop -
        scrollDisplacement

      if (window.scrollY > positionY) {
        window.scrollTo(0, positionY)
      }

      if (this.childChapters.length > 0) {
        this.isExpanded = true
      }
    }

    document.body.dispatchEvent(new Event('Ajax:Rendered'))
    if (!isPoppedEvent) {
      window.history.pushState({}, '', this.url)
    }
  }

  onClick(event) {
    if (event.currentTarget === document) {
      if (!this.url) return // guard against handling when a chapter has no url
      const targetLink = event.target.matches(`a[href="${this.url}"]`)
        ? event.target
        : event.target.closest(`a[href="${this.url}"]`)
      if (!targetLink) return
      if (targetLink.matches('[target="_blank]"')) return
      if (location.href.split('#')[0] === targetLink.href.split('#')[0]) return // guard against handling current page

      event.preventDefault()

      this.getContent()
      this.removeExpandedAttribute()

      return // guard against handling other document clicks
    }

    event.stopPropagation() // stop propagation to parent chapters

    // Allow modifier clicks
    if (event.metaKey || event.ctrlKey) {
      return
    }

    // Anchor links
    if (this.anchorTarget) {
      this.removeExpandedAttribute()

      return
    }

    const chapterIcon = event.target.matches('.Chapter-icon, .Chapter-icon *')

    const noLinkTitleWithChildren =
      !this.link &&
      this.childChapters.length > 0 &&
      event.target.matches('.Chapter-title, .Chapter-title *')

    const currentPageTitleWithChildren =
      this.link &&
      this.childChapters.length > 0 &&
      event.target.matches('.Chapter-title, .Chapter-title *') &&
      location.href.split('#')[0] === this.url.href.split('#')[0]

    if (
      chapterIcon ||
      noLinkTitleWithChildren ||
      currentPageTitleWithChildren
    ) {
      if (currentPageTitleWithChildren) {
        event.preventDefault()
      }
      this.isExpanded = !this.isExpanded
      return
    }

    // Disable Ajax
    if (this.book && this.book.hasAttribute('data-is-no-ajax')) {
      return
    }

    // External links
    if (
      this.isExternal ||
      !this.link ||
      this.link.getAttribute('target') === '_blank'
    ) {
      this.removeExpandedAttribute()

      return
    }

    if (this.link && event.target.matches('.Chapter-title, .Chapter-title *')) {
      event.preventDefault()

      this.getContent()
        .then(() => {
          this.removeExpandedAttribute()
        })
        .catch((error) => {
          console.error(error.message)
        })
    }
  }

  onActive(event) {
    this.isCurrent = event.target === this
    if (event.target === this || this.contains(event.target)) {
      this.isExpanded = true
    }

    if (
      [...this.childChapters].includes(event.target) &&
      event.target.anchorTarget
    ) {
      this.isCurrent = true
    }

    this.setAnchorChaptersBehaviour()
  }

  onPopstate(event) {
    if (this.url && this.url.href === location.href) {
      event.stopPropagation()
      const isPoppedEvent = true
      this.getContent(isPoppedEvent)
    }
  }

  onScroll() {
    this.setAnchorChaptersState()
  }

  emit(event, detail = {}) {
    const customEvent = new CustomEvent(event, {
      bubbles: true,
      detail: detail,
    })
    this.dispatchEvent(customEvent)
  }

  disconnectedCallback() {
    document.removeEventListener('click', this)
    this.removeEventListener('click', this)
    document.removeEventListener(events.active, this)
    window.removeEventListener('popstate', this)
    window.removeEventListener('scroll', this, true)
    window.removeEventListener('resize', this)
  }
}
