import plugins from '../../core/PluginRegistry.js'

const fetch = window.fetch

const SERVICE_URI_ATTRIBUTE = 'data-service-uri'
const UPDATE_INTERVAL_ATTRIBUTE = 'data-update-interval'

const PERCENT_CHANGE_INDICATOR_ATTRIBUTE = 'data-percent-direction'

/**
* Please see: http://documentation.q4websystems.com/home/stock-quote-feed/getfullstockquotelist for the payload structure
* example.
*/
const parseData = data => {
  const payload = data['GetFullStockQuoteListResult'].pop()

  return {
    price: payload['TradePrice'],
    percentChange: parseFloat(payload['PercChange']) || 0.0,
    time: payload['TradeDate'],
    dayHigh: payload['High'],
    dayLow: payload['Low'],
    volume: payload['Volume'],
    sharesTraded: payload['ShareTraded']
  }
}

class StockTicker {

  constructor (el) {
    this.el = el

    const className = this.el.className

    this.priceEl = this.el.querySelector(`.${className}-lastPrice`)
    this.percentLabelEl = this.el.querySelector(`.${className}-change`)
    this.percentEl = this.el.querySelector(`.${className}-percentageChange`)
    this.dateEl = this.el.querySelector(`.${className}-lastUpdated`)
    this.dayHighEl = this.el.querySelector(`.${className}-dayHigh`)
    this.dayLowEl = this.el.querySelector(`.${className}-dayLow`)
    this.volumeEl = this.el.querySelector(`.${className}-volume`)
    this.sharesTradedEl = this.el.querySelector(`.${className}-sharesTraded`)

    this.serviceUri = this.el.getAttribute(SERVICE_URI_ATTRIBUTE)
    this.updateInterval = parseInt(this.el.getAttribute(UPDATE_INTERVAL_ATTRIBUTE)) * 1000

    this.start()
  }

  fetchUpdate () {
    fetch(this.serviceUri, { mode: 'cors' })
      .then(response => response.json())
      .then(json => this.updateTickerUi(json))
      .catch(error => console.error(error))
  }

  updateTickerUi (stockData) {
    this.stock = parseData(stockData)

    this.priceEl.innerHTML = `$${this.stock.price}`
    this.percentEl.innerHTML = `${Math.abs(this.stock.percentChange)}%`
    this.dateEl.innerHTML = this.stock.time
    this.dayHighEl.innerHTML = this.stock.dayHigh
    this.dayLowEl.innerHTML = this.stock.dayLow
    this.volumeEl.innerHTML = this.stock.volume
    this.sharesTradedEl.innerHTML = this.stock.sharesTraded

    this.updateTickerIndicator(this.stock.percentChange)
  }

  updateTickerIndicator (percentChange) {
    if (percentChange === 0.0) {
      this.percentLabelEl.removeAttribute(PERCENT_CHANGE_INDICATOR_ATTRIBUTE)
    } else if (percentChange > 0.0) {
      this.percentLabelEl.setAttribute(PERCENT_CHANGE_INDICATOR_ATTRIBUTE, 'up')
    } else {
      this.percentLabelEl.setAttribute(PERCENT_CHANGE_INDICATOR_ATTRIBUTE, 'down')
    }
  }

  start () {
    if (!this.serviceUri || !this.updateInterval) {
      return
    }
    this.fetchUpdate()
    setInterval(() => this.fetchUpdate(), this.updateInterval)
  }
}

plugins.register(StockTicker, '.StockTicker')

export default {}
