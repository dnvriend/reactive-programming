package com.test.week2

import com.test.TestSpec
import com.test.week2.frp.{BankAccount, Signal}

class FRPTest extends TestSpec {
  def consolidated(accts: List[BankAccount]): Signal[Int] =
    Signal(accts.map(_.balance()).sum)

  "FRP" should "dynamically update signals" in {
    val a = new BankAccount
    val b = new BankAccount
    val c = consolidated(List(a, b))
    c() shouldBe 0
    a deposit 20
    c() shouldBe 20
    b deposit 30
    c() shouldBe 50
    val xchange = Signal(246.00) // a constant signal
    val inDollar = Signal(c() * xchange())
    inDollar() shouldBe 12300.0
    b withdraw 10
    inDollar() shouldBe 9840.0
  }
}
