package com.test.week1

import com.test.TestSpec

class PatternMatchTest extends TestSpec {

  // Scala has a built-in general pattern matching mechanism. It allows to match
  // on any sort of data with a first-match policy.
  // most importantly, the match 'expression' returns a value, in this case it is
  // a function from (Int => String) and thus returns a String
  def matchNumber(x: Int): String = x match {
    case 1 => "one"
    case 2 => "two"
    case _ => "many"
  }

  // this is a function from (Any => Any),
  // so it accepts Any, and returns Any
  def matchAny(x: Any): Any = x match {
    case 1 => "one"
    case "two" => 2
    case y: Int => "scala.Int"
    case couldBeAnyting => couldBeAnyting
  }

  // it is also possible to set guards
  // on the matches
  def bigger(x: Any): Any = x match {
    case i: Int if i < 0 => i - 1
    case i: Int => i + 1
    case d: Double if d < 0.0 => d - 0.1
    case d: Double => d + 0.1
    case text: String => text + "s"
  }

  "matchNumber" should "match numbers" in {
    matchNumber(1) shouldBe "one"
    matchNumber(2) shouldBe "two"
    matchNumber(3) shouldBe "many"
  }

  "matchAny" should "match literally anything" in {
    matchAny(1) shouldBe "one"
    matchAny("two") shouldBe 2
    matchAny(2) shouldBe "scala.Int"
    matchAny(List(1, 2, 3)) shouldBe List(1, 2, 3)
  }

  "bigger" should "match with guard" in {
    bigger(-1) shouldBe -2
    bigger(1) shouldBe 2
    bigger(-1.0) shouldBe -1.1
    bigger(1.0) shouldBe 1.1
    bigger("text") shouldBe "texts"
  }
}
