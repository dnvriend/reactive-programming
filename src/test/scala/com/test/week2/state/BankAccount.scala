package com.test.week2.state

class BankAccount {
  private var balance = 0
  def deposit(amount: Int): Unit = {
    balance = if (amount > 0) balance + amount else balance
  }

  def withdraw(amount: Int): Int =
    if(0 < amount && amount <= balance) {
      balance -= amount
      balance
    } else {
      throw new Error("insufficient funds")
    }
}
