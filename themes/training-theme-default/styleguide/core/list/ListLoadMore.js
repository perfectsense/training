const DEFAULTS = {
  selItems: '[data-list-loadmore-items]',
  selPagination: '[data-list-loadmore-pagination]',
  pageCacheRegex: /(page|p)=([0-9]*)$/ // regex where first item looksup key holding page number, and second item is the page number. This caches those items to ensure not recalling em
}

const PLUGIN_DATA_SEL = 'data-list-loadmore'

export class ListLoadMore extends window.HTMLElement {
  connectedCallback (settings) {
    this.el = this
    this.settings = Object.assign({}, DEFAULTS, settings)
    this.el.setAttribute(PLUGIN_DATA_SEL, '') // flag for interaction use
    this.pageHrefCache = [1]
    this.init()
  }

  init () {
    this.elItems = this.el.querySelector(this.settings.selItems)
    this.setBinds()
  }

  /**
   * @param {string} url - url which to fetch to
   * @param {function} customRender - custom rendering function, to bypass plugins default render, which is to append items to this.elItems
   * customRender call sends the following
   * - elItems - List items container
   * - elPagination - List Paginatin container
   * - elTempDiv - Full ajax response as a DOM node
   * - elIndex - Unique identifier for THIS list... Tells you that this is the nth list in document
   */
  loadMore (url, customRender) {
    let self = this
    let className = this.el.className
    let elsClassMatches = ListLoadMore.findClassNameMatches(document, className)
    let elIndex = Array.from(elsClassMatches).indexOf(this.el)

    this.pageHrefCache.push(this.getPageNum(url))
    this.el.setAttribute(PLUGIN_DATA_SEL, 'loading')

    window
      .fetch(url, { credentials: 'include' })
      .then(response => {
        this.el.setAttribute(PLUGIN_DATA_SEL, '')
        return response.text()
      })
      .then(text => {
        let elTempDiv = document.createElement('div')
        elTempDiv.innerHTML = text

        let elsClassMatches = ListLoadMore.findClassNameMatches(
          elTempDiv,
          className
        )
        let elItems = elsClassMatches[elIndex].querySelector(
          self.settings.selItems
        )
        let elPagination = elsClassMatches[elIndex].querySelector(
          self.settings.selPagination
        )

        if (typeof customRender === 'function') {
          customRender(elItems, elPagination, elsClassMatches[elIndex])
        } else {
          this.render(elItems, elPagination, self.elItems, self.elPagination)
        }
      })
      .catch(function (err) {
        console.log(err)
      })
  }

  next (customRender) {
    if (this.elPagination) {
      let elItem = this.elPagination.querySelector('a')
      if (elItem) {
        this.loadMore(elItem.href, customRender)
      }
    }
  }

  setBinds () {
    if (this.elPagination) {
      this.elPagination.addEventListener('click', e => {
        let elTarget = e.target
        if (elTarget.nodeName.toUpperCase() === 'A') {
          e.preventDefault()
          this.loadMore(elTarget.href)
        }
      })
    }
  }

  getPageNum (url) {
    try {
      let regexResult = this.settings.pageCacheRegex.exec(url)
      return parseInt(regexResult[2])
    } catch (e) {
      console.info('ListLoadMore', `${url} not a new page number`)
      return 0
    }
  }

  updatePaginationDOM (ajaxPagination) {
    let elPaginationLinks = [...ajaxPagination.querySelectorAll(':scope > a')]

    elPaginationLinks.forEach(elLink => {
      if (this.pageHrefCache.indexOf(this.getPageNum(elLink.href)) >= 0) {
        // remove link if already used | in pageHrefCache
        elLink.parentNode.removeChild(elLink)
      }
    })
    this.elPagination.innerHTML = ajaxPagination.innerHTML
  }

  static findClassNameMatches (doc, className) {
    return doc.querySelectorAll(`[class=${className}]`)
  }

  get elPagination () {
    return this.el.querySelector(this.settings.selPagination)
  }

  render (ajaxItems, ajaxPagination) {
    if (ajaxItems) {
      this.elItems.insertAdjacentHTML('beforeend', ajaxItems.innerHTML)
    }

    if (ajaxPagination) {
      this.updatePaginationDOM(ajaxPagination)
    } else {
      this.elPagination.parentNode.removeChild(this.elPagination)
    }

    this.dispatchRendered()
  }

  dispatchRendered () {
    let customEvent = new window.CustomEvent('Ajax:Rendered', {
      bubbles: true
    })

    document.body.dispatchEvent(customEvent)
  }
}
