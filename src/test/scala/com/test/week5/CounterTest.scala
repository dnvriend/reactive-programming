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

package com.test.week5

import akka.actor.{ Actor, Props }
import akka.event.LoggingReceive
import com.test.TestSpec

class CounterTest extends TestSpec {

  class Counter extends Actor {

    def counter(n: Int): Receive = LoggingReceive {
      case "incr" ⇒ context.become(counter(n + 1))
      case "get"  ⇒ sender() ! n
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
