package com.test.week2

import com.test.TestSpec

class LoopTest extends TestSpec {

  "Scala" should "support the while loop" in {
    // what am I even doing...
    var i = 0
    while(i < 10) {
      i += 1
    }
    i shouldBe 10
  }

  "WHILE" should "be implemented using a function" in {
    // 'condition' is a 'pass-by-name' parameter (note the '=>'), which means that every time
    // 'condition' or 'command' is called, the function will be 're-evaluated'
    // if it was 'pass-by-value', well maybe the loop would run forever... if the condition is true
    def WHILE(condition: => Boolean)(command: => Unit): Unit =
      if(condition) {
        command
        WHILE(condition)(command)
      } else ()

    var i = 0
    WHILE (i < 10) {
      i += 1
    }
    i shouldBe 10
  }

  "REPEAT" should "be implemented using a function" in {
    def REPEAT(command: => Unit)(condition: => Boolean): Unit = {
      command
      if(condition) () else REPEAT(command)(condition)
    }

    var i = 0
    REPEAT {
      i += 1
    } (i == 10)
    i shouldBe 10
  }

  "REPEAT UNTIL" should "be implemented using scala" in {
    class Repeater(command: => Unit) {
      def rep(condition: => Boolean): Unit = {
        command
        if(condition) () else rep(condition)
      }
      def UNTIL(condition: => Boolean) = rep(condition)
    }

    def REPEAT(command: => Unit) = new Repeater(command)
    var i = 0
    REPEAT {
      i += 1
    } UNTIL { i == 10}
    i shouldBe 10
  }

  "Scala" should "support the do-while loop" in {
    // no really, please stop..
    var i = 0
    do {
      i += 1
    } while(i < 10)
    i shouldBe 10
  }

  "Scala" should "support a for loop with generators" in {
    var j = 0
    for(i <- 1 to 3) {
      j += i
    }
    j shouldBe 6
  }

  it should "do a better job, oh man..." in {
    (1 to 3).sum shouldBe 6
  }

  "Nested loops" should "be evaluated" in {
    val xs = for (i <- 1 to 3; j <- "abc") yield s"$i$j"
    xs shouldBe List("1a", "1b", "1c", "2a", "2b", "2c", "3a", "3b", "3c")
  }
}
