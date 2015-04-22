package com.test.week2

import com.test.TestSpec
import com.test.week2.observer.{Consolidator, BankAccount}

class ObserverTest extends TestSpec {

  /**
   * The Observer Pattern is widely used when views need to react to changes
   * in a model.
   *
   *  Variants of it are also called:
   *   - publish / subscribe
   *   - model / view / controller
   */

  "Consolidator" should "compute the total balance" in {
    val a = new BankAccount
    val b = new BankAccount
    val c = new Consolidator(List(a, b))
    c.totalBalance shouldBe 0

    a deposit 20
    c.totalBalance shouldBe 20
    b deposit 30
    c.totalBalance shouldBe 50
  }
}
