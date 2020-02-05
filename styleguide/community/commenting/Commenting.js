export const COMMENTING_SELECTOR = '.Commenting'
const COMMENTING_FORM_SELECTOR = COMMENTING_SELECTOR + '-form > form'
const COMMENTING_COMMENTS_SELECTOR = COMMENTING_SELECTOR + '-comments'
const COMMENTING_COMMENT_TEMPLATE_SELECTOR = COMMENTING_SELECTOR + '-commentTemplate'
const COMMENTING_PAGINATION_NEXT_SELECTOR = COMMENTING_SELECTOR + '-pagination-next'
const COMMENTING_REPLY_FORM_TEMPLATE = COMMENTING_SELECTOR + '-replyFormTemplate'

const COMMENT_CLASS = 'Comment'
export const COMMENT_SELECTOR = '.' + COMMENT_CLASS
const COMMENT_REPLY_BUTTON_CLASS = COMMENT_CLASS + '-replyButton'

const fetch = window.fetch
const FormData = window.FormData

export default class Commenting {

  constructor (element) {
    this.element = element
    this.form = element.querySelector(COMMENTING_FORM_SELECTOR)
    this.results = element.querySelector(COMMENTING_COMMENTS_SELECTOR)
    this.commentTemplate = element.querySelector(COMMENTING_COMMENT_TEMPLATE_SELECTOR)
    this.paginationNextLink = element.querySelector(COMMENTING_PAGINATION_NEXT_SELECTOR)
    this.replyFormTemplate = element.querySelector(COMMENTING_REPLY_FORM_TEMPLATE)
    this.bindEvents()
  }

  bindEvents () {
    if (this.paginationNextLink) {
      this.paginationNextLink.addEventListener('click', (event) => this.onPaginate(event))
    }

    this.element.addEventListener('submit', (event) => this.onFormSubmit(event))
    this.element.addEventListener('click', (event) => {
      const target = event.target
      if (target.classList.contains(COMMENT_REPLY_BUTTON_CLASS)) {
        this.onReplyButtonClick(event)
      }
    })
  }

  onFormSubmit (event) {
    event.preventDefault()
    const form = event.target
    const formData = new FormData(form)
    this.renderPlaceholderComment(formData)

    Array.prototype.filter
          .call(form.querySelectorAll('input, textarea'), (input) => {
            return input.getAttribute('type') !== 'hidden'
          }).forEach((input) => {
            input.value = ''
          })

    fetch(form.getAttribute('action'), {
      method: 'POST',
      body: formData,
      credentials: 'include'
    }).then((response) => {
      if (response.status !== 200) {
        throw new Error(response.statusText)
      }

      return response.text()
    }).then((newCommentHtml) => {
      const temporaryTemplate = document.createElement('template')
      temporaryTemplate.innerHTML = newCommentHtml

      const newCommentNode = temporaryTemplate.content.firstElementChild
      const placeholderNode = this.results.querySelector('[data-placeholder]')
      this.results.replaceChild(newCommentNode, placeholderNode)
    }).catch((error) => {
      console.log(error)
    })
  }

  onPaginate (event) {
    event.preventDefault()

    const limit = Number(this.paginationNextLink.getAttribute('data-limit'))
    let offset = Number(this.paginationNextLink.getAttribute('data-offset'))
    const params = {
      limit: limit,
      offset: offset,
      targetId: this.paginationNextLink.getAttribute('data-target-id')
    }

    let url = this.paginationNextLink.getAttribute('href') + '?' +
       Object.keys(params).map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
         .join('&')

    fetch(url, {
      method: 'GET',
      credentials: 'include'
    }).then((response) => {
      if (response.status !== 200) {
        throw new Error(response.statusText)
      }

      return response.text()
    }).then((comments) => {
      this.results.insertAdjacentHTML('beforeend', comments)
      offset += limit
      this.paginationNextLink.setAttribute('data-offset', offset)

      const commentHTML = document.createElement('div')
      commentHTML.innerHTML = comments
      if (commentHTML.querySelectorAll(COMMENT_SELECTOR).length < limit) {
        this.paginationNextLink.style = 'display: none;'
      }
    }).catch((error) => {
      console.log(error)
    })
  }

  onReplyButtonClick (event) {
    event.preventDefault()
    const button = event.target

    const targetComment = [...this.results.querySelectorAll(COMMENT_SELECTOR)].filter(element => {
      return element.contains(button)
    })[0]

    targetComment.setAttribute('data-reply-active', true)
    let replyForm = targetComment.querySelector('form')
    if (!replyForm) {
      replyForm = document.importNode(this.replyFormTemplate.content, true)
      replyForm.querySelector('input[name="parent"]').setAttribute('value', targetComment.getAttribute('id'))
    }

    targetComment.appendChild(replyForm)
  }

  renderPlaceholderComment (formData) {
    const commentFragment = document.importNode(this.commentTemplate.content, true)
    commentFragment.querySelector('.Comment-body').textContent = formData.get('body')
    const commentElement = commentFragment.firstElementChild
    commentElement.setAttribute('data-placeholder', 'true')

    const replyParentId = formData.get('parent')
    if (replyParentId) {
      const parent = document.getElementById(replyParentId)
      parent.insertAdjacentElement('afterend', commentElement)
    } else {
      this.results.insertAdjacentElement('beforeend', commentElement)
      this.results.scrollIntoView({
        behavior: 'smooth', block: 'end'
      })
    }
  }
}
