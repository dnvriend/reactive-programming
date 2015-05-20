package com.test.week6

import akka.actor.{PoisonPill, Props}
import akka.event.LoggingReceive
import akka.pattern.ask
import akka.persistence.PersistentActor
import com.test.TestSpec

sealed trait Event

case class PostCreated(text: String) extends Event

case object QuotaReached extends Event

class BlogPostTest extends TestSpec {

  case class NewPost(text: String, id: Long)

  case class BlogPosted(id: Long)

  case class BlogNotPosted(id: Long, reason: String)

  case class State(posts: Vector[String], disabled: Boolean) {
    def update(event: Event): State = event match {
      case PostCreated(text) => copy(posts = posts :+ text)
      case QuotaReached => copy(disabled = true)
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
      case event: Event => updateState(event)
    }

    override def receiveCommand: Receive = LoggingReceive {
      case NewPost(_, id) if state.disabled =>
        sender() ! BlogNotPosted(id, "quota reached")

      case NewPost(text, id) if !state.disabled =>
        persist(PostCreated(text)) { event =>
          updateState(event)
          sender ! BlogPosted(id) // confirm
        }
        persist(QuotaReached)(updateState)
    }
  }

  def createUser(pid: Long) =
    system.actorOf(Props(new UserActor(pid)))

  "BlogPost" should "have lame quota size" in {
    var user1 = createUser(1)
    (user1 ? NewPost("foo", 1)).futureValue shouldBe BlogPosted(1)
    (user1 ? NewPost("bar", 2)).futureValue shouldBe BlogNotPosted(2, "quota reached")
    user1 ! PoisonPill
    user1 = createUser(1)
    (user1 ? NewPost("bar", 2)).futureValue shouldBe BlogNotPosted(2, "quota reached")
  }
}
