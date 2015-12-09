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
