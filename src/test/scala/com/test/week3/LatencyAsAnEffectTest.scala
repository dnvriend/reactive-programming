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

class LatencyAsAnEffectTest extends TestSpec {

  //
  // Latency as an effect, aka. Future
  // It is a Monad that capture the effect that
  // computations take time, and can fail, so it
  // captures both latency and failure.
  //

  //
  // combinators; another word word the higher-order-functions
  // in where you combine multiple higher order functions
  // to get new behavior
  //

  //
  // callbacks are sometimes called continuations.
  // most often functions from T => Unit (success-callback)
  // or Throwable => Unit (failure-callback)
  //

  val socket = Socket()
  import socket._

  //
  // Note: futureValue and toTry are helper methods that are only available in
  // test suites. Please *do not* use these constructs in your production code.
  // They basically block the test execution to allow the test to assert certain
  // results. Please do not use in production, they do more harm than good!
  //
  // There is one rule: Once you're asynchronous, you should never-ever-block!
  //

  "Socket" should "read from memory and send to Europe" in {
    val fromEurope: Future[Array[Byte]] = readFromMemory.flatMap(sendToEurope(_))
    fromEurope.toTry should be a 'success
    fromEurope.futureValue.getString shouldBe "fromMemory->fromEurope"
  }

  it should "still fail when an error occurs on the network" in {
    val fromEurope = readFromMemory.flatMap(sendToEurope(_, failed = true))
    fromEurope.toTry should be a 'failure
    fromEurope.toTry.failure.exception.getMessage should include("fromEurope")
  }

  it should "let's send the packet twice, to europe and usa, and zip the result" in {
    val fromEurope = readFromMemory.flatMap(sendToEurope(_, failed = true))
    val fromUsa = readFromMemory.flatMap(sendToUsa(_, failed = false))
    // you get a future of a pair of an array of bytes (that's what zip does)
    val response: Future[(Array[Byte], Array[Byte])] = fromEurope.zip(fromUsa)
    response.toTry should be a 'failure // because one of the futures failed, bummer
    response.toTry.failure.exception.getMessage should include("fromEurope")
  }

  it should "first send to europe, when it fails, only then send to usa" in {
    // we now need a handle to the packet we want to send to be reused for the send to Usa case
    val packet = readFromMemory.futureValue
    val result: Future[Array[Byte]] =
      sendToEurope(packet, failed = true)
        .recoverWith {
          case t: Throwable ⇒
            sendToUsa(packet, failed = false)
        }

    result.toTry should be a 'success
    result.futureValue.getString shouldBe "fromMemory->fromUsa"
  }

  //
  // Now for some 'hipster functional programming'...
  //

  //
  // To be a true hipster, we must know about 'implicit conversions'
  // To add some methods to a type, we first convert that type
  // to another type, and then add the functionality eg to
  // add the 'fallbackTo' method to a future, we will create
  // an implicit class HipsterFuture (you can call it anything you
  // wish) and then add the method eg:
  //

  implicit class HipsterFuture[T](f: Future[T]) {

    // When the future 'f' fails, it will execute future 'that',
    // and it 'that' fails, it will return the failed future 'f'
    def fallbackTo(that: ⇒ Future[T]): Future[T] = {
      f.recoverWith {
        case _ ⇒ that recoverWith {
          case _ ⇒ f
        }
      }
    }
  }

  "HipsterFuture" should "support the fallbackTo combinator" in {
    val packet = readFromMemory.futureValue
    val response: Future[Array[Byte]] = // the normal Future will be implicitly converted to HipsterFuture
      sendToEurope(packet, failed = true)
        .fallbackTo(sendToUsa(packet, failed = true))
    // the fallBackTo combinator is quite nice. It factors
    // away all the ugly recoverWith code
    response.toTry should be a 'failure
    response.toTry.failure.exception.getMessage should include("fromEurope")
  }
}
