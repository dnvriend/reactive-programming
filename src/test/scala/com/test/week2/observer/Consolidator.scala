/*
 * Copyright 2015 Dennis Vriend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
  observed.foreach(bankaccount ⇒ bankaccount.subscribe(this))

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
