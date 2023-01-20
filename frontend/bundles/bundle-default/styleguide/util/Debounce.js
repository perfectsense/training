export function debounce(interval, debouncedFunction, immediate) {
  let timeout
  return function () {
    const context = this
    const args = arguments
    const later = () => {
      timeout = null
      if (!immediate) debouncedFunction.apply(context, args)
    }
    const callNow = immediate && !timeout
    clearTimeout(timeout)
    timeout = setTimeout(later, interval)
    if (callNow) debouncedFunction.apply(context, args)
  }
}
