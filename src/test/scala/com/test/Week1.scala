package com.test

class Week1 extends TestSpec {

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
    "address" ->JObj(Map(
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
    case JSeq(elems) => "[" + (elems map show mkString ",") + "]"
    case JObj(bindings) =>
      val assoc = bindings map {
        case (key, value) => "\"" + key + "\": " + show(value)
      }
      "{" + (assoc mkString ", ") + "}"

    case JNum(num) => num.toString
    case JStr(str) => "\"" + str + "\""
    case JBool(b) => b.toString
    case JNull => "null"
  }

  "data" should "be json" in {
    show(data) shouldBe """{"firstName": "John", "lastName": "Smith", "address": {"street": "21 2nd Street", "state": "NY", "postalCode": 10021.0}, "phoneNumbers": [{"type": "Home", "number": "212 555-1234"},{"type": "fax", "number": "646 555-4567"}]}"""
  }

  "A function" should "be applied" in {
    // the literal String => String is a Function1[String, String]
    // this type can only be applied, because it is a Function,
    // but, when the function is not defined, scala thows a match error
    val f: String => String = { case "ping" => "pong" }
    f("ping") shouldBe "pong"
    intercept[MatchError] {
      f("foo")
    }
  }

  "A partial function" should "be applied" in {
    // there is no partial function literal
    // the partial function has the isDefinedAt method
    // so it can be checked
    val f: PartialFunction[String, String] = { case "ping" => "pong" }
    f.isDefinedAt("ping") shouldBe true
    f.isDefinedAt("foo") shouldBe false
    // it still throws a match error when applied though...
    intercept[MatchError] {
      f("foo")
    }
  }

  "A map" should "be a function" in {
    // a map is a function, thus is should be able to be applied
    val map = Map("1" -> "Foo", "2" -> "Bar")
    map("1") shouldBe "Foo"
    map("2") shouldBe "Bar"
    intercept[NoSuchElementException] {
      map("3") shouldBe "Bar"
    }
  }

  "A seq" should "be a function" in {
    // a seq is also a function, thus .. oh well
    val xs = Seq(1, 2)
    xs(0) shouldBe 1
    xs(1) shouldBe 2
    intercept[IndexOutOfBoundsException] {
      xs(2) shouldBe 3
    }
  }

  "More complex partial function" should "be applied" in {
    val f: PartialFunction[List[Int], String] = {
      case Nil => "empty"
      case first :: second :: tail => s"$first::$second::$tail"
    }

    // f is now a PartialFunction, which can be applied with a List[Int], lets do so
    f(Nil) shouldBe "empty"
    f(List(1, 2, 3)) shouldBe "1::2::List(3)"
    f(List(1, 2, 3, 4)) shouldBe "1::2::List(3, 4)"
  }

  "Nested matching" should "be applied" in {
    val f: PartialFunction[List[Int], String] = {
      case Nil => "empty"
      case head :: tail => tail match {
          case Nil => "empty"
        }
    }

    // let's apply this partial function
    f.isDefinedAt(List(1, 2, 3)) shouldBe true
    // but when actually applied...
    intercept[MatchError] {
      f(List(1, 2, 3))
    }
  }
}
