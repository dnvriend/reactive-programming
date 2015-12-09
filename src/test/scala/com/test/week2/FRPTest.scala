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

package com.test.week2

import com.test.TestSpec
import com.test.week2.frp.{ BankAccount, Signal }

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
