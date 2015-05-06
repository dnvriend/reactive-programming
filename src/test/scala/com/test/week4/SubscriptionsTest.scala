package com.test.week4

import com.test.TestSpec
import rx.lang.scala._
import rx.lang.scala.subscriptions.{SerialSubscription, MultipleAssignmentSubscription, CompositeSubscription}
import scala.concurrent.duration._

class SubscriptionsTest extends TestSpec {

  "Streams" should "manually unsubscribe" in {
    val s1: Subscription = Observable.interval(1.second).subscribe()
    val s2: Subscription = Observable.interval(2.seconds).subscribe()
    s1 should not be 'unsubscribed
    s2 should not be 'unsubscribed
    s1.unsubscribe()
    s1 should be ('unsubscribed)
    s2 should not be 'unsubscribed
    s2.unsubscribe()
    s2 should be ('unsubscribed)

    // s1 and s2 are both 'cold' observables,
    // because they each have their own private
    // data source

    // 'hot' observables all share the same data source
    // so all subscribers are all shared together
  }

  "Subscription" should "unsubscribe idempotently" in {
    var counter = 0
    val s1 = Subscription {
      counter += 1
    }
    s1 should not be 'unsubscribed
    s1.unsubscribe()
    s1 should be ('unsubscribed)
    s1.unsubscribe()
    counter shouldBe 1
  }

  "CompositeSubscription" should "unsubscribe both subscriptions" in {
    val s1 = Subscription()
    val s2 = Subscription()
    // Subscription that represents a group of Subscriptions that are unsubscribed together.
    val composite = CompositeSubscription(s1, s2)
    s1 should not be 'unsubscribed
    s2 should not be 'unsubscribed
    composite should not be 'unsubscribed
    composite.unsubscribe()
    s1 should be ('unsubscribed)
    s2 should be ('unsubscribed)
  }

  "MultipleAssignmentSubscription" should "unsubscribe" in {
    val s1 = Subscription()
    val s2 = Subscription()

    val multi = MultipleAssignmentSubscription()
    multi should not be 'unsubscribed
    s1 should not be 'unsubscribed
    s2 should not be 'unsubscribed

    // set the underlying subscription of multi to s1
    multi.subscription = s1
    // unsubscribe multi
    multi.unsubscribe()
    multi should be ('unsubscribed)
    s1 should be ('unsubscribed)
    s2 should not be 'unsubscribed

    // so multi is unsubscribed.. now set the underlying
    // subscription of multi to s2, which is still subscribed,
    // see what happens
    multi.subscription = s2
    multi should be ('unsubscribed)
    s1 should be ('unsubscribed)
    s2 should be ('unsubscribed)
  }

  "SerialSubscription" should "unsubscribe" in {
    val s1 = Subscription()
    val s2 = Subscription()

    // SerialSubscription Represents a subscription whose underlying
    // subscription can be swapped for another subscription which
    // causes the previous underlying subscription to be unsubscribed.
    val serial = SerialSubscription()
    serial.subscription = s1
    serial should not be 'unsubscribed
    s1 should not be 'unsubscribed
    s2 should not be 'unsubscribed
    serial.subscription = s2
    serial should not be 'unsubscribed
    s1 should be ('unsubscribed)
    s2 should not be 'unsubscribed
  }
}
