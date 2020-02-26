export function debounce (interval, debouncedFunction, immediate) {
  let timeout
  return function () {
    let context = this
    let args = arguments
    let later = () => {
      timeout = null
      if (!immediate) debouncedFunction.apply(context, args)
    }
    let callNow = immediate && !timeout
    clearTimeout(timeout)
    timeout = setTimeout(later, interval)
    if (callNow) debouncedFunction.apply(context, args)
  }
}
