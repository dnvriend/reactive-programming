package com.test.week2.observer

/**
 * Observes a list of bankaccounts and is always
 * up to date with the total balance of all the
 * bankaccounts (sum of all the balances)
 */
class Consolidator(observed: List[BankAccount]) extends Subscriber {
  // subscribe to each of the bankaccounts
  // The 'trick' is that the list of bankaccounts *are* the publishers,
  // so on each of the bankaccount, consolidator subscribes itself (this)
  observed.foreach (bankaccount => bankaccount.subscribe(this))

  private var total: Int = _
  compute()

  private def compute() =
    total = observed.map(_.currentBalance).sum

  /**
   * When the publisher calls the 'handler' method, the consolidator will
   * recompute the total balance
   * @param pub
   */
  def handler(pub: Publisher) = compute()

  def totalBalance = total
}
