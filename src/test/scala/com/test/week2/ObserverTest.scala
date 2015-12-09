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
import com.test.week2.observer.{ Consolidator, BankAccount }

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
