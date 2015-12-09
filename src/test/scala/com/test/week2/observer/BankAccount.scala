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

class BankAccount extends Publisher {
  private var balance = 0

  /**
   * When the state changes, notify all subscribers
   * Bankaccount is called a publisher because it
   * publishes the change 'event', which is implicit
   * in this case. The fact that it calls the publish()
   * method is an event in itself
   * @param amount
   */
  def deposit(amount: Int): Unit = {
    balance = if (amount > 0) balance + amount else balance
    publish()
  }

  /**
   * Also notify subscribers
   * @param amount
   */
  def withdraw(amount: Int): Unit =
    if (0 < amount && amount <= balance) {
      balance -= amount
      publish()
    } else {
      throw new Error("insufficient funds")
    }

  def currentBalance = balance
}
