/* eslint-disable no-undef */

export class InfiniteScroll {
  constructor (container, settings) {
    this.container = container
    this.settings = settings

    const loadMoreElement = document.querySelector(`${this.settings.loadMoreSelector}`)

    if (!loadMoreElement) {
      return
    }

    // have an issue with express where on the last page, it returns the previous page
    // creating an infinite load more loop. Adding this until the express fix is in
    if (loadMoreElement.innerHTML.indexOf('Previous') > -1) {
      let loadMoreParent = loadMoreElement.parentNode
      loadMoreParent.removeChild(loadMoreElement)
      return
    }

    // Fallback on manual mode?
    if (!(`IntersectionObserver` in window)) {
      // reveal the loadmore button
      loadMoreElement.classList.add(`visible`)
      loadMoreElement.addEventListener(`click`, (evt) => {
        evt.preventDefault()
        this.getNext(loadMoreElement).then(next => {
          if (next) {
            next.classList.add(`visible`)
            // todo: rebind
          }
        })
      })
      return // exit early
    }

    // If we've gotten this far, we're going to use an observer.
    const observer = new IntersectionObserver(changes => {
      changes.forEach(change => {
        if (change.isIntersecting) {
          // stop observing since we've reached the loadmore
          observer.unobserve(change.target)

          this.getNext(change.target).then(next => {
            if (next) observer.observe(next)
          })
        }
      })
    }, {
      threshold: 1.0
    })

    observer.observe(loadMoreElement)
  }

  getNext (current) {
    let insert = this.settings.insert || `beforeend`

    return this.request(this.url(current)).then(text => {
      const range = document.createRange()
      const frag = range.createContextualFragment(text)
      const newItems = frag.querySelectorAll(this.settings.itemSelector)
      const newLoadMore = frag.querySelector(this.settings.loadMoreSelector)

      current.remove()

      newItems.forEach(item => {
        this.container.insertAdjacentElement(`${insert}`, item)
      })

      if (newLoadMore) {
        // have an issue with express where on the last page, it returns the previous page
        // creating an infinite load more loop. Adding this until the express fix is in
        if (newLoadMore.innerHTML.indexOf('Previous') > -1) {
          return false
        }

        this.container.appendChild(newLoadMore)
      }

      return this.container.querySelector(`${this.settings.loadMoreSelector}`)
    })
  }

  request (url) {
    return window.fetch(url, {
      credentials: 'same-origin'
    })
    .then(response => {
      return response.text()
    })
  }

  url (element) {
    return element.getAttribute(`href`)
  }
}
