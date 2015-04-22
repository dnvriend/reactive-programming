package com.test.week2.observer

trait Subscriber {
  def handler(pub: Publisher): Unit
}
