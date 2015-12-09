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

package com.test.week1

import com.test.TestSpec

class JsonTest extends TestSpec {

  abstract class JSON
  case class JSeq(elems: List[JSON]) extends JSON
  case class JObj(bindings: Map[String, JSON]) extends JSON
  case class JNum(num: Double) extends JSON
  case class JStr(str: String) extends JSON
  case class JBool(b: Boolean) extends JSON
  case object JNull extends JSON

  val data = JObj(Map(
    "firstName" -> JStr("John"),
    "lastName" -> JStr("Smith"),
    "address" -> JObj(Map(
      "street" -> JStr("21 2nd Street"),
      "state" -> JStr("NY"),
      "postalCode" -> JNum(10021)
    )),
    "phoneNumbers" -> JSeq(List(
      JObj(Map(
        "type" -> JStr("Home"), "number" -> JStr("212 555-1234")
      )),
      JObj(Map(
        "type" -> JStr("fax"), "number" -> JStr("646 555-4567")
      ))
    ))
  ))

  def show(json: JSON): String = json match {
    case JSeq(elems) ⇒ "[" + (elems map show mkString ",") + "]"
    case JObj(bindings) ⇒
      val assoc = bindings map {
        case (key, value) ⇒ "\"" + key + "\": " + show(value)
      }
      "{" + (assoc mkString ", ") + "}"

    case JNum(num) ⇒ num.toString
    case JStr(str) ⇒ "\"" + str + "\""
    case JBool(b)  ⇒ b.toString
    case JNull     ⇒ "null"
  }

  "data" should "be json" in {
    show(data) shouldBe """{"firstName": "John", "lastName": "Smith", "address": {"street": "21 2nd Street", "state": "NY", "postalCode": 10021.0}, "phoneNumbers": [{"type": "Home", "number": "212 555-1234"},{"type": "fax", "number": "646 555-4567"}]}"""
  }
}
