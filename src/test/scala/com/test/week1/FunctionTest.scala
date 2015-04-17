package com.test.week1

import com.test.TestSpec

class FunctionTest extends TestSpec {

  "Scala function literal" should "be of the following types" in {
    // all three are the same
    val f0_1: Function0[Unit] = new Function0[Unit] {
      override def apply(): Unit = Unit
    }
    val f0_2: Function0[Unit] = () => Unit
    val f0_3: Unit = () => Unit

    // all three are the same
    val f1_1: Function1[Unit, Unit] = new Function1[Unit, Unit] {
      override def apply(x: Unit): Unit = Unit
    }
    val f1_2: Function1[Unit, Unit] = (x: Unit) => Unit
    val f1_3: (Unit => Unit) = (x: Unit) => Unit

    // all three are the same
    val f2_1: Function1[Int, Unit] = new Function1[Int, Unit] {
      override def apply(x: Int): Unit = Unit
    }
    val f2_2: Function1[Int, Unit] = (x: Int) => Unit
    val f2_3: (Int => Unit) = (x: Int) => Unit

    // all three are the same
    val f3_1: Function[Int, Int] = new Function1[Int, Int] {
      override def apply(x: Int): Int = x
    }
    val f3_2: Function1[Int, Int] = (x: Int) => x
    val f3_3: Int => Int = (x: Int) => x

    // all three are the same
    val f4_1: Function2[Int, Int, Unit] = new Function2[Int, Int, Unit] {
      override def apply(x: Int, y: Int): Unit = Unit
    }
    val f4_2: Function2[Int, Int, Unit] = (x: Int, y: Int) => Unit
    val f4_3: (Int, Int) => Unit = (x: Int, y: Int) => Unit

    // all three are the same
    val f5_1: Function2[Int, Int, Int] = new Function2[Int, Int, Int] {
      override def apply(x: Int, y: Int): Int = x + y
    }
    val f5_2: Function2[Int, Int, Int] = (x: Int, y: Int) => x + y
    val f5_3: (Int, Int) => Int = (x: Int, y: Int) => x + y

    // all three are the same
    val f6_1: Function3[Int, Int, Int, Unit] = new Function3[Int, Int, Int, Unit] {
      override def apply(x: Int, y: Int, z: Int): Unit = Unit
    }
    val f6_2: Function3[Int, Int, Int, Unit] = (x: Int, y: Int, z: Int) => Unit
    val f6_3: (Int, Int, Int) => Unit = (x: Int, y: Int, z: Int) => Unit
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