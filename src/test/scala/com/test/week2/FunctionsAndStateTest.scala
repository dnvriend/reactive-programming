package com.test.week2

import com.test.TestSpec
import com.test.week2.state.BankAccount

class FunctionsAndStateTest extends TestSpec {

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
