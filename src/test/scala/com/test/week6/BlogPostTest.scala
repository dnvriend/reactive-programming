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

import akka.actor.{ PoisonPill, Props }
import akka.event.LoggingReceive
import akka.pattern.ask
import akka.persistence.PersistentActor
import com.test.TestSpec

class BlogPostTest extends TestSpec {

  sealed trait Event
  case class PostCreated(text: String) extends Event
  case object QuotaReached extends Event

  case class NewPost(text: String, id: Long)
  case class BlogPosted(id: Long)
  case class BlogNotPosted(id: Long, reason: String)

  case class State(posts: Vector[String], disabled: Boolean) {
    def update(event: Event): State = event match {
      case PostCreated(text) ⇒ copy(posts = posts :+ text)
      case QuotaReached      ⇒ copy(disabled = true)
    }
  }

  class UserActor(pid: Long) extends PersistentActor {
    override val persistenceId: String = "user-" + pid // must be a stable id

    // state is encapsulated in a single case class that has all
    // logic to update the state
    var state = State(Vector.empty[String], disabled = false)

    def updateState(event: Event): Unit = {
      state = state.update(event)
    }

    override def receiveRecover: Receive = LoggingReceive {
      case event: Event ⇒ updateState(event)
    }

    override def receiveCommand: Receive = LoggingReceive {
      case NewPost(_, id) if state.disabled ⇒
        sender() ! BlogNotPosted(id, "quota reached")

      case NewPost(text, id) if !state.disabled ⇒
        persist(PostCreated(text)) { event ⇒
          updateState(event)
          sender ! BlogPosted(id) // confirm
        }
        persist(QuotaReached)(updateState)
    }
  }

  class UserActorAsync(pid: Long) extends PersistentActor {
    override val persistenceId: String = "user-" + pid // must be a stable id

    // state is encapsulated in a single case class that has all
    // logic to update the state
    var state = State(Vector.empty[String], disabled = false)

    def updateState(event: Event): Unit = {
      state = state.update(event)
    }

    override def receiveRecover: Receive = LoggingReceive {
      case event: Event ⇒ updateState(event)
    }

    override def receiveCommand: Receive = LoggingReceive {
      case NewPost(_, id) if state.disabled ⇒
        sender() ! BlogNotPosted(id, "quota reached")

      case NewPost(text, id) if !state.disabled ⇒
        val created = PostCreated(text)
        updateState(created)
        updateState(QuotaReached)
        persistAsync(created)(_ ⇒ sender() ! BlogPosted(id))
        persistAsync(QuotaReached)(_ ⇒ ())
        persist(QuotaReached)(updateState)
    }
  }

  def createUser(pid: Long) =
    system.actorOf(Props(new UserActor(pid)))

  "BlogPost" should "store post and remember state" in {
    var user1 = createUser(1)
    (user1 ? NewPost("foo", 1)).futureValue shouldBe BlogPosted(1)
    (user1 ? NewPost("bar", 2)).futureValue shouldBe BlogNotPosted(2, "quota reached")
    user1 ! PoisonPill
    user1 = createUser(1)
    (user1 ? NewPost("bar", 2)).futureValue shouldBe BlogNotPosted(2, "quota reached")
  }

  "AsyncBlogPost" should "store post and remember state" in {
    var user1 = createUser(1)
    // because of async processing of events, the responses can be out of order
    val possilbleResponses: PartialFunction[Any, Unit] = {
      case BlogPosted(1)                     ⇒
      case BlogNotPosted(1, "quota reached") ⇒
      case BlogNotPosted(2, "quota reached") ⇒
    }
    (user1 ? NewPost("foo", 1)).futureValue mustBe possilbleResponses
    (user1 ? NewPost("bar", 2)).futureValue mustBe possilbleResponses
    user1 ! PoisonPill
    user1 = createUser(1)
    (user1 ? NewPost("bar", 2)).futureValue mustBe possilbleResponses
  }
}
