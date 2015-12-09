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

package com.github.dnvriend

import java.util.concurrent.Executor

import com.ning.http.client.AsyncHttpClient
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import scala.collection.JavaConverters._
import scala.concurrent.{ Promise, ExecutionContext, Future }
import scala.util.Try

object HttpClient {
  def apply(): AsyncHttpClient = new AsyncHttpClient

  implicit class HttpClientToScala(client: AsyncHttpClient) {
    def get(url: String)(implicit ec: Executor): Future[String] = {
      val f = client.prepareGet(url).execute
      val p = Promise[String]()
      f.addListener(new Runnable {
        override def run(): Unit = {
          val response = f.get
          if (response.getStatusCode < 400)
            p.success(response.getResponseBodyExcerpt(131072))
          else p.failure(new RuntimeException(s"BadStatus: ${response.getStatusCode}"))
        }
      }, ec)
      p.future
    }
  }
}

object HttpUtils {
  implicit class FindLinksFuture(self: Future[String])(implicit ec: ExecutionContext) {
    def links: Future[Option[Iterator[String]]] =
      self.map(body ⇒ findLinks(body))
  }

  def findLinks(body: String): Option[Iterator[String]] =
    Try(Jsoup.parse(body)).map { (document: Document) ⇒
      val links: Elements = document.select("a[href]")
      for (link ← links.iterator().asScala; if link.absUrl("href").startsWith("http://")) yield link.absUrl("href")
    }.toOption
}
