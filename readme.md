# Principles of Reactive Programming
These are my notes and study guide how I approach studying `Principles of Reactive Programming` from [Coursera](https://class.coursera.org/reactive-002).

[![Build Status](https://travis-ci.org/dnvriend/reactive-programming.svg)](https://travis-ci.org/dnvriend/reactive-programming)

# What is Reactive Programming (RP)?
> The basic principle of reactive programming is: `Reacting to sequence of events that happen in time`, 
> and, using these patterns to, build software systems that are more robust, more resilient, more flexible and 
> better positioned to meet modern demands. -- <cite>[Reactive Manifesto](http://www.reactivemanifesto.org/)</cite>

> In computing, reactive programming is a `programming paradigm oriented around data flows and the propagation of 
> change`. This means that it should be possible to express static or dynamic data flows with ease in the 
> programming languages used, and that the underlying execution model will automatically propagate changes through 
> the data flow. -- <cite>[Wikipedia](http://en.wikipedia.org/wiki/Reactive_programming)</cite>

- [Youtube: Erik Meijer - What does it mean to be Reactive?](https://www.youtube.com/watch?v=sTSQlYX5DU0)
- [Youtube: Dr. Roland Kuhn - Go Reactive at the Trivento Summercamp](https://www.youtube.com/watch?v=auYuWBudVt8)

# Week 5: Akka
> Actors are very lightweight concurrent entities. They process messages asynchronously using an event-driven receive loop. Pattern matching against messages is a convenient way to express an actor's behavior. They raise the abstraction level and make it much easier to write, test, understand and maintain concurrent and/or distributed systems. You focus on workflow—how the messages flow in the system—instead of low level primitives like threads, locks and socket IO.
-- <quote>[Akka.io](http://akka.io)</quote>

## Documentation
- [Akka.io](http://akka.io)

## Video
- [Youtube - Up, Up, and Out: Scaling Software with Akka](https://www.youtube.com/watch?v=GBvtE61Wrto)
- [Youtube - Up And Out Scaling Software With Akka - Jonas Bonér](https://www.youtube.com/watch?v=t4KxWDqGfcs)
- [Youtube - Akka 2.0: Scaling Up & Out With Actors](https://www.youtube.com/watch?v=3jbqTxstlC4)
- [Youtube - Above the Clouds: Introducing Akka](https://www.youtube.com/watch?v=UY3fuHebRMI)
- [Youtube - Deep Dive into the Typesafe Reactive Platform - Akka and Scala - with Nilanjan Raychaudhuri](https://www.youtube.com/watch?v=fMWzKEN6uTY)

# Week 4: Rx: Reactive Extensions
> Users expect real time data. They want their tweets now. Their order confirmed now. They need prices accurate as of now. Their online games need to be responsive. As a developer, you demand fire-and-forget messaging. You don't want to be blocked waiting for a result. You want to have the result `pushed` to you when it is ready. Even better, when working with result sets, you want to receive individual results as they are ready. You do not want to wait for the entire set to be processed before you see the first row. The world has moved to `push`; users are waiting for us to catch up. Developers have tools to `push` data, this is easy. Developers need tools to `react to push data`. 
-- <cite>[Introduction to Rx](http://www.introtorx.com/Content/v1.0.10621.0/01_WhyRx.html#WhyRx)</cite>

> Rx offers a natural paradigm for dealing with sequences of events. A sequence can contain zero or more events. Rx proves to be most valuable when `composing sequences of events`. 
-- <cite>[Introduction to Rx](http://www.introtorx.com/Content/v1.0.10621.0/01_WhyRx.html#WhyRx)</cite>

> You can think of Rx as providing an API similar to Java 8 / Groovy / Scala collections (methods like filter, forEach, map, reduce, zip etc) - but which operates on an asynchronous stream of events rather than a collection. So you could think of Rx as like working with asynchronous `push-based` collections (rather than the traditional synchronous pull based collections). 
-- <cite>[Camel Rx](http://camel.apache.org/rx.html)</cite>

Please note that Rx focusses on `push-based` events. There is no way for the network to go from a `push-based` model to a `pull-based` model like with [Reactive Streams](http://www.reactive-streams.org/), because the network has no notion of an upstream (the demand stream), in which the `subscriber` communicates its demand for data to the `publisher`. With Rx there is only a downwards stream, in which the `publisher` pushes the data-items to the `subscribers`. The [RxJavaReactiveStreams](https://github.com/ReactiveX/RxJavaReactiveStreams) project makes Rx compatible with [Reactive Streams](http://www.reactive-streams.org/).

## Video
- [A Playful Introduction to Rx - Erik Meijer](https://www.youtube.com/watch?v=WKore-AkisY)
- [RxJava: Reactive Extensions in Scala](https://www.youtube.com/watch?v=tOMK_FYJREw)
- [Ben Christensen - "Functional Reactive Programming with RxJava](https://www.youtube.com/watch?v=_t06LRX0DV0)
- [DevCamp 2010 Keynote - Rx: Curing your asynchronous programming blues](http://channel9.msdn.com/Blogs/codefest/DC2010T0100-Keynote-Rx-curing-your-asynchronous-programming-blues)
- [Channel 9 - Rx Workshop: Introduction](http://channel9.msdn.com/Series/Rx-Workshop/Rx-Workshop-Introduction)
- [An Event-driven and Reactive Future - Jonathan Worthington](https://www.youtube.com/watch?v=_VdIQTtRkb8)

## Books
- [Free online - Introduction to Rx](http://www.introtorx.com/Content/v1.0.10621.0/01_WhyRx.html#WhyRx)

## Docs
- [Rx - Operators Reference](http://reactivex.io/documentation/operators.html)
- [RxJava <-> RxScala API](http://reactivex.io/rxscala/comparison.html)
- [ReactiveX - Portal](http://reactivex.io/)
- [RxScala](https://github.com/ReactiveX/RxScala)
- [RxJava](https://github.com/ReactiveX/RxJava)
- [RxJava Wiki](https://github.com/ReactiveX/RxJava/wiki)
- [Microsoft Open Technologies - Rx](https://rx.codeplex.com/)
- [The Rx Observable](http://reactivex.io/documentation/observable.html)
- [Reactive Programming in the Netflix API with RxJava](http://techblog.netflix.com/2013/02/rxjava-netflix-api.html)
- [Lee Campbell - Reactive Extensions for .NET an Introduction](http://leecampbell.blogspot.co.uk/2010/08/reactive-extensions-for-net.html)
- [MSDN - The Reactive Extensions (Rx)...](https://msdn.microsoft.com/en-us/data/gg577609)

## Docs
- [Scala Swing Docs 2.11.1](http://www.scala-lang.org/api/2.11.1/scala-swing/#scala.swing.package)

## Hint 1
The textValues and clicks observables:

```scala
 def textValues: Observable[String] =
      Observable[String]({ subscriber =>
        val eventHandler: PartialFunction[Event, Unit] = {
          case ValueChanged(source) =>
            subscriber.onNext(source.text)
        }
        field.subscribe(eventHandler)
        Subscription {
          field.unsubscribe(eventHandler)
        }
      })  
```

```scala
 def clicks: Observable[Button] =
      Observable[Button] ({ subscriber =>
        val eventHandler: PartialFunction[Event, Unit] = {
          case ButtonClicked(source) =>
            subscriber.onNext(source)
        }
        button.subscribe(eventHandler)
        Subscription {
          button.unsubscribe(eventHandler)
        }
      })
```

## Hint 2
Wikipedia API:

```scala
def sanitized: Observable[String] =
   obs.map(_.replaceAll(" ", "_"))
      
def recovered: Observable[Try[T]] =
   obs.map(Try(_)).onErrorReturn(Failure(_))
   
def timedOut(totalSec: Long): Observable[T] =
  obs.take(totalSec.seconds)

def concatRecovered[S](requestMethod: T => Observable[S]): Observable[Try[S]] =
  obs.flatMap(requestMethod(_).recovered)
```

## Hint 3
WikipediaSuggest.scala

```scala
    val searchTerms: Observable[String] =
      searchTermField.textValues

    val suggestions: Observable[Try[List[String]]] =
      searchTerms.flatMap(wikiSuggestResponseStream).recovered

    val suggestionSubscription: Subscription =
      suggestions.observeOn(eventScheduler).subscribe { (x: Try[List[String]]) =>
        x.map(suggestionList.listData = _)
          .recover { case t: Throwable =>
          status.text = t.getLocalizedMessage
         }
      }

    val selections: Observable[String] =
      button.clicks.flatMap { _ =>
        suggestionList.selection.items.self match {
               case seq: Seq[String] if seq.nonEmpty =>
                Observable.just(seq.head)
               case _ =>
                Observable.empty
          }
      }

    val pages: Observable[Try[String]] =
      selections.flatMap(wikiPageResponseStream).recovered

    val pageSubscription: Subscription =
      pages.observeOn(eventScheduler) subscribe { (x: Try[String]) =>
        x.map (editorpane.text = _)
         .recover { case t: Throwable =>
            status.text = t.getLocalizedMessage
          }
      }
  }
```

# Week 3 - Futures and Composition
> Futures provide a nice way to reason about performing many operations in parallel– in an efficient and 
> non-blocking way. The idea is simple, a Future is a sort of a placeholder object that you can create for a result 
> that does not yet exist. Generally, the result of the Future is computed concurrently and can be later collected. 
> Composing concurrent tasks in this way tends to result in faster, asynchronous, non-blocking parallel code. 
-- <cite>[ScalaDocs](http://docs.scala-lang.org/overviews/core/futures.html)</cite>

## Async-Await
- [Scala - Async await](https://github.com/scala/async)
- [Scala - SIP-22 - Async](http://docs.scala-lang.org/sips/pending/async.html)
- [The Future is not good enough: coding with async/await](http://engineering.roundupapp.co/the-future-is-not-good-enough-coding-with-async-await/)

## Futures
- [Scala - Futures](http://docs.scala-lang.org/overviews/core/futures.html)
- [Akka  - Futures](http://doc.akka.io/docs/akka/2.3.10/scala/futures.html)
- [The Neophyte's Guide to Scala Part 8 - Welcome to the Future](http://danielwestheide.com/blog/2013/01/09/the-neophytes-guide-to-scala-part-8-welcome-to-the-future.html)
- [The Neophyte's Guide to Scala Part 9 - Promises and Futures in Practice](http://danielwestheide.com/blog/2013/01/16/the-neophytes-guide-to-scala-part-9-promises-and-futures-in-practice.html)

## Notes from the previous year
- [Ian Irvine](https://github.com/iirvine) - [Notes (2014) - Monads and Effects](https://github.com/iirvine/principles-of-reactive-programming/blob/master/notes/week-3/001-monads-and-effects.md)
- [Ian Irvine](https://github.com/iirvine) - [Notes (2014) - Latency as an Effects](https://github.com/iirvine/principles-of-reactive-programming/blob/master/notes/week-3/002-latency-as-an-effect.md)
- [Ian Irvine](https://github.com/iirvine) - [Notes (2014) - Combinators on Futures](https://github.com/iirvine/principles-of-reactive-programming/blob/master/notes/week-3/003-combinators-on-futures.md)
- [Ian Irvine](https://github.com/iirvine) - [Notes (2014) - Composing Futures](https://github.com/iirvine/principles-of-reactive-programming/blob/master/notes/week-3/004-composing-futures.md)
- [Ian Irvine](https://github.com/iirvine) - [Notes (2014) - Promises](https://github.com/iirvine/principles-of-reactive-programming/blob/master/notes/week-3/005-promises.md)

## Hint 1: Future combinators
Most combinators are explained in the videos by Eric Meijer. Please view these videos again and implement them in the
nodescala package object. Some combinators are already available in the Future object itself, so if you are lazy, you
can reuse those.

## Hint 2: A future that does not complete
Does a `Promise[T]().future` complete?

## Hint 3: Launching the web server
The timeout looks a whole lot like the `userInterrupted` future structure

## Hint 4: TerminatedRequested Future
Reuse the future `userInterrupted` and `timeout`. When `any` of those fail, the `terminatedRequested` future should fail.

## Hint 5: Unsubscribe from the server
Note that to cancel a Future, you should use the `val subscription: Subscription = Future.run() { (ct: CancellationToken) => }` future construct.  
The `Future.run() { ct => }` construct returns a `Subscription` that can be used to `unsubscribe` from. When you call
`subscription.unsubscribe`, the `CancellationToken`, that is available in the curried function (the `context` if you will), 
that contains the members `isCancelled: Boolean` and `nonCancelled = !isCancelled` properties, can be queried to figure out
whether or not the future has been canceled. 

So the question remains, how does one `unsubscribe`, the thereby cancel all requests that the server handles, when a 
`subscription` is in scope? 

## Hint 6: Creating the response
The `respond` method, that will be called by the `start` method (you should implement both), will stream the result back
using the `exchange`'s `write` method. 

In a loop, you should check whether or not the `token` has been canceled.

In a loop, you should check whether or not the `response` has more Strings; it is an Iterator.

After you're done writing to the `exchange`, please close the stream using `exchange.close()`

## Hint 7: The solution
The solution is:

```scala
private def respond(exchange: Exchange, token: CancellationToken, response: Response): Unit = {
  while(response.hasNext && token.nonCancelled) {
    exchange.write(response.next())
  }
  exchange.close()
}
```

## Hint 8: The start method
Eric Meijer likes the `async-await` construct, because you can use imperative constructs together with async constructs
but still being non-blocking. The `while(ct.nonCanceled)` 'problem' makes it imperative.

## Hint 9: The start method
You should first create a `listener`, then `start` the listener, which returns a `Subscription`. Then you should 
create a cancellable context using the `Future.run() {}` construct. You should then `loop` while the context is nonCanceled,
then you should wait for a nextRequest from the listener, then you should respond
 
## Hint 10: The start method
Return a combined `Subscription` with the `Subscription(subscription1, subscription2)` construct.

## Hint 11: The start method
You can respond by applying the `handler` with the `request`, which gives you a `Response`, and calling the `respond` method.

## Hint 12: The solution
The solution is:

```scala
def start(relativePath: String)(handler: Request => Response): Subscription = {
  val listener = createListener(relativePath)
  val listenerSubscription: Subscription = listener.start()
  val requestSubscription: Subscription = Future.run() { (ct: CancellationToken) =>
    async {
      while (ct.nonCancelled) {
        val (req, exch) = await (listener.nextRequest())
        respond(exch, ct, handler(req))
      }
    }
  }
  Subscription(listenerSubscription, requestSubscription)
}
```

## Hint 13: The solution
You could probably rewrite the solution to:

```scala
def start(relativePath: String)(handler: Request => Response): Subscription = {
  val listener = createListener(relativePath)
  val listenerSubscription: Subscription = listener.start()
  val requestSubscription: Subscription = Future.run() { (ct: CancellationToken) =>
    Future {
      while (ct.nonCancelled) {
        Await.result(listener.nextRequest().map { req =>
          respond(req._2, ct, handler(req._1))
        }, Duration.Inf)
      }
    }
  }
  Subscription(listenerSubscription, requestSubscription)
}
```

or

```scala
def start(relativePath: String)(handler: Request => Response): Subscription = {
  val listener = createListener(relativePath)
  val listenerSubscription: Subscription = listener.start()
  val requestSubscription: Subscription = Future.run() { (ct: CancellationToken) =>
    Future {
      while (ct.nonCancelled) {
        Await.result(listener.nextRequest().map {
          case (req: Request, exch: Exchange ) => respond(exch, ct, handler(req))
        }, Duration.Inf)
      }
    }
  }
  Subscription(listenerSubscription, requestSubscription)
}
```

## Video
- [Promise of the Futures](https://www.youtube.com/results?search_query=scala+futures)
- [Composable Futures with Akka 2.0 - Mike Slinn](https://www.youtube.com/watch?v=VCattsfHR4o)

# Week 2 - Functional Reactive Programming
> Functional reactive programming (FRP) is a programming paradigm for reactive programming (asynchronous dataflow 
> programming) using the building blocks of functional programming (e.g. map, reduce, filter). FRP has been used for 
> programming graphical user interfaces (GUIs), robotics, and music, aiming to simplify these problems by explicitly > modeling time. 
-- <cite>[Wikipedia](http://en.wikipedia.org/wiki/Functional_reactive_programming)</cite>

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
- [Brian Beckman - Don't fear the Monad](https://www.youtube.com/watch?v=ZhuHCtR3xq8)
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

# Akka

## Documentation
- [The Akka Roadmap](https://docs.google.com/document/d/18W9-fKs55wiFNjXL9q50PYOnR7-nnsImzJqHOPPbM4E/pub)
- [Akka News](http://akka.io/news/)
- [Akka 2.3 -> 2.4 Migration Guide](http://doc.akka.io/docs/akka/snapshot/project/migration-guide-2.3.x-2.4.x.html)

# Hystrix
> Hystrix is a latency and fault tolerance library designed to isolate points of access to remote systems, services and 3rd party libraries, stop cascading failure and enable resilience in complex distributed systems where failure is inevitable.
-- <cite>[Hystrix - GitHub](https://github.com/Netflix/Hystrix)</cite>

> Applications in complex distributed architectures have dozens of dependencies, each of which will inevitably fail at some point. If the host application is not isolated from these external failures, it risks being taken down with them.
-- <cite>[Hystrix Wiki](https://github.com/Netflix/Hystrix/wiki)</cite>

> Hystrix is not about Futures and Promises, it is about bulk-heading and isolating dependencies by limiting concurrent execution, circuit breakers, real time monitoring and metrics. Futures are just a mechanism by which async execution is exposed. Futures by themselves do not provide the same degree of fault-tolerance functionality (though parts of it can be achieved with careful use of timeouts and thread-pool sizing). You could think of Hystrix as a hardened extension of a Future.
-- <cite>[Google Groups](https://groups.google.com/forum/#!topic/hystrixoss/jAL3tV9lc30)</cite>

Note; it basically focusses on the [Resilient](http://www.reactivemanifesto.org/) part of Reactive applications.

## Documentation
- [Netflix - Introducing Hystrix for Resilience Engineering](http://techblog.netflix.com/2012/11/hystrix.html)
- [Enonic - Resilience with Hystrix](http://labs.enonic.com/articles/resilience-with-hystrix)
- [Ben Christensen - Application Resilience in a Service-Oriented Architecture using Hystrix](http://benjchristensen.com/2013/06/10/application-resilience-in-a-service-oriented-architecture-using-hystrix/)
- [Ben Christensen - Application Resilience Engineering and Operations at Netflix with Hystrix - JavaOne 2013](https://speakerdeck.com/benjchristensen/application-resilience-engineering-and-operations-at-netflix-with-hystrix-javaone-2013)

# GitHub Markdown
- [Markdown Cheatsheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)

# Parleys
- [Scala Days 2015 - San Fransisco](https://www.parleys.com/channel/scala-days-san-francisco-2015)
- [Scala Days 2014 - Berlin](https://www.parleys.com/channel/scala-days-2014)
- [Scala Days 2013 - New York](https://www.parleys.com/channel/scaladays-2013)

# Number of students enrolled
On 2015-04-28 there were `23,200` students enrolled, which is roughly `12,000` less than last time (2014 edition). 

# Scala
- The [Scala Source Code on GitHub](https://github.com/scala/scala)
- The [Scala API Docs for 2.11.6](http://www.scala-lang.org/files/archive/api/2.11.6/#package)

# Scalaz
- [Scalaz - Scalaz](https://github.com/scalaz/scalaz) a Scala library for functional programming.
- [Learning Scalaz](http://eed3si9n.com/learning-scalaz/)
