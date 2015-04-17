package com.test

import akka.actor.ActorSystem
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FlatSpec, Matchers}

trait TestSpec extends FlatSpec with Matchers with ScalaFutures {
  val system = ActorSystem("test")
}
