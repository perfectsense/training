import Refreshable from '../../core/util/Refreshable.js'
import UncachePromise from '../../core/util/Uncache.js'

export default class AuthenticateNavigation extends Refreshable {

  when () {
    return UncachePromise
  }
}
