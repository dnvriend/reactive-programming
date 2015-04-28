package com.test

import akka.actor.ActorSystem
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{TryValues, FlatSpec, Matchers}

import scala.util.Random

object Random {
  def apply(): Random = new Random()
}

trait TestSpec extends FlatSpec with Matchers with ScalaFutures with TryValues {
  val system = ActorSystem("test")
}
