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
import rx.lang.scala.Subject
import rx.lang.scala.subjects.{ BehaviorSubject, AsyncSubject, ReplaySubject, PublishSubject }

import scala.concurrent.{ Future, Promise }
import scala.util.Success

class PromisesAndSubjectsTest extends TestSpec {

  def futureOfInt: Future[Int] = {
    val p = Promise[Int]()
    p.complete(Success(42))
    p.future
  }

  "Promise" should "return a future" in {
    futureOfInt.map(_ / 2).futureValue shouldBe 21
  }

  // a Subject represents an object that is both
  // an Observable and an Observer.

  // Rx has the following subjects:
  // AsyncSubject, BehaviorSubject, PublishSubject,
  // ReplaySubject, SerializedSubject, TestSubject

  /**
   * A PublishSubject is a Subject that, once an Observer has subscribed,
   * emits all subsequently observed items to the subscriber.
   *
   * see: http://reactivex.io/RxJava/javadoc/rx/subjects/PublishSubject.html
   */
  "PublishSubject" should "return an Observable" in {
    val channel = PublishSubject[Int]()

    var xsA: List[Int] = Nil
    var xsB: List[Int] = Nil
    var xsC: List[Int] = Nil

    val a = channel.subscribe(x ⇒ xsA = x :: xsA)
    val b = channel.subscribe(x ⇒ xsB = x :: xsB)

    // explicitly call onNext
    channel.onNext(42) // put 42 on the channel
    a.unsubscribe()
    xsA shouldBe List(42)
    xsB shouldBe List(42)

    channel.onNext(4711) // put 4711 on the channel
    channel.onCompleted()
    xsA shouldBe List(42)
    xsB shouldBe List(4711, 42)

    val c = channel.subscribe(x ⇒ xsC = x :: xsC)
    channel.onNext(13) // you cannot put 13 on the channel, it already has been closed/completed
    xsA shouldBe List(42)
    xsB shouldBe List(4711, 42)
    xsC shouldBe Nil
  }

  /**
   * Subject that buffers all items it observes and replays
   * them to any Observer that subscribes.
   *
   * see: http://reactivex.io/RxJava/javadoc/rx/subjects/ReplaySubject.html
   */
  "ReplaySubject" should "replay history" in {
    val channel = ReplaySubject[Int](3)

    var xsA: List[Int] = Nil
    var xsB: List[Int] = Nil
    var xsC: List[Int] = Nil

    val a = channel.subscribe(x ⇒ xsA = x :: xsA)
    val b = channel.subscribe(x ⇒ xsB = x :: xsB)

    // explicitly call onNext
    (1 to 3).foreach(channel.onNext)
    xsA shouldBe List(3, 2, 1)
    xsB shouldBe List(3, 2, 1)
    a.unsubscribe()
    b.unsubscribe()

    channel.onCompleted() // close the channel
    val c = channel.subscribe(x ⇒ xsC = x :: xsC)
    channel.onNext(13) // you cannot put 13 on the channel, it already has been closed/completed
    xsC shouldBe List(3, 2, 1) // but the ReplaySubject has a buffer of 3, so they get placed on the channel
  }

  /**
   * Subject that publishes only the last item observed to each Observer
   * that has subscribed, when the source Observable completes.
   *
   * see: http://reactivex.io/RxJava/javadoc/rx/subjects/AsyncSubject.html
   */
  "AsyncSubject" should "return the last item" in {
    val channel = AsyncSubject[Int]()

    var xsA: List[Int] = Nil
    var xsB: List[Int] = Nil
    var xsC: List[Int] = Nil

    val a = channel.subscribe(x ⇒ xsA = x :: xsA)
    val b = channel.subscribe(x ⇒ xsB = x :: xsB)

    // explicitly call onNext
    (1 to 3).foreach(channel.onNext)
    channel.onCompleted() // the channel must be closed! see the docs
    xsA shouldBe List(3)
    xsB shouldBe List(3)
    a.unsubscribe()
    b.unsubscribe()

    val c = channel.subscribe(x ⇒ xsC = x :: xsC)
    channel.onNext(13) // you cannot put 13 on the channel, it already has been closed/completed
    xsC shouldBe List(3) // returns the last item observed to the observer
  }

  /**
   * Subject that emits the most recent item it has observed and all
   * subsequent observed items to each subscribed Observer.
   *
   * see: http://reactivex.io/RxJava/javadoc/rx/subjects/BehaviorSubject.html
   */
  "BehaviorSubject" should "most recent item" in {
    val channel = BehaviorSubject[Int]()

    var xsA: List[Int] = Nil
    var xsB: List[Int] = Nil
    var xsC: List[Int] = Nil

    val a = channel.subscribe(x ⇒ xsA = x :: xsA)
    channel.onNext(1)
    channel.onNext(2)
    val b = channel.subscribe(x ⇒ xsB = x :: xsB)
    channel.onNext(3)
    xsA shouldBe List(3, 2, 1)
    xsB shouldBe List(3, 2)
    a.unsubscribe()
    b.unsubscribe()

    val c = channel.subscribe(x ⇒ xsC = x :: xsC)
    channel.onNext(4)
    xsC shouldBe List(4, 3)
    channel.onCompleted()
  }
}
