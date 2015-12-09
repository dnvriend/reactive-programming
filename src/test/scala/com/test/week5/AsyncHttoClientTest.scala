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

package com.test.week5

import com.github.dnvriend.HttpClient._
import com.github.dnvriend.HttpUtils._
import com.github.dnvriend.HttpClient
import com.test.TestSpec

class AsyncHttoClientTest extends TestSpec {

  "AsyncHttpClient" should "get info from google" in {
    HttpClient()
      .get("http://www.google.nl")
      .links
      .futureValue
      .value should not be empty
  }

  it should "print all to sysout" in {
    HttpClient()
      .get("http://www.google.nl")
      .links
      .futureValue
      .value
      .foreach(println)
  }
}
