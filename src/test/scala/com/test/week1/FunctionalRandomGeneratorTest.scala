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

package com.test.week1

import com.test.{ Random, TestSpec }
import org.scalatest.exceptions.TestFailedException
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class FunctionalRandomGeneratorTest extends TestSpec with GeneratorDrivenPropertyChecks {

  /**
   * Simple generators, only give one result each time
   */
  object Simple {
    trait Generator[+T] {
      def generate: T
    }

    def integers = new Generator[Int] {
      override def generate: Int = Random().nextInt()
    }

    def booleans = new Generator[Boolean] {
      override def generate: Boolean = integers.generate > 0
    }

    def pairs = new Generator[(Int, Int)] {
      override def generate: (Int, Int) = (integers.generate, integers.generate)
    }
  }

  "Random" should "create random numbers" in {
    Random().nextInt() shouldBe a[Integer]
  }

  "SimpleGenerator" should "generate a random number" in {
    Simple.integers.generate shouldBe a[Integer]
  }

  it should "generate a boolean" in {
    Simple.booleans.generate shouldBe a[java.lang.Boolean]
  }

  it should "generate a pair of integer" in {
    Simple.pairs.generate mustBe {
      case (_: Int, _: Int) ⇒
    }
  }

  /**
   * A bit more advanced generator, has the map and flatMap methods
   */
  object Advanced {
    trait Generator[+T] {
      self ⇒

      def generate: T

      def map[S](f: T ⇒ S): Generator[S] = new Generator[S] {
        def generate: S = f(self.generate)
      }

      def flatMap[S](f: T ⇒ Generator[S]): Generator[S] = new Generator[S] {
        override def generate: S = f(self.generate).generate
      }
    }

    /**
     * Generic Generators
     */
    def single[T](x: T) = new Generator[T] {
      override def generate: T = x
    }

    def choose(lo: Int, hi: Int): Generator[Int] =
      for (x ← integers) yield lo + x % (hi - lo)

    // the T* is the varargs syntax you know of Java
    def oneOf[T](xs: T*): Generator[T] =
      for (idx ← choose(0, xs.length)) yield xs(idx)

    def integers: Generator[Int] = new Generator[Int] {
      override def generate: Int = Random().nextInt()
    }

    def booleans: Generator[Boolean] = new Generator[Boolean] {
      override def generate: Boolean = integers.generate > 0
    }

    def pairs[T, U](t: Generator[T], u: Generator[U]) = new Generator[(T, U)] {
      override def generate: (T, U) = (t.generate, u.generate)
    }

    def lists: Generator[List[Int]] = for {
      isEmpty ← booleans
      list ← if (isEmpty) emptyList else nonEmptyList
    } yield list

    def emptyList: Generator[List[Int]] = single(List.empty[Int])

    def nonEmptyList: Generator[List[Int]] = for {
      head ← integers
      tail ← lists
    } yield head :: tail

    def leafs: Generator[Leaf] = for {
      x ← integers
    } yield Leaf(x)

    def nodes: Generator[Node] = for {
      l ← trees
      r ← trees
    } yield Node(l, r)

    // it generates either a leaf or a node
    def trees: Generator[Tree] = for {
      isLeaf ← booleans
      tree ← if (isLeaf) leafs else nodes
    } yield tree
  }

  "Advanced" should "generate a random number" in {
    Advanced.integers.generate shouldBe a[Integer]
  }

  it should "generate a boolean" in {
    Advanced.booleans.generate shouldBe a[java.lang.Boolean]
  }

  it should "generate a pair of integer" in {
    Advanced.pairs(Advanced.integers, Advanced.integers).generate mustBe {
      case (_: Int, _: Int) ⇒
    }
  }

  it should "generate a list" in {
    Advanced.lists.generate mustBe {
      case _: List[Int] ⇒
    }
  }

  // definition of a binary tree, a tree can be either a leaf or a node
  // but when its a node, then it contains to its left side a tree and
  // to its right a tree, which either can be either... etc
  trait Tree
  case class Node(left: Tree, right: Tree) extends Tree
  case class Leaf(x: Int) extends Tree

  it should "generate trees" in {
    Advanced.trees.generate shouldBe a[Tree]
  }

  def test[T](g: Advanced.Generator[T], numTimes: Int = 100)(test: T ⇒ Boolean): Unit = {
    for (i ← 0 until numTimes) {
      val value: T = g.generate
      assert(test(value), s"test failed for $value")
    }
    println(s"passed $numTimes tests")
  }

  "testing list adding two list must always be greater" should "always fail" in {
    intercept[TestFailedException] {
      test(Advanced.pairs(Advanced.lists, Advanced.lists)) {
        case (xs, xy) ⇒ (xs ++ xy).length > xs.length
      }
    }
  }

  it should "aways fail using forAll" in {
    intercept[TestFailedException] {
      forAll { (l1: List[Int], l2: List[Int]) ⇒
        l1.size + l2.size should not be (l1 ++ l2).size
      }
    }
  }

  it should "always succeed forAll" in {
    forAll { (l1: List[Int], l2: List[Int]) ⇒
      l1.size + l2.size shouldBe (l1 ++ l2).size
    }
  }
}
