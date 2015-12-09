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

/**
 * A stack for Signals
 */
class StackableVariable[T](init: T) {
  // a list of Signals
  private var values: List[T] = List(init)
  // the current value
  def value: T = values.head
  // put the new value on the top of the stack
  def withValue[R](newValue: T)(operationToPerform: ⇒ R): R = {
    values = newValue :: values
    try operationToPerform finally values = values.tail
  }
}
