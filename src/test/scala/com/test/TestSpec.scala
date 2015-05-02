package com.test

import java.io.IOException

import akka.actor.ActorSystem
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{OptionValues, TryValues, FlatSpec, Matchers}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Try, Random}

object Random {
  def apply(): Random = new Random()
}

trait TestSpec extends FlatSpec with Matchers with ScalaFutures with TryValues with OptionValues {
  implicit val system: ActorSystem = ActorSystem("test")
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val pc: PatienceConfig = PatienceConfig(timeout = 50.seconds)

  implicit class PimpedByteArray(arr: Array[Byte]) {
    def getString: String = new String(arr)
  }

  implicit class PimpedFuture[T](f: Future[T]) {
    def toTry: Try[T] = Try(f.futureValue)
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
