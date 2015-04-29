package com.test.week3

import com.test.TestSpec

import scala.async.Async._
import scala.concurrent.Future
import scala.util.{Failure, Try}

class AsyncAwaitTest extends TestSpec {

  //
  // Note: we should import:
  // "org.scala-lang.modules" %% "scala-async"     % "0.9.2",
  //
  // please read: https://github.com/scala/async
  // it explains what is going on with the async..await
  // construct, as it is being rewritten to non-blocking code
  //

  //
  // I don't know if I will use it... oh well, choices..
  //

  def slowCalcFuture: Future[Int] = Future {
    Thread.sleep(200)
    1
  }

  /**
   * Note, the async..await will be rewritten by the
   * compiler, it returns a future, and nothing is blocked
   * on.. but, we can 'reason' about it in a blocked way
   * within the async {} block
   *
   * What do 'they' say about Macro's every single time?
   * .. -> don't use it.
   */
  def combined: Future[Int] = async {
    // execute sequentially
    await(slowCalcFuture) + await(slowCalcFuture)
  }

  /**
   * Execute in parallel
   */
  def parallel: Future[Int] = async {
    val f1 = slowCalcFuture
    val f2 = slowCalcFuture
    await(f1) + await(f2)
  }

  "AsyncAwait" should "evaluate combined" in {
    combined.toTry should be a 'success
    combined.futureValue shouldBe 2
  }

  it should "evaluate parallel" in {
    parallel.toTry should be a 'success
    parallel.futureValue shouldBe 2
  }

  //
  // HipsterFuture
  //

  implicit class HipsterFuture[T](f: Future[T]) {
    def fallbackTo(that: => Future[T]): Future[T] = {
      f.recoverWith {
        case _ => that recoverWith {
          case _ => f
        }
      }
    }

    // imperative..
    def retry(noTimes: Int, block: => Future[T]): Future[T] = async {
      var i = 0
      var result: Try[T] = Failure(new Exception("_"))
      while(result.isFailure && i < noTimes) {
        result = await (block)
        i += 1
      }
      result.get
    }

    implicit def futureOfTToFutureOfTryOfT(f: Future[T]): Future[Try[T]] =
      Future(f.toTry)

    // retries the future num times, then retries 'that'
    // num times (seems more interesting to me)
    def retryWith(noTimes: Int)(that: => Future[T]): Future[T] = {
      retry(noTimes, f).fallbackTo(retry(noTimes, that))
    }

    def filter(p: T => Boolean): Future[T] = async {
      val x = await (f)
      if (!p(x)) throw new NoSuchElementException else x
    }
  }

  val socket = Socket()
  import socket._

  "HipsterFuture" should "support the retry combinator" in {
    val packet = readFromMemory.futureValue
    val result: Future[Array[Byte]] =
      sendToEurope(packet, failed = true)
        .retryWith(3)(sendToUsa(packet, failed = false))

    result.toTry should be a 'success
  }

  it should "support the new filter combinator" in {
    Future(1).filter(_ == 1).toTry should be a 'success
    Future(1).filter(_ == 0).toTry should be a 'failure
  }
}
