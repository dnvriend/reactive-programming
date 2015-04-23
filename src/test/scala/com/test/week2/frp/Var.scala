package com.test.week2.frp

/**
 * A mutable Signal
 */
object Var {
  /**
   * Creates a new Var
   */
  def apply[T](expr: => T): Var[T] = new Var(expr)
}

class Var[T](expr: => T) extends Signal[T](expr) {
  /**
   * Updates the signal; when it does, all the observers
   * need to be re-evaluated
   */
  override def update(expr: => T): Unit = super.update(expr)
}
