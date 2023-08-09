export class ModuleAnimations {
  constructor() {
    const animatedModules = document.querySelectorAll(
      '[data-animate-on-scroll]'
    )

    const animatedOptions = {
      threshold: [0, 0.25, 0.5, 0.75, 1],
    }

    animatedModules.forEach((item) => {
      item.setAttribute('data-not-scrolled', true)
    })

    animatedModules.forEach((image) => {
      // let previousY = 0
      // let previousRatio = 0

      const animatedModuleObserver = new window.IntersectionObserver(function (
        entries
      ) {
        entries.forEach(function (entry) {
          const animatedModule = entry.target

          if (entry.isIntersecting) {
            animatedModule.removeAttribute('data-not-scrolled')
            animatedModule.setAttribute('data-scrolled', true)
          }

          // saving this code in here in case we ever want to detect in and out of view and scrolling state

          // const currentY = entry.boundingClientRect.y
          // const currentRatio = entry.intersectionRatio
          // const isIntersecting = entry.isIntersecting

          // if (previousRatio === 0 && currentRatio > 0.8) {
          //   console.log(animatedModule.className + ' in view')
          // }

          // if (currentY < previousY) {
          //   // scrolling down
          //   if (isIntersecting) {
          //     if ((currentRatio < previousRatio) && (currentRatio > 0.3) && (currentRatio < 0.6)) {
          //       console.log(animatedModule.className + ' scrolling down - out of view')
          //     }

          //     if ((currentRatio > previousRatio) && (currentRatio > 0.3) && (currentRatio < 0.6)) {
          //       console.log(animatedModule.className + ' scrolling down - in view')
          //     }
          //   }
          // } else {
          //   // scrolling up
          //   if (isIntersecting) {
          //     if ((currentRatio > previousRatio) && (currentRatio > 0.3) && (currentRatio < 0.6)) {
          //       console.log(animatedModule.className + ' scrolling up - in view')
          //     }

          //     if ((currentRatio < previousRatio) && (currentRatio > 0.3) && (currentRatio < 0.6)) {
          //       console.log(animatedModule.className + ' scrolling up - out of view')
          //     }
          //   }
          // }

          // previousY = currentY
          // previousRatio = currentRatio
        })
      },
      animatedOptions)

      animatedModuleObserver.observe(image)
    })
  }
}
