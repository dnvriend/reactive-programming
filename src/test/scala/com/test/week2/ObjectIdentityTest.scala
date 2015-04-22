package com.test.week2

import com.test.TestSpec
import com.test.week2.state.BankAccount

class ObjectIdentityTest extends TestSpec {

  //
  // operational equivalence
  //

  /**
   * The function that isolates the test to be performed
   * on both objects
   */
  def f(x: BankAccount, y: BankAccount): Unit = {
    x.deposit(30)
    y.withdraw(20)
  }

  "Two object" should "be the same when their behavior is the same" in {
    val x = new BankAccount
    val y = x
    // the test did not fail, x and y behave the same so they
    // are the same
    f(x, y)
  }

  it should "test whether these two object are the same" in {
    val x = new BankAccount
    val y = new BankAccount
    // it should fail, they are not the same
    intercept[Error] {
      f(x, y)
    }
  }
}
