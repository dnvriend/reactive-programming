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

package com.test.week2.frp

import scala.util.DynamicVariable

// a special signal that has no value and no implementation
object NoSignal extends Signal[Nothing](???) {
  override def computeValue() = ()
}

object Signal {
  // The DynamicVariable is a way to obtain variables
  // anywhere in the code (not passing through methods
  // arguments eg. implicitly by means of currying),
  // which would be much nicer.
  //
  // The DynamicVariable uses the ThreadLocal trick, which means
  // that DynamicVariable binds variables to a specific thread
  // that processes the code.
  //
  // So it's a non-intrusive way to store and pass around
  // context(thread)-specific information.
  //
  // The variable is global for the thread, but it is not shared between
  // threads.
  private val caller = new DynamicVariable[Signal[_]](NoSignal)
  /**
   * Creates a new Signal
   */
  def apply[T](expr: ⇒ T): Signal[T] = new Signal(expr)
}

class Signal[T](expr: ⇒ T) {
  import Signal._

  // the current value of the signal
  private var myValue: T = _

  // the 'current' expression that defines the signal value
  private var myExpr: () ⇒ T = _

  // a set of observers: the other signals that depend on its value
  private var observers: Set[Signal[_]] = Set()

  update(expr)

  protected def update(expr: ⇒ T): Unit = {
    myExpr = () ⇒ expr
    computeValue()
  }

  protected def computeValue(): Unit = {
    // the withValue method will be called with a newValue,
    // which is this Signal, and an expression 'myExpr'.
    //
    // When, within the expression 'myExpr()', the thread tries to get
    // the value of a signal (by calling the apply() method of a Signal,
    // like eg. 'c()' from the Coursera Lecture, this operation will take
    // place within the scope of the second argument 'myExpr()'.
    //
    // When you scroll down you will see that apply() calls 'caller.value'.
    //
    // When the thread calls 'caller.value', it will be given back this Signal
    //
    val newValue = caller.withValue(this)(myExpr())
    if (myValue != newValue) {
      myValue = newValue
      val obs = observers
      observers = Set()
      obs.foreach(_.computeValue())
    }
  }

  def apply(): T = {
    // when the value of the signal is requested, the Signal will be added to the list of
    // observers which depends on the value of this signal.
    observers += caller.value
    // will throw an error, remember: 'balance() = balance() + amount' ?
    assert(!caller.value.observers.contains(this), "cyclic signal definition")
    myValue
  }
}
