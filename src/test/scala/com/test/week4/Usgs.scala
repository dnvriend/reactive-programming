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

package com.test.week4

import rx.lang.scala.Observable

import scala.concurrent.{ Promise, Future }
import scala.util.Random

object Usgs {

  lazy val rnd = new Random()

  case class EarthQuake(magnitude: Double, location: (Double, Double))
  case class Country(name: String)

  /**
   * A very very inaccurate and course grained geo tranlation table, please forgive me :)
   */
  def reverseGeoCode(c: (Double, Double)): Future[Country] = {
    val p = Promise[Country]()
    val country = c match {
      case (a, b) if (0 to 50).contains(a.toInt) && (0 to 20).contains(b.toInt)     ⇒ Country("United Kingdom")
      case (a, b) if (0 to 50).contains(a.toInt) && (20 to 40).contains(b.toInt)    ⇒ Country("Germany")
      case (a, b) if (0 to 50).contains(a.toInt) && (40 to 60).contains(b.toInt)    ⇒ Country("Belgium")
      case (a, b) if (0 to 50).contains(a.toInt) && (60 to 80).contains(b.toInt)    ⇒ Country("France")
      case (a, b) if (0 to 50).contains(a.toInt) && (80 to 100).contains(b.toInt)   ⇒ Country("Spain")
      case (a, b) if (50 to 100).contains(a.toInt) && (0 to 20).contains(b.toInt)   ⇒ Country("America")
      case (a, b) if (50 to 100).contains(a.toInt) && (20 to 40).contains(b.toInt)  ⇒ Country("Canada")
      case (a, b) if (50 to 100).contains(a.toInt) && (40 to 60).contains(b.toInt)  ⇒ Country("Mexico")
      case (a, b) if (50 to 100).contains(a.toInt) && (60 to 80).contains(b.toInt)  ⇒ Country("Colombia")
      case (a, b) if (50 to 100).contains(a.toInt) && (80 to 100).contains(b.toInt) ⇒ Country("Brazil")
      case _                                                                        ⇒ Country("The Netherlands")
    }
    p.success(country)
    p.future
  }

  trait Magnitude { def min: Int; def max: Int }
  case object Micro extends Magnitude { val min = 0; val max = 5 }
  case object Minor extends Magnitude { val min = 5; val max = 10 }
  case object Light extends Magnitude { val min = 10; val max = 30 }
  case object Moderate extends Magnitude { val min = 30; val max = 60 }
  case object Strong extends Magnitude { val min = 60; val max = 70 }
  case object Major extends Magnitude { val min = 70; val max = 90 }
  case object Great extends Magnitude { val min = 90; val max = 100 }

  /**
   * This is not a correct translation, but hey...
   */
  object Magnitude {
    def apply(mag: Int): Magnitude = mag match {
      case _ if (Micro.min to Micro.max).contains(mag)       ⇒ Micro
      case _ if (Minor.min to Minor.max).contains(mag)       ⇒ Minor
      case _ if (Light.min to Light.max).contains(mag)       ⇒ Light
      case _ if (Moderate.min to Moderate.max).contains(mag) ⇒ Moderate
      case _ if (Strong.min to Strong.max).contains(mag)     ⇒ Strong
      case _ if (Major.min to Major.max).contains(mag)       ⇒ Major
      case _                                                 ⇒ Great
    }
  }

  /**
   * Generate a random EarthQuake
   */
  def generateEarthQuake: EarthQuake =
    EarthQuake(magnitude = rnd.nextDouble * 100, location = (rnd.nextDouble * 100, rnd.nextDouble * 100))

  /**
   * Return a continuous stream of EarthQuakes
   * @return
   */
  def stream: Observable[EarthQuake] = Observable(observer ⇒ {
    try {
      while (!observer.isUnsubscribed) {
        observer.onNext(generateEarthQuake)
      }
      observer.onCompleted()
    } catch {
      case ex: Throwable ⇒ observer.onError(ex)
    }
  })
}
