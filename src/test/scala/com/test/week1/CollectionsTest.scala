package com.test.week1

import com.test.TestSpec

class CollectionsTest extends TestSpec {

  /* scala has the following collection hierarchy

                     Traversable
                         |
                         |
                      Iterable
                         |
      +------------------+--------------------+
     Map                Set                  Seq
      |                  |                    |
      |             +----+----+         +-----+------+
    Sorted Map  SortedSet   BitSet   IndexedSeq   LineairSeq
   */

  "IndexedSeq" should "have map, flatMap and Filter" in {
    // IndexedSeq collections are optimized for contant-time or near constant-time access
    // and length computations. It provides random access and updates in constant time, as
    // well as very fast append and prepend.
    val xs: Vector[Int] = Vector(1, 2, 3, 4, 5)
    xs.head shouldBe 1
    xs.last shouldBe 5
    xs.drop(1) shouldBe Vector(2, 3, 4, 5)
    xs.drop(1).dropRight(1) shouldBe Vector(2, 3, 4)
    xs.filter(_ % 2 == 0) shouldBe Vector(2, 4)
    xs.filterNot(_ % 2 == 0) shouldBe Vector(1, 3, 5)
    xs.map(_ * 2).find(_ == 5) shouldBe None
    xs.map(_ * 2).find(_ == 10) shouldBe Some(10)

    xs.sum shouldBe 1 + 2 + 3 + 4 + 5

    val xx = for (x <- xs) yield x * 2
    xx shouldBe Vector(2, 4, 6, 8, 10)

    // the Vector can also be created like this
    val xy = for(x <- 1 to 5) yield x * 2
    xy shouldBe Vector(2, 4, 6, 8, 10)

    val xz = for(x <- 1 to 5 if x > 2) yield x
    xz shouldBe Vector(3, 4, 5)

    xs.foldLeft(1) { _ * _ } shouldBe 120
  }

  "Arrays" should "have map, flatMap and Filter" in {
    // an Array is a *mutable* indexed collection of values, and is 100%
    // compatible with Java's Array, the T[]
    val xs: Array[Int] = Array(1, 2, 3, 4, 5)
    xs.head shouldBe 1
    xs.last shouldBe 5
    xs.drop(1) shouldBe Array(2, 3, 4, 5)
    xs.drop(1).dropRight(1) shouldBe Array(2, 3, 4)
    xs.filter(_ % 2 == 0) shouldBe Array(2, 4)
    xs.filterNot(_ % 2 == 0) shouldBe Array(1, 3, 5)
    xs.map(_ * 2).find(_ == 5) shouldBe None
    xs.map(_ * 2).find(_ == 10) shouldBe Some(10)

    xs.sum shouldBe 1 + 2 + 3 + 4 + 5

    val xx = for (x <- xs) yield x * 2
    xx shouldBe Array(2, 4, 6, 8, 10)

    val xz = for(x <- 1 to 5 if x > 2) yield x
    xz shouldBe Array(3, 4, 5)

    xs.foldLeft(1) { _ * _ } shouldBe 120
  }

  "List" should "have map, flatMap and Filter" in {
    // Lists are optimized for Sequential Scan (it is a LineairSeq)
    val xs: List[Int] = List(1, 2, 3, 4, 5)
    xs.head shouldBe 1
    xs.last shouldBe 5
    xs.drop(1) shouldBe List(2, 3, 4, 5)
    xs.drop(1).dropRight(1) shouldBe List(2, 3, 4)
    xs.filter(_ % 2 == 0) shouldBe List(2, 4)
    xs.filterNot(_ % 2 == 0) shouldBe List(1, 3, 5)
    xs.map(_ * 2).find(_ == 5) shouldBe None
    xs.map(_ * 2).find(_ == 10) shouldBe Some(10)

    xs.sum shouldBe 1 + 2 + 3 + 4 + 5

    val xx = for (x <- xs) yield x * 2
    xx shouldBe List(2, 4, 6, 8, 10)

    val xz = for(x <- 1 to 5 if x > 2) yield x
    xz shouldBe List(3, 4, 5)

    xs.foldLeft(1) { _ * _ } shouldBe 120
  }
}
