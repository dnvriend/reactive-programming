package com.test.week4

import com.test.TestSpec

import scala.collection.immutable.TreeSet

class IterableTest extends TestSpec {

  "List" should "be an Iterable" in {
    List(1, 2, 3) shouldBe an [Iterable[_]]
  }

  it should "have next elements" in {
    List(1, 2, 3).iterator should have ('hasNext(true))
  }

  it should "contain elements" in {
    List(Seq(0, 1), Seq(1, 2)) should contain inOrder (Seq(0, 1), Seq(1, 2))
  }

  "Vector" should "be an Iterable" in {
    Vector(1, 2, 3) shouldBe an [Iterable[_]]
  }

  it should "have next elements" in {
    Vector(1, 2, 3).iterator should have ('hasNext(true))
  }

  "Set" should "be an Iterable" in {
    Set(1, 2, 3) shouldBe an [Iterable[_]]
  }

  it should "have next elements" in {
    Set(1, 2, 3).iterator should have ('hasNext(true))
  }

  "Tree" should "be an Iterable" in {
    TreeSet(1, 2, 3) shouldBe an [Iterable[_]]
  }

  it should "have next elements" in {
    TreeSet(1, 2, 3).iterator should have ('hasNext(true))
  }

  "Map" should "be an Iterable" in {
    Map(1 -> "1", 2 -> "2", 3 -> "3") shouldBe an [Iterable[_]]
  }

  it should "have next elements" in {
    Map(1 -> "1", 2 -> "2", 3 -> "3").iterator should have ('hasNext(true))
  }
}
