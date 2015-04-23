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
  def withValue[R](newValue: T)(operationToPerform: => R): R = {
    values = newValue :: values
    try operationToPerform finally values = values.tail
  }
}
