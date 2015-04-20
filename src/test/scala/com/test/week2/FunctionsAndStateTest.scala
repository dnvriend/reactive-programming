package com.test.week2

import akka.event.Logging
import com.test.TestSpec

class FunctionsAndStateTest extends TestSpec {
   val log = Logging(system, this.getClass)
   class BankAccount {
     private var balance = 0
     def deposit(amount: Int): Unit = {
       balance = if (amount > 0) balance + amount else balance
       log.info("Depositing: {}, balance is: {}", amount, balance)
     }

     def withdraw(amount: Int): Int =
      if(0 < amount && amount <= balance) {
        balance -= amount
        log.info("Withdrawn: {}, balance is: {}", amount, balance)
        balance
      } else {
        log.error("Withdrawn: {}, overdrawn!, balance would be: {}", amount, balance - amount)
        throw new Error("insufficient funds")
      }
   }

  "BankAccount" should "be able to be overdrawn" in {
    // account is a stateful object, because the effect of the withdraw method
    // depends on the history of the object. The results differ based upon the
    // value of the balance variable.
    val acct = new BankAccount
    acct.deposit(50)
    acct.withdraw(20) shouldBe 30
    acct.withdraw(20) shouldBe 10
    intercept[Error] {
      acct.withdraw(15)
    }
  }
}
