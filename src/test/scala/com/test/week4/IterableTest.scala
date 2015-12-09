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

package com.test.week4

import com.test.TestSpec

import scala.collection.immutable.TreeSet

class IterableTest extends TestSpec {

  "List" should "be an Iterable" in {
    List(1, 2, 3) shouldBe an[Iterable[_]]
  }

  it should "have next elements" in {
    List(1, 2, 3).iterator should have('hasNext(true))
  }

  it should "contain elements" in {
    List(Seq(0, 1), Seq(1, 2)) should contain inOrder (Seq(0, 1), Seq(1, 2))
  }

  "Vector" should "be an Iterable" in {
    Vector(1, 2, 3) shouldBe an[Iterable[_]]
  }

  it should "have next elements" in {
    Vector(1, 2, 3).iterator should have('hasNext(true))
  }

  "Set" should "be an Iterable" in {
    Set(1, 2, 3) shouldBe an[Iterable[_]]
  }

  it should "have next elements" in {
    Set(1, 2, 3).iterator should have('hasNext(true))
  }

  "Tree" should "be an Iterable" in {
    TreeSet(1, 2, 3) shouldBe an[Iterable[_]]
  }

  it should "have next elements" in {
    TreeSet(1, 2, 3).iterator should have('hasNext(true))
  }

  "Map" should "be an Iterable" in {
    Map(1 -> "1", 2 -> "2", 3 -> "3") shouldBe an[Iterable[_]]
  }

  it should "have next elements" in {
    Map(1 -> "1", 2 -> "2", 3 -> "3").iterator should have('hasNext(true))
  }
}
