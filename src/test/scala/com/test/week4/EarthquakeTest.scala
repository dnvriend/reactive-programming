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

import com.test.TestSpec
import rx.lang.scala._

import scala.concurrent.Future

class EarthQuakeTest extends TestSpec {
  import Usgs._

  "EarthQuake stream" should "be queried" in {
    Usgs.stream
      .map(q ⇒ (q.location, Magnitude(q.magnitude.toInt)))
      .filter({
        case (loc, magnitude) ⇒
          magnitude.min >= Major.min
      })
      .take(3)
      .map(_._2)
      .toBlocking
      .toList should contain atLeastOneOf (Major, Great)
  }

  it should "translate geocode" in {
    Usgs.stream
      .map { quake ⇒
        val country: Future[Country] = reverseGeoCode(quake.location)
        Observable.from(country.map(c ⇒ (quake, c)))
      }
      .flatten
      .filter({
        case (_, Country("America")) ⇒ true
        case _                       ⇒ false
      })
      .take(2)
      .map(_._2.name)
      .toBlocking
      .toList should contain("America")
  }
}
