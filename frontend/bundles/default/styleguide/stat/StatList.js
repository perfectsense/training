export default class StatList extends HTMLElement {
  connectedCallback() {
    const counters = this.querySelectorAll('[data-counter]')

    const counterObserver = new window.IntersectionObserver(function (entries) {
      entries.forEach(function (entry) {
        if (entry.isIntersecting) {
          const item = entry.target

          // eslint-disable-next-line no-unused-vars
          const updateCount = () => {
            const target = item.getAttribute('data-target')
            const count = parseInt(item.innerText)
            const inc = target / 100

            if (count < target) {
              item.innerText = Math.ceil(count + inc)
              setTimeout(updateCount, 20)
            } else {
              item.innerText = target
            }
          }

          updateCount()

          counterObserver.unobserve(item)
        }
      })
    })

    counters.forEach((counter) => {
      counterObserver.observe(counter)
    })
  }
}
