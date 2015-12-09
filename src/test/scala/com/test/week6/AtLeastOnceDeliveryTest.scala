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

package com.test.week6

import akka.actor.{ PoisonPill, Props, ActorPath }
import akka.event.LoggingReceive
import akka.persistence.{ AtLeastOnceDelivery, PersistentActor }
import com.test.TestSpec
import akka.pattern.ask

class AtLeastOnceDeliveryTest extends TestSpec {

  /**
   * - Guaranteeing delivery means retrying until successful
   * - Retries are the sender's responsibility
   * - The recipient needs to acknowledge receipt
   * - Lost receipts lead to duplicate deliveries
   *   => at-least-once, so 0, or more times delivery by the sender,
   *                     so 0, or more times acknowledge by the recipient
   */

  /**
   * - At-least-once delivery needs the sender and the receiver to collaborate
   * - Retrying means taking note that the message needs to be sent
   * - Acknowledgement means taking note of the receipt of the confirmation
   */

  /**
   * - Performing the effect and persisting that it was done cannot be atomic
   *   - Perform it before persisting for at-least-once semantic
   *   - Perform it after  persisting for at-most-once  semantic
   *
   * - The choice needs to be made based on the underlying business model.
   * - A processing is idempotent then using at-least-once semantic achieves
   *   effectively exactly-once processing
   */

  sealed trait Protocol
  case class PublishPost(text: String, id: Long) extends Protocol
  case class PostPublished(id: Long) extends Protocol

  sealed trait Event
  case class PostCreated(text: String) extends Event

  // the test thread will play a user that posts a message
  sealed trait Api
  case class NewPost(text: String, id: Long) extends Api
  case class BlogPosted(id: Long) extends Api
  case object NrPosted
  case class NrPostedResponse(posted: Long)

  // The userActor will instruct the publisher to publish a post,
  // but the publisher will only do so, when it receives the PublishPost command

  class UserActor(subscriber: ActorPath) extends PersistentActor with AtLeastOnceDelivery {
    override def persistenceId: String = "userActor"

    override def receiveCommand: Receive = LoggingReceive {
      case NewPost(text, id) ⇒
        persist(PostCreated(text)) { e ⇒
          deliver(subscriber)(PublishPost(text, _))
          sender() ! BlogPosted(id)
        }
      case PostPublished(id) ⇒
        confirmDelivery(id)
        persist(PostPublished(id))(_ ⇒ ())
    }

    override def receiveRecover: Receive = LoggingReceive {
      case PostCreated(text) ⇒ deliver(subscriber)(PublishPost(text, _))
      case PostPublished(id) ⇒ confirmDelivery(id)
    }
  }

  class Publisher extends PersistentActor {
    override val persistenceId: String = "publisher"

    var expectedId = 1L

    var nrPosted = 0L

    override def receiveRecover: Receive = LoggingReceive {
      case PostPublished(id) ⇒
        expectedId = id + 1
        nrPosted += 1
    }

    override def receiveCommand: Receive = LoggingReceive {
      case PublishPost(text, id) if id > expectedId ⇒
      // ignore the message, the sender will retry

      case PublishPost(text, id) if id < expectedId ⇒
        // already received, just confirm
        sender() ! PostPublished(id)

      case PublishPost(text, id) if id == expectedId ⇒
        persist(PostPublished(id)) { e ⇒
          sender() ! e
          // modify the website
          nrPosted += 1
          expectedId += 1
        }

      case NrPosted ⇒ sender() ! NrPostedResponse(nrPosted)
    }
  }

  "UserActor" should "Retry sending PublishPost command to Publisher" in {
    var publisher = system.actorOf(Props(new Publisher), "publisher")
    var userActor = system.actorOf(Props(new UserActor(publisher.path)), "userActor")
    val tp = probe
    tp watch publisher
    tp watch userActor
    (publisher ? NrPosted).futureValue shouldBe NrPostedResponse(posted = 0)
    (userActor ? NewPost("foo", 1)).futureValue shouldBe BlogPosted(1)
    (userActor ? NewPost("bar", 2)).futureValue shouldBe BlogPosted(2)
    (publisher ? NrPosted).futureValue shouldBe NrPostedResponse(posted = 2)
    publisher ! PoisonPill
    tp.expectTerminated(publisher)
    userActor ! PoisonPill
    tp.expectTerminated(userActor)
    publisher = system.actorOf(Props(new Publisher), "publisher")
    userActor = system.actorOf(Props(new UserActor(publisher.path)), "userActor")
    (publisher ? NrPosted).futureValue shouldBe NrPostedResponse(posted = 2)
  }
}
