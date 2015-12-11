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

import akka.actor._
import akka.event.LoggingReceive
import akka.pattern._
import akka.testkit.{ ImplicitSender, TestKit, TestProbe }
import akka.util.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ GivenWhenThen, BeforeAndAfterAll, FlatSpecLike, Matchers }

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Try

class RunningWithTestProbeTest extends TestKit(ActorSystem("test")) with ImplicitSender with FlatSpecLike with BeforeAndAfterAll with ScalaFutures with Matchers with GivenWhenThen {

  "Toggle" should "toggle happy to sad and back" in {
    val toggle = system.actorOf(Props(new Actor {

      /**
       * Returns a partial function that represents the happy state
       */
      def happy: Receive = LoggingReceive {
        case "How are you?" ⇒
          sender() ! "happy"
          context.become(sad)
      }

      /**
       * Returns a partial function that represents the sad state
       */
      def sad: Receive = LoggingReceive {
        case "How are you?" ⇒
          sender() ! "sad"
          context.become(happy)
      }

      /**
       * Represents the initial actor state
       */
      override def receive: Receive = happy
    }))
    toggle ! "How are you?"
    expectMsg("happy")
    toggle ! "How are you?"
    expectMsg("sad")
    toggle ! "unknown"
    expectNoMsg(1.second)
    cleanUp(toggle)
  }

  "WrongActor" should "sleep one second" in {
    val wrong = system.actorOf(Props(new Actor {
      override def receive: Actor.Receive = LoggingReceive {
        case "How are you?" ⇒
          Thread.sleep(1000) // never ever do this in an actor!!
          sender() ! "I'm fine"
      }
    }))
    wrong ! "How are you?"
    expectMsg(2.seconds, "I'm fine")
    cleanUp(wrong)
  }

  "CorrectActor" should "sleep one second" in {
    val correct = system.actorOf(Props(new Actor {
      import context.dispatcher
      override def receive: Actor.Receive = {
        case "How are you?" ⇒
          context.system.scheduler.scheduleOnce(1.second, sender(), "I'm fine")
      }
    }))
    correct ! "How are you?"
    expectMsg(2.seconds, "I'm fine")
    cleanUp(correct)
  }

  "Ask pattern future" should "succeed when an akka.actor.Status.Success message has been received" in {
    Given("A success actor that will return a akka.actor.Status.Success message when any message has been received")
    val success = system.actorOf(Props(new Actor {
      override def receive = LoggingReceive {
        case _ ⇒ sender() ! Status.Success("success!")
      }
    }), "SuccessActor")

    And("An implicit timeout of 500 milliseconds has been configured")
    implicit val timeout = Timeout(500.millis)

    When("A message 'Foo' has been send to the actor using the ask pattern")
    Then("The Future should succeed")
    success.ask(success, "Foo").toTry should be a 'success
    cleanUp(success)
  }

  it should "fail when an akka.actor.Status.Failure message has been received" in {
    Given("A failure actor that will return a akka.actor.Status.Failure message when any message has been received")
    val failure = system.actorOf(Props(new Actor {
      override def receive = LoggingReceive {
        case _ ⇒ sender() ! Status.Failure(new RuntimeException("This is an error"))
      }
    }), "FailureActor")

    And("An implicit timeout of 500 milliseconds has been configured")
    implicit val timeout = Timeout(500.millis)

    When("A message 'Foo' has been send to the actor using the ask pattern")
    Then("The Future should fail")
    val response = failure.ask(failure, "Foo").toTry
    response should be a 'failure
    response.failed.get.getMessage should include("The future returned an exception of type: java.lang.RuntimeException, with message: This is an error")
    cleanUp(failure)
  }

  it should "fail when the actor throws an RuntimeException, the future will time out" in {
    Given("A failure actor that will throw a RuntimeException when any message has been received")
    val exceptionActor = system.actorOf(Props(new Actor {
      override def receive = LoggingReceive {
        case _ ⇒ throw new RuntimeException("This is an error")
      }
    }), "RuntimeException")

    And("An implicit timeout of 500 milliseconds has been configured")
    implicit val timeout = Timeout(500.millis)

    When("A message 'Foo' has been send to the actor using the ask pattern")
    Then("The Future should time out")
    val response: Try[Any] = (exceptionActor ? "RuntimeException").toTry
    response should be a 'failure
    response.failed.get.getMessage should include("A timeout occurred waiting for a future to complete")
    cleanUp(exceptionActor)
  }

  it should "fail when the actor does not respond, the future will time out" in {
    Given("A non response actor that will never respond when a message had been received")
    val noResponseActor = system.actorOf(Props(new Actor {
      override def receive = LoggingReceive {
        case _ ⇒
      }
    }), "NoResponseActor")

    And("An implicit timeout of 500 milliseconds has been configured")
    implicit val timeout = Timeout(500.millis)

    When("A message 'Foo' has been send to the actor using the ask pattern")
    Then("The Future should time out")
    val response: Try[Any] = (noResponseActor ? "NoResponseActor").toTry
    response should be a 'failure
    response.failed.get.getMessage should include("A timeout occurred waiting for a future to complete")
    cleanUp(noResponseActor)
  }

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  def cleanUp(actors: ActorRef*): Unit =
    actors.foreach { (actor: ActorRef) ⇒
      val probe = TestProbe()
      actor ! PoisonPill
      probe watch actor
      probe.expectTerminated(actor)
    }

  implicit class FutureToTry[T](f: Future[T]) {
    def toTry: Try[T] = Try(f.futureValue)
  }
}
