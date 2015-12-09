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
 * A mutable Signal
 */
object Var {
  /**
   * Creates a new Var
   */
  def apply[T](expr: ⇒ T): Var[T] = new Var(expr)
}

class Var[T](expr: ⇒ T) extends Signal[T](expr) {
  /**
   * Updates the signal; when it does, all the observers
   * need to be re-evaluated
   */
  override def update(expr: ⇒ T): Unit = super.update(expr)
}
