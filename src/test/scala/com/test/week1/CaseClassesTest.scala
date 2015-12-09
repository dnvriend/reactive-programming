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

class CaseClassesTest extends TestSpec {

  // Case Classes are used to conveniently store and
  // match on the contents of a class. You can construct
  // them without using the 'new' keyword.

  case class Calculator(brand: String, model: String)

  "case classes" should "be created easily" in {
    Calculator("hp", "20b") shouldBe Calculator("hp", "20b")
  }

  // case classes have equality, hashcode and nice toString methods
  // based on the constructor arguments

  "case classes" should "have nice toString method" in {
    Calculator("hp", "20b").toString shouldBe "Calculator(hp,20b)"
  }

  it should "have nice equality method" in {
    val hp20b = Calculator("hp", "20b")
    val hp20B = Calculator("hp", "20b")
    hp20b shouldBe hp20B
  }

  // case classes can have methods just like normal classes.

  case class CalulcatorWithMethod(brand: String, model: String) {
    def hello: String = s"Hello I am the $brand/$model"
  }

  "case classes" should "be able to have methods" in {
    CalulcatorWithMethod("hp", "20b").hello shouldBe "Hello I am the hp/20b"
  }

  // case classes are designed to be used with pattern matching.

  trait CalcType
  case object Financial extends CalcType
  case object Scientific extends CalcType
  case object Business extends CalcType
  case class Unknown(message: String) extends CalcType

  def calcType(calc: Calculator): CalcType = calc match {
    case Calculator("hp", "20B")  ⇒ Financial
    case Calculator("hp", "48G")  ⇒ Scientific
    case Calculator("hp", "30B")  ⇒ Business
    case Calculator(brand, model) ⇒ Unknown(s"Calculator $brand, $model is of unknown type")
  }

  "case classes" should "be used with pattern matching" in {
    calcType(Calculator("hp", "20B")) shouldBe Financial
    calcType(Calculator("hp", "48G")) shouldBe Scientific
    calcType(Calculator("hp", "30B")) shouldBe Business
    calcType(Calculator("hp", "NW280AA")) shouldBe Unknown("Calculator hp, NW280AA is of unknown type")
  }
}
