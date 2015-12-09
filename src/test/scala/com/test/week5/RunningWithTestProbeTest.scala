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

import akka.actor.{ Actor, Props, ActorSystem }
import akka.event.LoggingReceive
import akka.testkit.{ ImplicitSender, TestKit }
import org.scalatest.{ BeforeAndAfterAll, FlatSpecLike }
import scala.concurrent.duration._

class RunningWithTestProbeTest extends TestKit(ActorSystem("test")) with ImplicitSender with FlatSpecLike with BeforeAndAfterAll {

  class Toggle extends Actor {

    def happy: Receive = LoggingReceive {
      case "How are you?" ⇒
        sender() ! "happy"
        context.become(sad)
    }

    def sad: Receive = LoggingReceive {
      case "How are you?" ⇒
        sender() ! "sad"
        context.become(happy)
    }

    override def receive: Receive = happy
  }

  class SleepActorWrong extends Actor {
    override def receive: Actor.Receive = LoggingReceive {
      case "How are you?" ⇒
        Thread.sleep(1000) // never ever do this in an actor!!
        sender() ! "I'm fine"
    }
  }

  class SleepActorCorrect extends Actor {
    import context.dispatcher
    override def receive: Actor.Receive = {
      case "How are you?" ⇒
        context.system.scheduler.scheduleOnce(1.second, sender(), "I'm fine")
    }
  }

  "Toggle" should "toggle happy to sad and back" in {
    val toggle = system.actorOf(Props(new Toggle))
    toggle ! "How are you?"
    expectMsg("happy")
    toggle ! "How are you?"
    expectMsg("sad")
    toggle ! "unknown"
    expectNoMsg(1.second)
  }

  "WrongActor" should "sleep one second" in {
    val wrong = system.actorOf(Props(new SleepActorWrong))
    wrong ! "How are you?"
    expectMsg(2.seconds, "I'm fine")
  }

  "CorrectActor" should "sleep one second" in {
    val correct = system.actorOf(Props(new SleepActorCorrect))
    correct ! "How are you?"
    expectMsg(2.seconds, "I'm fine")
  }

  override protected def afterAll(): Unit = {
    system.shutdown()
  }
}
