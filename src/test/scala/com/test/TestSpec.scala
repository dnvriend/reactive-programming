package com.test

import java.io.IOException

import akka.actor.{PoisonPill, ActorRef, ActorSystem}
import akka.event.{LoggingAdapter, Logging}
import akka.testkit.TestProbe
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.exceptions.TestFailedException
import org.scalatest.{OptionValues, TryValues, FlatSpec, Matchers}
import rx.lang.scala._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Try, Random => Rnd}

object Random {
  def apply(): Rnd = new Rnd()
}

trait TestSpec extends FlatSpec with Matchers with ScalaFutures with TryValues with OptionValues with Eventually {
  implicit val system: ActorSystem = ActorSystem("test")
  implicit val ec: ExecutionContext = system.dispatcher
  val log: LoggingAdapter = Logging(system, this.getClass)
  implicit val pc: PatienceConfig = PatienceConfig(timeout = 50.seconds)

  def probe: TestProbe = TestProbe()

  def cleanup(actors: ActorRef*): Unit = {
    actors.foreach { (actor: ActorRef) =>
      actor ! PoisonPill
      probe watch actor
    }
  }

  implicit class PimpedByteArray(self: Array[Byte]) {
    def getString: String = new String(self)
  }

  implicit class PimpedFuture[T](self: Future[T]) {
    def toTry: Try[T] = Try(self.futureValue)
  }

  implicit class PimpedObservable[T](self: Observable[T]) {
    def waitFor: Unit = {
      self.toBlocking.toIterable.last
    }
  }

  implicit class MustBeWord[T](self: T) {
    def mustBe(pf: PartialFunction[T, Unit]): Unit =
      if(!pf.isDefinedAt(self)) throw new TestFailedException("Unexpected: " + self, 0)
  }

  object Socket { def apply() = new Socket }
  class Socket {
    def readFromMemory: Future[Array[Byte]] = Future {
      Thread.sleep(100) // sleep 100 millis
      "fromMemory".getBytes
    }

    def send(payload: Array[Byte], from: String, failed: Boolean): Future[Array[Byte]] =
      if(failed) Future.failed(new IOException(s"Network error: $from"))
      else {
        Future {
          Thread.sleep(250) // sleep 250 millis, not real life time, but hey
          s"${payload.getString}->$from".getBytes
        }
      }

    def sendToEurope(payload: Array[Byte], failed: Boolean = false): Future[Array[Byte]] =
      send(payload, "fromEurope", failed)

    def sendToUsa(payload: Array[Byte], failed: Boolean = false): Future[Array[Byte]] =
      send(payload, "fromUsa", failed)
  }
}
