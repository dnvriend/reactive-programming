package com.test.week5

import akka.actor.{Actor, Props}
import akka.event.LoggingReceive
import com.test.TestSpec

class CounterTest extends TestSpec {

  class Counter extends Actor {

    def counter(n: Int): Receive = LoggingReceive {
      case "incr" => context.become(counter(n + 1))
      case "get" => sender() ! n
    }

    override def receive = counter(0)
  }

  "counter" should "increment" in {
    val counter = system.actorOf(Props(new Counter), "counter")
    val p = probe
    p.send(counter, "incr")
    p.send(counter, "get")
    p.expectMsg(1)
    p.send(counter, "incr")
    p.send(counter, "get")
    p.expectMsg(2)
    cleanup(counter)
  }
}
