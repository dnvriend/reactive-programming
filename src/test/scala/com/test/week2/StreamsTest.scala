package com.test.week2

import com.test.TestSpec

class StreamsTest extends TestSpec {

  /**
   * A 'normal' collection, like a List is called a 'strict' collection. A strict collection
   * evaluates all elements eagerly, even when it is not needed.
   */

  "A List" should "evaluate all entries on head" in {
    var numTimes = 0
    val x = List(1, 2, 3).map { i =>
      numTimes += 1
      i * 2
    }.head // only get the head, so one evaluation would be enough, but ...
    x shouldBe 2
    numTimes shouldBe 3 // the collection has been evaluated 3 times
  }

  it should "still evaluate all entries on take 2" in {
    var numTimes = 0
    val x = List(1, 2, 3).map { i =>
      numTimes += 1
      i * 2
    }.take(2) // only get the first 2 elements, so two evaluations would be enough, but ...
    x shouldBe List(2, 4)
    numTimes shouldBe 3 // the collection has been evaluated 3 times
  }

  it should "be created using '::'" in {
    // lists can be created using "::"
    val xs = 1 :: 2 :: 3 :: Nil
    xs.head shouldBe 1
  }

  it should "filter the list" in {
    var num = 0
    val xs = List(3, 1, 2, 1, 4)
    val x = xs.filter { e =>
      num += 1
      e < 2
    }.take(2)
    x shouldBe List(1, 1)
    num shouldBe 5 // filter evaluates all elements, even if we only want 2
  }

  /**
   * Lazy collections (non-strict collections) are collections that will only evaluate the
   * necessary elements, depending on the operations that follow the collection.
   * You could also call them 'on-demand' collections.
   */

  "A Stream" should "not evaluate all entries on head" in {
    var numTimes = 0
    val x = Stream(1, 2, 3).map { i =>
      numTimes += 1
      i * 2
    }.head // only get the head, so one evaluation would be enough,
    x shouldBe 2
    numTimes shouldBe 1 // and it did only one evaluation
  }

  it should "not evaluate all entries on take 2" in {
    var numTimes = 0
    val x = Stream(1, 2, 3).map { i =>
      numTimes += 1
      i * 2
    }.take(2) // only get the first two elements, so two evaluations would be enough,
    x shouldBe List(2, 4)
    numTimes shouldBe 2 // and it did only two evaluations
  }

  it should "be created using '#::' and filter" in {
    var num = 0
    val xs: Stream[Int] = 3 #:: 1 #:: 2 #:: 1 #:: 4 #:: Stream.empty
    val x = xs.filter { e =>
      num += 1
      e < 2
    }.take(2)
    x shouldBe Stream(1, 1)
    num shouldBe 4 // it skips the first element (3), not < 2,
                   // then takes the second (1), skips the third (2)
                   // and takes the fourth (1), it will not evaluate (4)
  }
}
