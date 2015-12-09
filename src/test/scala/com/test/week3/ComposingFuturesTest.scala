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

package com.test.week3

import com.test.TestSpec

import scala.concurrent.Future

class ComposingFuturesTest extends TestSpec {

  //
  // Once you introduce Futures, you are in FutureLand.. LoL!
  //

  implicit class HipsterFuture[T](f: Future[T]) {
    def fallbackTo(that: ⇒ Future[T]): Future[T] = {
      f.recoverWith {
        case _ ⇒ that recoverWith {
          case _ ⇒ f
        }
      }
    }

    // executes the block number of times
    def retry(noTimes: Int)(block: ⇒ Future[T]): Future[T] = {
      if (noTimes == 0)
        Future.failed(new Exception("Sorry"))
      else
        block.fallbackTo(retry(noTimes - 1)(block))
    }

    // retries the future num times, then retries 'that'
    // num times (seems more interesting to me)
    def retryWith(noTimes: Int)(that: ⇒ Future[T]): Future[T] = {
      retry(noTimes)(f).fallbackTo(retry(noTimes)(that))
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
}
