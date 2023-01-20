export const DEFAULT_BREAKPOINT_OPTIONS = {
  mobile: ['mq-xs', 'mq-sm', 'mq-md'],
  desktop: ['mq-lg', 'mq-hk', 'mq-xl'],
}

export function mediaQuery() {
  const mqValue =
    window
      .getComputedStyle(document.querySelector('body'), '::before')
      .getPropertyValue('content') || false

  if (mqValue) {
    return mqValue.replace(/["']/g, '')
  } else {
    return false
  }
}
