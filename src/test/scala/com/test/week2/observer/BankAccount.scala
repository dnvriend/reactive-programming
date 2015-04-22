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
    if(0 < amount && amount <= balance) {
      balance -= amount
      publish()
    } else {
      throw new Error("insufficient funds")
    }

  def currentBalance = balance
}
