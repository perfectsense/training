// For an explanation of this file's purpose,
// see https://www.a11yproject.com/posts/2013-01-25-never-remove-css-outlines/

export class Unfocus {
  constructor() {
    let styleText =
      '::-moz-focus-inner{border:0 !important;}:focus{outline: none !important;'
    const unfocusStyle = document.createElement('STYLE')

    window.unfocus = function () {
      document.getElementsByTagName('HEAD')[0].appendChild(unfocusStyle)

      document.addEventListener('mousedown', function () {
        unfocusStyle.innerHTML = styleText + '}'
      })
      document.addEventListener('keydown', function () {
        unfocusStyle.innerHTML = ''
      })
    }

    window.unfocus.style = function (style) {
      styleText += style
    }

    window.unfocus()
  }
}
