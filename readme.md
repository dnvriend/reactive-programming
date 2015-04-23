# Principles of Reactive Programming
The basic principle of reactive programming is: `Reacting to sequence of events that happen in time`, 
and according to the [Reactive Manifesto](http://www.reactivemanifesto.org/), using these patterns to
build software systems that are more robust, more resilient, more flexible and better positioned to meet modern demands.

# General
- The [Scala Source Code on GitHub](https://github.com/scala/scala)
- The [Scala API Docs for 2.11.6](http://www.scala-lang.org/files/archive/api/2.11.6/#package)

# Scalaz
- [Scalaz - Scalaz](https://github.com/scalaz/scalaz) a Scala library for functional programming.
- [Learning Scalaz](http://eed3si9n.com/learning-scalaz/)

# GitHub Markdown
- [Markdown Cheatsheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)

# Week 1
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

# Week 2
I would seriously advice reading / viewing the resources below to get a good idea on what 
[Functional Reactive Programming](http://en.wikipedia.org/wiki/Functional_reactive_programming) is. The model we use 
this week is push based, in which systems take events and push them through a 'signal' network to achieve a result. The basic
idea of FRP that we focus on this week is that events are combined into 'signals' that always have a current value, but change discretely.
The changes are event-driven. But instead of having an event handler that returns Unit, (like the onClick handler and such), we return
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

## Documentation
- [What is the difference between view, stream and iterator?](http://docs.scala-lang.org/tutorials/FAQ/stream-view-iterator.html)
- [Wikipedia - Functional Reactive Programming](http://en.wikipedia.org/wiki/Functional_reactive_programming)
- [Deprecating the Observer Pattern - Odersky / Maier](http://infoscience.epfl.ch/record/176887/files/DeprecatingObservers2012.pdf)
- [Stackoverflow - What happened to scala.react?](http://stackoverflow.com/questions/21546456/what-happened-to-scala-react)

## Books
- [Reactive Design Patterns - Kuhn](http://manning.com/kuhn/) - [Chapter 1 (PDF)](http://manning.com/kuhn/RDP_meap_CH01.pdf)
- [Functional Reactive Programming - Blackheath](http://www.manning.com/blackheath/) - [Chapter 1 (PDF)](http://www.manning.com/blackheath/FPR_MEAP_ch1.pdf)
- [Reactive Web Applications with Play - Bernhardt](http://www.manning.com/bernhardt/) - [Chapter 1 (PDF)](http://www.manning.com/bernhardt/RWAwithPlay_MEAP_ch01.pdf)
- [Reactive Application Development - Devore](http://www.manning.com/devore/) - [Chapter 1 (PDF)](http://www.manning.com/devore/RAD_MEAP_ch1.pdf)
- [Functional and Reactive Domain Modeling - Ghosh](http://www.manning.com/ghosh2/) - [Chapter 1 (PDF)](http://www.manning.com/ghosh2/FRDM_MEAP_CH01.pdf)

## Video
- [An Introduction to Functional Reactive Programming](https://www.youtube.com/watch?v=ZOCCzDNsAtI)
- [Functional Reactive Programming in Elm - Evan Czaplicki](https://www.youtube.com/watch?v=JreO-Kl0Ed4)

