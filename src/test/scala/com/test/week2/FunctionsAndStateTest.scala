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
