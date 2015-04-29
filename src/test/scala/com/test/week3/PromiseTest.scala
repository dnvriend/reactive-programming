package com.test.week3

import com.test.TestSpec

import scala.async.Async._
import scala.concurrent.{Promise, Future}
import scala.util.{Success, Failure}

class PromiseTest extends TestSpec {

  //
  // Promise is a construct which are a way to create
  // Futures where you can set the result value of the
  // Future on the outside (success/failure)
  //

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

    // executes the block number of times
    def retry(noTimes: Int)(block: => Future[T]): Future[T] = {
      if(noTimes == 0)
        Future.failed(new Exception("Sorry"))
      else
        block.fallbackTo(retry(noTimes-1)(block))
    }

    // retries the future num times, then retries 'that'
    // num times (seems more interesting to me)
    def retryWith(noTimes: Int)(that: => Future[T]): Future[T] = {
      retry(noTimes)(f).fallbackTo(retry(noTimes)(that))
    }

    // the filter using a promise
    def filter(p: T => Boolean): Future[T] = {
      val promise = Promise[T]()
      f.onComplete {
        case Failure(t) => promise.failure(t)
        case Success(x) =>
          if(!p(x))
            promise.failure(new NoSuchElementException)
          else
            promise.success(x)
      }
      promise.future
    }
  }

  def race[T](left: Future[T], right: Future[T]): Future[T] = {
    val promise = Promise[T]()
    left.onComplete(promise.tryComplete)
    right.onComplete(promise.tryComplete)
    promise.future
  }

  def zip[T, S, R](p: Future[T], q: Future[S])(f: (T, S) => R): Future[R] = {
    val promise = Promise[R]()
    p.onComplete {
      case Failure(t) => promise.failure(t)
      case Success(x) => q.onComplete {
        case Failure(t) => promise.failure(t)
        case Success(y) => promise.success(f(x, y))
      }
    }
    promise.future
  }

  def zipAwait[T, S, R](p: Future[T], q: Future[S])(f: (T, S) => R): Future[R] = async {
    f(await(p), await(q))
  }

  def sequence[T](fxs: List[Future[T]]): Future[List[T]] = {
    fxs match {
      case Nil => Future(Nil)
      case (head :: tail) =>
        head.flatMap(x =>
          sequence(tail)
            .flatMap(xs => Future(x :: xs))
          )
    }
  }

  "HipsterFuture" should "support the filter combinator" in {
    Future(1).filter(_ == 1).toTry should be a 'success
    Future(1).filter(_ == 0).toTry should be a 'failure
  }

  "race" should "complete the first the fastest future" in {
    def sleep(millis: Long) = Thread.sleep(millis)
    val result: Future[Int] = race(Future {sleep(250); 1 }, Future {sleep(750); 2})
    result.toTry should be a 'success
    result.futureValue shouldBe 1
  }

  it should "complete the second the fastest future" in {
    def sleep(millis: Long) = Thread.sleep(millis)
    val result: Future[Int] = race(Future {sleep(750); 1 }, Future {sleep(250); 2})
    result.toTry should be a 'success
    result.futureValue shouldBe 2
  }

  "zip" should "zip two success futures" in {
    val result: Future[Int] = zip(Future(2), Future(3)) {
      case (x, y) => x * y
    }
    result.toTry should be a 'success
    result.futureValue shouldBe 6
  }

  it should "fail when one future fails" in {
    val result: Future[Int] = zip(Future(1/0), Future(3)) {
      case (x, y) => x * y
    }
    result.toTry should be a 'failure
  }

  "zipAwait" should "zip two success futures" in {
    val result: Future[Int] = zipAwait(Future(4), Future(5)) {
      case (x, y) => x * y
    }
    result.toTry should be a 'success
    result.futureValue shouldBe 20
  }

  it should "fail when one future fails" in {
    val result: Future[Int] = zipAwait(Future(1/0), Future(3)) {
      case (x, y) => x * y
    }
    result.toTry should be a 'failure
  }

  "sequence" should "return a list of numbers" in {
    val xs: Future[List[Int]] = sequence(List(Future(1), Future(2), Future(3)))
    xs.toTry should be a 'success
    xs.futureValue shouldBe List(1, 2, 3)
  }
}
