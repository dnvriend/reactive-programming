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

class LoopTest extends TestSpec {

  "Scala" should "support the while loop" in {
    // please don't do this, scala has support for declarative style
    var i = 0
    while (i < 10) {
      i += 1
    }
    i shouldBe 10
  }

  it should "support declarative style 1" in {
    (1 to 10).map(_ ⇒ 1).sum shouldBe 10
  }

  "WHILE" should "be implemented using a function" in {
    // 'condition' is a 'pass-by-name' parameter (note the '=>'), which means that every time
    // 'condition' or 'command' is called, the function will be 're-evaluated'
    // if it was 'pass-by-value', well maybe the loop would run forever... if the condition is true
    def WHILE(condition: ⇒ Boolean)(command: ⇒ Unit): Unit =
      if (condition) {
        command
        WHILE(condition)(command)
      } else ()

    var i = 0
    WHILE(i < 10) {
      i += 1
    }
    i shouldBe 10
  }

  "REPEAT" should "be implemented using a function" in {
    def REPEAT(command: ⇒ Unit)(condition: ⇒ Boolean): Unit = {
      command
      if (condition) () else REPEAT(command)(condition)
    }

    var i = 0
    REPEAT {
      i += 1
    }(i == 10)
    i shouldBe 10
  }

  "REPEAT UNTIL" should "be implemented using named class" in {
    class Repeater(command: ⇒ Unit) {
      def UNTIL(condition: ⇒ Boolean): Unit = {
        command
        if (condition) () else UNTIL(condition)
      }
    }

    def REPEAT(command: ⇒ Unit) = new Repeater(command)
    var i = 0
    REPEAT {
      i += 1
    } UNTIL (i == 10)

    i shouldBe 10
  }

  it should "be implemented using AnyRef type" in {
    def REPEAT(command: ⇒ Unit) = new AnyRef {
      def UNTIL(condition: ⇒ Boolean): Unit = {
        command
        if (condition) () else UNTIL(condition)
      }
    }

    var i = 0
    REPEAT {
      i += 1
    } UNTIL (i == 10)

    i shouldBe 10
  }

  it should "be implemented as an anonymous class" in {
    def REPEAT(command: ⇒ Unit) = new {
      def UNTIL(condition: ⇒ Boolean): Unit = {
        command
        if (condition) () else UNTIL(condition)
      }
    }

    var i = 0
    REPEAT {
      i += 1
    } UNTIL (i == 10)

    i shouldBe 10
  }

  it should "be implemented using Eta expansion" in {
    def REPEAT(command: ⇒ Unit)(condition: () ⇒ Boolean): Unit = {
      command
      if (condition()) () else REPEAT(command)(condition)
    }

    def UNTIL(condition: ⇒ Boolean): () ⇒ Boolean = () ⇒ condition

    // extra braces due to currying
    var i = 0
    REPEAT {
      i += 1
    }(UNTIL(i == 10))

    i shouldBe 10
  }

  "Scala" should "support the do-while loop" in {
    // please don't do this, scala has support for declarative style
    var i = 0
    do {
      i += 1
    } while (i < 10)
    i shouldBe 10
  }

  it should "support declarative style 2" in {
    (1 to 10).map(_ ⇒ 1).sum shouldBe 10
  }

  "Scala" should "support a for loop with generators" in {
    // please don't do this, scala has support for declarative style
    var j = 0
    for (i ← 1 to 3) {
      j += i
    }
    j shouldBe 6
  }

  it should "support declarative style 3" in {
    (1 to 3).sum shouldBe 6
  }

  "Nested loops" should "be evaluated" in {
    val xs = for (i ← 1 to 3; j ← "abc") yield s"$i$j"
    xs shouldBe List("1a", "1b", "1c", "2a", "2b", "2c", "3a", "3b", "3c")
  }
}
