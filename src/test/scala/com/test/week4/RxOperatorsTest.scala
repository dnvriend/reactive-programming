package com.test.week4

import com.test.TestSpec
import rx.lang.scala._

import scala.concurrent.duration._

class RxOperatorsTest extends TestSpec {

  /**
   * Observables are asynchronous streams of data.
   *
   * Contrary to Futures, they can return multiple values.
   *
   * In ReactiveX an `observer` subscribes to an `Observable`.
   *
   * Then that observer `reacts` to whatever item or sequence
   * of items the Observable `emits`.
   *
   * So, the most important part is that Observable(s) emit
   * items (stuff) that observers can subscribe to and react
   * upon.
   *
   * The workflow is:
   * 1. Create an Observable (note, it emits items!!)
   * 2. write an 'item'-processing pipeline, eg.
   *  take (2) items, then convert those to a list
   * 3. Subscribe to the observable
   * 4. React to the emitted and transformed items
   */

  /**
   * Create an observable that emits 0, 1, 2, ... with a delay
   * of duration between consecutive numbers.
   */
  def observableThatEmitsNumbers: Observable[Long] = Observable.interval(200.millis)

  /**
   * Create an observable that emits no data to the observer
   * and immediately invokes its onCompleted method.
   */
  def emptyObservable: Observable[Nothing] = Observable.empty

  "Observer that emits numbers" should "emit the number 0" in {
    val observable =
        observableThatEmitsNumbers
        // the observable will take 2 items from the stream
        // then it will automatically unsubscribe
        .take(2)
        // for testing, it is better to have a BlockingObservable, normally
        // all operations are asynchronous and non-blocking
        .toBlocking

    // Subscribe to the observable and take the head of the stream, when there are
    // no items, return None
    // Returns an Option with the very first item emitted by the source
    // Observable, or None if the source Observable is empty.
    val result: Option[Long] = observable.headOption
    result.headOption should not be empty
    result.value shouldBe 0
  }

  it should "emit 0, 1" in {
    // the `.toList` returns an Observable that emits a single item, a List composed of all the items emitted by the
    // source Observable. Be careful not to use this operator on Observables that emit infinite or very large numbers
    // of items, as you do not have the option to unsubscribe.
    observableThatEmitsNumbers
      .take(2)
      .toBlocking
      .toList shouldBe List(0, 1)
  }

  it should "transform the items using .map()" in {
    observableThatEmitsNumbers
      .drop(3)
      .take(2)
      .map(n => "number: " + n)
      .toBlocking
      .toList shouldBe List("number: 3", "number: 4")
  }

  it should "merge two streams" in {
    val o1 = observableThatEmitsNumbers.drop(3).take(3) // 3, 4, 5
    val o2 = observableThatEmitsNumbers.take(3)         // 0, 1, 2
    o1.merge(o2)
      .take(6)
      .toBlocking
      .toList shouldBe List(0, 1, 2, 3, 4, 5)
  }

  "An empty observable" should "emit nothing" in {
    emptyObservable
      .toBlocking
      .headOption shouldBe empty
  }

  it should "return an empty list" in {
    emptyObservable.toList.toBlocking.head shouldBe empty
    emptyObservable.toList.toBlocking.head shouldBe Nil
  }

  "A list" should "convert to Observable" in {
    val o1 = List(0, 1, 2).toObservable
    val o2 = List(3, 4, 5).toObservable
    o1.merge(o2)
      .take(6)
      .toBlocking
      .toList shouldBe List(0, 1, 2, 3, 4, 5)
  }

  it should "convert to Observable in reverse" in {
    val o1 = List(0, 1, 2).toObservable
    val o2 = List(3, 4, 5).toObservable
    o2.merge(o1)
      .take(6)
      .toBlocking
      .toList shouldBe List(3, 4, 5, 0, 1, 2)
  }

  "Observables" should "be used as follows" in {
    observableThatEmitsNumbers              // emit numbers
       .slidingBuffer(count = 2, skip = 1)  // buffer 2 elements, skip 1, so (0, 1), (1, 2), (2, 3) etc
       .take(3)                             // take 2 pairs, then unsubscribe automatically
       .toBlocking
       .toList should contain inOrder (Seq(0, 1), Seq(1, 2))
  }

  it should "filter" in {
    observableThatEmitsNumbers             // emit numbers
      .filter(_ % 2 == 0)                  // only emit elements that are even numbers
      .slidingBuffer(count = 2, skip = 2)  // buffer 2 elements and skip 2 (0, 2), (4, 6) etc
      .take(2)                             // take 2 pairs
      .toBlocking
      .toList should contain inOrder (Seq(0, 2), Seq(4, 6))
  }

  it should "flatMap" in {
    val o1 = observableThatEmitsNumbers.take(2)
    val o2 = observableThatEmitsNumbers.take(5)
    o1.flatMap(_ => o2)
      .toBlocking
      .toList should not be empty

      // the content of the resulting list is non-deterministic.
      // this is because, in contrary to iterables, observables are asynchronous
      // the function you are flat-mapping over will produce its values asynchronously
  }

  it should "merge" in {
    val o1 = observableThatEmitsNumbers.take(2)
    val o2 = observableThatEmitsNumbers.take(5)
    o1.merge(o2)
      .toBlocking
      .toList shouldBe List(0, 0, 1, 1, 2, 3, 4)
  }

  it should "concat" in {
    val o1 = observableThatEmitsNumbers.take(2)
    val o2 = observableThatEmitsNumbers.take(5)
    (o1 ++ o2)
      .toBlocking
      .toList shouldBe List(0, 1, 0, 1, 2, 3, 4)
  }

  it should "sum" in {
    observableThatEmitsNumbers
      .take(5)
      .sum
      .toBlocking
      .head shouldBe 10
  }

  it should "count" in {
    observableThatEmitsNumbers
      .take(5)
      .countLong
      .toBlocking
      .head shouldBe 5
  }

  it should "zip" in {
    val o1 = observableThatEmitsNumbers.take(3)
    val o2 = observableThatEmitsNumbers.drop(5).take(5)
    o1.zip(o2)
      .toBlocking
      .toList shouldBe List((0, 5), (1, 6), (2, 7))
  }

  // The marble diagram of the sheet is wrong, the three observables
  // each emit only 3's, or 2's or 1's not zero and ones
  "flattening nested streams" should "return the correct sequence" in {
    val xs: Observable[Int] = Observable.from(List(3, 2, 1))
    val yss: Observable[Observable[Int]] =
      xs.map(x => Observable.interval(x seconds).map(_ => x).take(2))
    val zs: Observable[Int] = yss.flatten

    zs.toBlocking.toList match {
      case List(1, 1, 2, 3, 2, 3) =>
      case List(1, 2, 1, 3, 2, 3) =>
      case u => fail("Unexpected: " + u)
    }
  }

  "Concatenating nested streams" should "return the correct sequence" in {
    Observable.from(List(3, 2, 1))
      .map(x => Observable.interval(x seconds).map(_ => x).take(2))
      .concat
      .toBlocking
      .toList shouldBe List(3, 3, 2, 2, 1, 1)

    // note, never use concat, because it must wait until all streams terminate
    // before the streams can be concatenated.
  }
}
