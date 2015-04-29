# Principles of Reactive Programming
The basic principle of reactive programming is: `Reacting to sequence of events that happen in time`, 
and according to the [Reactive Manifesto](http://www.reactivemanifesto.org/), using these patterns to
build software systems that are more robust, more resilient, more flexible and better positioned to meet modern demands.

[![Build Status](https://travis-ci.org/dnvriend/reactive-programming.svg)](https://travis-ci.org/dnvriend/reactive-programming)

# Typesafe
- [Typesafe - Going Reactive in Java with Typesafe Reactive Platform](https://www.youtube.com/watch?v=y70Z5S2eSIo)
- [Typesafe - Deep Dive into the Typesafe Reactive Platform - Akka and Scala](https://www.youtube.com/watch?v=fMWzKEN6uTY)
- [Typesafe - Deep Dive into the Typesafe Reactive Platform - Activator and Play](https://www.youtube.com/watch?v=EJl9mQ0051g)
- [Typesafe - What Have The Monads Ever Done For Us with Dick Wall](https://www.youtube.com/watch?v=2IYNPUp751g)
- [Typesafe - Deep Dive into the Typesafe Reactive Platform - Ecosystem and Tools](https://www.youtube.com/watch?v=3nNerwsqrQI)
- [A Playful Introduction to Rx - Erik Meijer](https://www.youtube.com/watch?v=WKore-AkisY)
- [RxJava: Reactive Extensions in Scala](https://www.youtube.com/watch?v=tOMK_FYJREw)
- [What does it mean to be Reactive? - Erik Meijer](https://www.youtube.com/watch?v=sTSQlYX5DU0)

# General
- The [Scala Source Code on GitHub](https://github.com/scala/scala)
- The [Scala API Docs for 2.11.6](http://www.scala-lang.org/files/archive/api/2.11.6/#package)

# Scalaz
- [Scalaz - Scalaz](https://github.com/scalaz/scalaz) a Scala library for functional programming.
- [Learning Scalaz](http://eed3si9n.com/learning-scalaz/)

# Stream Processing
> Stream processing is a different paradigm to the Actor Model or to Future composition, therefore it may take some careful study of this subject until you feel familiar with the tools and techniques.
-- <cite>Akka Streams Documentation</cite>

- [Akka Streams Documentation 1.0-RC1](http://doc.akka.io/docs/akka-stream-and-http-experimental/1.0-RC1/scala.html)
- [Quick Start - Reactive Tweets](http://doc.akka.io/docs/akka-stream-and-http-experimental/1.0-RC1/scala/stream-quickstart.html#stream-quickstart-scala)
- [Akka Streams API 1.0-RC1](http://doc.akka.io/api/akka-stream-and-http-experimental/1.0-RC1/)
- [Design Principles behind Reactive Streams](http://doc.akka.io/docs/akka-stream-and-http-experimental/1.0-RC1/stream-design.html#stream-design)
- [Streams Cookbook](http://doc.akka.io/docs/akka-stream-and-http-experimental/1.0-RC1/scala/stream-cookbook.html#stream-cookbook-scala)
- [Overview of built-in stages and their semantics](http://doc.akka.io/docs/akka-stream-and-http-experimental/1.0-RC1/stages-overview.html#stages-overview)
- [Reactive Streams](http://www.reactive-streams.org/)

# GitHub Markdown
- [Markdown Cheatsheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)

# Parleys
- [Scala Days 2015 - San Fransisco](https://www.parleys.com/channel/scala-days-san-francisco-2015)
- [Scala Days 2014 - Berlin](https://www.parleys.com/channel/scala-days-2014)
- [Scala Days 2013 - New York](https://www.parleys.com/channel/scaladays-2013)

# Week 3 - Futures and Composition

## Documentation
- [Scala - Async await](https://github.com/scala/async)
- [Scala - Futures](http://docs.scala-lang.org/overviews/core/futures.html)
- [Akka  - Futures](http://doc.akka.io/docs/akka/2.3.10/scala/futures.html)
- [The Neophyte's Guide to Scala Part 8 - Welcome to the Future](http://danielwestheide.com/blog/2013/01/09/the-neophytes-guide-to-scala-part-8-welcome-to-the-future.html)
- [The Neophyte's Guide to Scala Part 9 - Promises and Futures in Practice](http://danielwestheide.com/blog/2013/01/16/the-neophytes-guide-to-scala-part-9-promises-and-futures-in-practice.html)
- [Ian Irvine](https://github.com/iirvine) - [Notes (2014) - Monads and Effects](https://github.com/iirvine/principles-of-reactive-programming/blob/master/notes/week-3/001-monads-and-effects.md)
- [Ian Irvine](https://github.com/iirvine) - [Notes (2014) - Latency as an Effects](https://github.com/iirvine/principles-of-reactive-programming/blob/master/notes/week-3/002-latency-as-an-effect.md)
- [Ian Irvine](https://github.com/iirvine) - [Notes (2014) - Combinators on Futures](https://github.com/iirvine/principles-of-reactive-programming/blob/master/notes/week-3/003-combinators-on-futures.md)
- [Ian Irvine](https://github.com/iirvine) - [Notes (2014) - Composing Futures](https://github.com/iirvine/principles-of-reactive-programming/blob/master/notes/week-3/004-composing-futures.md)
- [Ian Irvine](https://github.com/iirvine) - [Notes (2014) - Promises](https://github.com/iirvine/principles-of-reactive-programming/blob/master/notes/week-3/005-promises.md)

## Video
- [Promise of the Futures](https://www.youtube.com/results?search_query=scala+futures)
- [Composable Futures with Akka 2.0 - Mike Slinn](https://www.youtube.com/watch?v=VCattsfHR4o)

# Week 2 - Functional Reactive Programming
I would advice reading / viewing the resources below to get a good idea on what 
[Functional Reactive Programming](http://en.wikipedia.org/wiki/Functional_reactive_programming) is. The model we use 
this week is push based, in which systems take events and push them through a 'signal' network to achieve a result. The basic
idea of FRP that we focus on this week is that events are combined into 'signals' that always have a current value, but change discretely.
The changes are event-driven. But instead of having an event handler that returns Unit, (like the onClick handler and such), it returns
a value. 

FRP in a nutshell (for now at least):

When we do an assignment in Scala, the following happens:

```scala
scala> var a = 1
a: Int = 1

scala> var b = 2
b: Int = 2

scala> var c = a + b
c: Int = 3

scala> a = 2
a: Int = 2

scala> c
res1: Int = 3

scala> var c = a + b
c: Int = 4
```

As we can see, the value of `c` did not change, when we changed the value of `a` from `1` to `2`. This is normal behavior
because we have expressed the relationship at one point in the execution of the program. 

But what if, `c` would change when we changed the value of a dependent value like `a`. This would mean that there is a 
`dependency` created between `c`, `a` and `b` that expresses how these values will relate over time. So the basic idea is 
that `c` will change when we change either `a` and/or `b`.

## Hint 1: TweetText
The following should work:

```scala
 def tweetRemainingCharsCount(tweetText: Signal[String]): Signal[Int] =
    Signal(MaxTweetLength - tweetLength(tweetText()))

  def colorForRemainingCharsCount(remainingCharsCount: Signal[Int]): Signal[String] =
    Signal {
      remainingCharsCount() match {
        case count if (0 to 14).contains(count) => "orange"
        case count if count < 0 => "red"
        case _ => "green"
      }
    }
```

## Hint 2: Polynomal
Please first try it yourself, then if you wish, verify.

```scala
  def computeDelta(a: Signal[Double], b: Signal[Double], c: Signal[Double]): Signal[Double] =
  Signal {
    Math.pow(b(), 2) - (4 * a() * c())
  }

  def computeSolutions(a: Signal[Double], b: Signal[Double], c: Signal[Double], delta: Signal[Double]): Signal[Set[Double]] =
    Signal {
      delta() match {
        case discriminant if discriminant < 0 => Set()
        case discriminant if discriminant == 0 => Set(calcLeft(a(), b(), c()))
        case discriminant => Set(calcLeft(a(), b(), c()), calcRight(a(), b(), c()))
      }
    }

  def calcLeft(a: Double, b: Double, c: Double): Double =
    (-1 * b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a)

  def calcRight(a: Double, b: Double, c: Double): Double =
    (-1 * b - Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a)
```

## Hint 3: Calculator
Please first try it yourself, then if you wish, verify.

```scala
  def computeValues(namedExpressions: Map[String, Signal[Expr]]): Map[String, Signal[Double]] = {
    namedExpressions.mapValues { expr =>
      Signal(eval(expr(), namedExpressions))
    }
  }

  def eval(expr: Expr, references: Map[String, Signal[Expr]]): Double = {
    expr match {
      case Literal(v) => v
      case Ref(name) => eval(getReferenceExpr(name, references), references - name)
      case Plus(aExpr, bExpr)   => eval(aExpr, references) + eval(bExpr, references)
      case Minus(aExpr, bExpr)  => eval(aExpr, references) - eval(bExpr, references)
      case Times(aExpr, bExpr)  => eval(aExpr, references) * eval(bExpr, references)
      case Divide(aExpr, bExpr) => eval(aExpr, references) / eval(bExpr, references)
      case _ => Double.MaxValue
    }
  }

  /** Get the Expr for a referenced variables.
   *  If the variable is not known, returns a literal NaN.
   */
  private def getReferenceExpr(name: String, references: Map[String, Signal[Expr]]): Expr = {
    references.get(name).fold[Expr](Literal(Double.NaN)) {
      exprSignal => exprSignal()
    }
  }
```

## Documentation
- [What is the difference between view, stream and iterator?](http://docs.scala-lang.org/tutorials/FAQ/stream-view-iterator.html)
- [Wikipedia - Functional Reactive Programming](http://en.wikipedia.org/wiki/Functional_reactive_programming)
- [Functional Reactive Animation - Elliott / Hudak (PDF)](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.52.2850&rep=rep1&type=pdf)
- [Deprecating the Observer Pattern - Odersky / Maier (PDF)](http://infoscience.epfl.ch/record/176887/files/DeprecatingObservers2012.pdf)
- [Stackoverflow - What happened to scala.react?](http://stackoverflow.com/questions/21546456/what-happened-to-scala-react)

## FRP Libraries
- [scala.frp](https://github.com/dylemma/scala.frp) - [Dylan Halperin](https://github.com/dylemma) 
- [scala.react](https://github.com/dylemma/scala.react) - [Dylan Halperin](https://github.com/dylemma)
- [React4J](https://bitbucket.org/yann_caron/react4j/wiki/Home) - [Yann Caron](https://bitbucket.org/yann_caron)

## Books
- [Reactive Design Patterns - Kuhn](http://manning.com/kuhn/) - [Chapter 1 (PDF)](http://manning.com/kuhn/RDP_meap_CH01.pdf)
- [Functional Reactive Programming - Blackheath](http://www.manning.com/blackheath/) - [Chapter 1 (PDF)](http://www.manning.com/blackheath/FPR_MEAP_ch1.pdf)
- [Reactive Web Applications with Play - Bernhardt](http://www.manning.com/bernhardt/) - [Chapter 1 (PDF)](http://www.manning.com/bernhardt/RWAwithPlay_MEAP_ch01.pdf)
- [Reactive Application Development - Devore](http://www.manning.com/devore/) - [Chapter 1 (PDF)](http://www.manning.com/devore/RAD_MEAP_ch1.pdf)
- [Functional and Reactive Domain Modeling - Ghosh](http://www.manning.com/ghosh2/) - [Chapter 1 (PDF)](http://www.manning.com/ghosh2/FRDM_MEAP_CH01.pdf)

## Video
- [An Introduction to Functional Reactive Programming](https://www.youtube.com/watch?v=ZOCCzDNsAtI)
- [Functional Reactive Programming in Elm - Evan Czaplicki](https://www.youtube.com/watch?v=JreO-Kl0Ed4)
- [Building Reactive Apps](https://www.youtube.com/watch?v=AFqFXlKrwRc) - [James Ward](http://www.jamesward.com/)

# Week 1 - Functional Programming
- What does [invariant](http://en.wikipedia.org/wiki/Invariant_%28mathematics%29) mean?
- [The Neophyte's Guide to Scala Part 12 - Type Classes](http://danielwestheide.com/blog/2013/02/06/the-neophytes-guide-to-scala-part-12-type-classes.html)
- [Learn yourself Haskell - Functors, Applicative Functors and Monoids](http://learnyouahaskell.com/functors-applicative-functors-and-monoids)
- [Learn yourself Haskell - A fistful of Monads](http://learnyouahaskell.com/a-fistful-of-monads)

## Video
- What is a [Priority Queue](https://www.youtube.com/watch?v=QJ_7S1p0Kj8)?
- What is a [Binary Heap](https://www.youtube.com/watch?v=cEY_JAm7L_o)?
- Algorithms with Attitude - [Introduction to Binary Heaps](https://www.youtube.com/watch?v=WCm3TqScBM8)
- Algorithms with Attitude - [Binary Heaps for Priority Queues](https://www.youtube.com/watch?v=-WEku8ZnynU)
- Algorithms with Attitude - [Optimized Heapify](https://www.youtube.com/watch?v=uuzVCZ-0rr8)
- Algorithms with Attitude - [HeapSort](https://www.youtube.com/watch?v=onlhnHpGgC4)
- Algorithms with Attitude - [Lineair Time BuildHeap](https://www.youtube.com/watch?v=BlMQVkakxtE)
- [Typeclasses in Scala with Dan Rosen](https://www.youtube.com/watch?v=sVMES4RZF-8) 

## Example source code
- [Vladimir Kostyukov's Scalacaster](https://github.com/vkostyukov/scalacaster): algorithms and data structures in Scala

## Test frameworks
- [ScalaTest](http://www.scalatest.org/)
- [ScalaCheck](http://scalacheck.org/)
- [Generator-driven property checks](http://www.scalatest.org/user_guide/generator_driven_property_checks)

## Hints
### Hint 1
For testing, you want to insert two values into the `heap`, for example with:

```scala
forAll { (x: Int, y: Int) => 
}
```

When you add the values into the heap and search for the minimum `findMin`, it would be handy
to know whether `x` or `y` is the smallest, the following will help:

```scala
scala> def order = scala.math.Ordering.Int
order: math.Ordering.Int.type

scala> order.min(2,1)
res0: Int = 1
```

The heap has the method `ord` that does the same.

### Hint 2
To generate a heap, you can use the following code:

```scala
lazy val genHeap: Gen[H] = for {
    n <- arbitrary[Int]
    h <- oneOf(empty, genHeap)
  } yield insert(n, h)
```

### Hint 3
Did you notice the following? We have three methods, `isEmpty`, `findMin` and `deleteMin`. When you combine these
methods you can `iterate` over the heap until its empty and put the contents into a list:

```scala
 def heapToList(h: H): List[Int] =
    if(isEmpty(h)) Nil else findMin(h) :: heapToList(deleteMin(h))
```

Lists can be sorted:

```scala
scala> val xs = List(2,3,1,5,6,2,3,4,0,1,2)
xs: List[Int] = List(2, 3, 1, 5, 6, 2, 3, 4, 0, 1, 2)

scala> xs.sorted
res0: List[Int] = List(0, 1, 1, 2, 2, 2, 3, 3, 4, 5, 6)
```

Lists can also be compared:

```scala
scala> List(1, 2) == List(1, 2)
res0: Boolean = true
              
scala> List(1, 2) == List(2, 1)
res1: Boolean = false

scala> List(1, 2) == List(2, 1).sorted
res2: Boolean = true

scala> List(2, 1).sorted == List(2, 1).sorted
res3: Boolean = true
```
