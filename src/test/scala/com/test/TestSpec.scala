package com.test

import akka.actor.ActorSystem
import org.scalatest.{FlatSpec, Matchers}

trait TestSpec extends FlatSpec with Matchers {
  val system = ActorSystem("test")
}
