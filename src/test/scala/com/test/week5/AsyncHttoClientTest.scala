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
