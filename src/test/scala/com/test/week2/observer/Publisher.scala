package com.test.week2.observer

trait Publisher {
  private var subscribers: Set[Subscriber] = Set()

  /**
   * Add a subscriber to the set
   * @param subscriber
   */
  def subscribe(subscriber: Subscriber): Unit =
    subscribers += subscriber

  /**
   * Remove a subscriber from the set
   * @param subscriber
   */
  def unsubscribe(subscriber: Subscriber): Unit =
    subscribers -= subscriber

  /**
   * Publish the change to all subscribers
   */
  def publish(): Unit =
    subscribers.foreach(_.handler(this))

}
