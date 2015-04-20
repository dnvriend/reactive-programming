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

  "Functions" should "be able to be applied" in {
    // A function, like the ones above, can be applied. What does that mean?
    // A function like val `f = (x: Int) => x` can be evaluated. This
    // can be done by applying a value to the function.

    // when you evaluate a function by applying a value to that function
    // it looks a lot like math:

    val f: Int => Int = (x: Int) => x
    f(3) shouldBe 3

    // you can skip the type part if you wish:

    val g = (x: Int) => x
    g(3) shouldBe 3
  }

  it should "look like math" in {
    val f = (x: Int) => x * x
    f(2) shouldBe 4

    val g = (x: Int) => x + x
    g(2) shouldBe 4
  }

  it should "be applied in sequence" in {
    val f: Int => Int = (x: Int) => x * x
    val g: Int => Int = (x: Int) => x + x + x
    val h: Int => Int = (x: Int) => g(f(x))
    val i: Int => Int = f andThen g
    val j: Int => Int = f compose g

    // functions can be nested, this infuences the sequence of
    // evaluation which is first 'f' andThen 'g':
    g(f(2)) shouldBe 12

    // the function 'h' does the same as the evaluation on line 91,
    // but it is defined as a function, thus we can easily evaluate it
    // by applying a number to it, like 2:
    h(2) shouldBe 12

    // the function 'i' composes these two functions. `andThen` is actually
    // a method on the 'f' function object, which is of type Function1. This composition
    // will first evaluate f and then evaluate g with the result of 'f', in which it does
    // the same as line 96:
    i(2) shouldBe 12

    // the evaluation can also be reversed by first evaluating g and then f which is what
    // function 'j' does:
    j(2) shouldBe 36
  }

  //
  // Methods as functions, 'ETA Expansion' (automatic coercion)
  //

  "Methods" should "be expanded to functions" in {
    // methods are not functions, they are methods, but they look so much
    // like functions! Can they be used like functions as well?

    // two methods 'sqr' and 'add'
    def sqr(x: Int) = x * x
    def add(x: Int) = x + x + x

    // when we wish to use these methods, in eg. a higher order function, then
    // we can use them like the methods are objects of type Function1[Int, Int],
    // which is clearly not the case as they are methods! But why does the
    // compiler not complain?

    def squareThenAdd(x: Int, f: Int => Int, g: Int => Int): Int = g(f(x))
    squareThenAdd(2, sqr, add) shouldBe 12
  }

  it should "be expanded to functions using the Bridge Pattern" in {
    def add(x: Int) = x + x + x
    def sqr(x: Int) = x * x

    // squareThenAdd only accepts objects of type Function1[Int, Int], so
    // what is going on is that the methods are automatically coerced by the
    // compiler to the type Function1[Int, Int]. To show how this process works,
    // let's manually transform the method to a function:

    def squareThenAdd(x: Int, f: Int => Int, g: Int => Int): Int = g(f(x))

    // create a new object of type Function[Int, Int] and
    // call the sqr method
    val f: Int => Int = new Function1[Int, Int] {
      override def apply(x: Int): Int = sqr(x)
    }

    // create a new object of type Function[Int, Int] and
    // call the add method
    val g: Int => Int = new Function1[Int, Int] {
      override def apply(x: Int): Int = add(x)
    }

    // the squareThenAdd method can be called with the two functions. We now
    // have manually done what the compiler does for us automatically, so
    // please don't do this in your projects! This process is called 'ETA Expansion'.
    //
    // There is more to ETA expansion than this, but now the story is
    // kind-of complete for basic Functions anyway.
    squareThenAdd(2, f, g) shouldBe 12
  }

  //
  // The functions above are always defined, which means they always give an answer,
  // but there are functions, that can also be applied with a value, but only evaluate
  // succesfully for certain inputs, let's look at some.
  //

  "A function" should "can be true for certain inputs" in {
    // the literal String => String is a Function1[String, String]
    // this type can be applied, because it is a Function,
    // but, when the function is not defined, scala thows a match error.
    // for example, the function only has an answer for the input "ping", in
    // which it will return a "pong", but for all other inputs, it will throw a
    // 'Match Error'
    val f: String => String = { case "ping" => "pong" }
    f("ping") shouldBe "pong"
    intercept[MatchError] {
      // evaluate the function by applying 'foo' to it
      f("foo")
    }
  }

  // functions that throw 'MatchErrors' are a pain, wouldn't it be nice to first check
  // whether or not a certain value can be applied to the function? Let's introduce the
  // PartialFunction type, which is a subtype of Function!

  "A partial function" should "be applied" in {
    // Note, there is no partial function literal in Scala. Scala has only
    // the literal '=>' which is shorthand for Function.
    // A partial function has the isDefinedAt method
    // so it can be checked
    val f: PartialFunction[String, String] = { case "ping" => "pong" }
    // we can check whether the PartialFunction is defined for a given input
    // which is a big plus!
    f.isDefinedAt("ping") shouldBe true
    f.isDefinedAt("foo") shouldBe false
    // it still throws a match error when applied though...
    intercept[MatchError] {
      // evaluate the function by applying 'foo' to it
      f("foo")
    }
  }

  //
  // In Scala, collections are also functions, and therefor they can be evaluated:
  //

  "A map" should "be a function" in {
    // a map is a function, thus is should be able to be applied
    val map = Map("1" -> "Foo", "2" -> "Bar")
    // apply '1' and '2' to the function map (strange concept coming from Java)
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
    // 'f' is a PartialFunction that can be applied with 'List[Int]' and
    // returns a String.
    val f: PartialFunction[List[Int], String] = {
      case Nil => "empty"
      case first :: second :: tail => s"$first::$second::$tail"
    }

    // As stated before, the PartialFunction can be applied with a List[Int], lets do so
    f(Nil) shouldBe "empty"
    f(List(1, 2, 3)) shouldBe "1::2::List(3)"
    f(List(1, 2, 3, 4)) shouldBe "1::2::List(3, 4)"
    f.isDefinedAt(Nil) shouldBe true
    f.isDefinedAt(List(1)) shouldBe false
    f.isDefinedAt(List(1, 2)) shouldBe true
  }

  "Nested matching" should "be applied" in {
    // 'f' is a PartialFunction that can be applied with 'List[Int]' and
    // returns a String. Note the !!nested PartialFunction!!, which is only defined
    // when the tail is 'Nil', but when the tail is eg. a List(1, 2, 3) the nested
    // PartialFunction is not defined
    val f: PartialFunction[List[Int], String] = {
      case Nil => "empty"
      case head :: tail => tail match { // nested PartialFunction
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