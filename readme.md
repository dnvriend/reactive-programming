# Principles of Reactive Programming
The examples for each week and some try-outs..

# Week 1
## Hint 1
For testing, you want to insert two values into the heap, for example with:

```scala
forAll { (x: Int, y: Int) => 
}
```

When you add the values into the heap and search for the minimum (findMin), it would be handy
to know whether x or y is the smallest, the following will help:

```scala
scala> def order = scala.math.Ordering.Int
order: math.Ordering.Int.type

scala> order.min(2,1)
res0: Int = 1
```

The heap has the method `ord` that does the same.

## Hint 2
Did you notice the following:

* 


# Week 2


I get my information from a whole lot of resources, I will share it here:

# Documentation
- The [Scala Source Code on GitHub](https://github.com/scala/scala)
- The [Scala API Docs for 2.11.6](http://www.scala-lang.org/files/archive/api/2.11.6/#package)
- What does [invariant](http://en.wikipedia.org/wiki/Invariant_%28mathematics%29) mean?
- [The Neophyte's Guide to Scala Part 12 - Type Classes](http://danielwestheide.com/blog/2013/02/06/the-neophytes-guide-to-scala-part-12-type-classes.html)
- [Learn yourself Haskell - Functors, Applicative Functors and Monoids](http://learnyouahaskell.com/functors-applicative-functors-and-monoids)
- [Learn yourself Haskell - A fistful of Monads](http://learnyouahaskell.com/a-fistful-of-monads)

# Video resources
- What is a [Priority Queue](https://www.youtube.com/watch?v=QJ_7S1p0Kj8)?
- What is a [Binary Heap](https://www.youtube.com/watch?v=cEY_JAm7L_o)?
- Algorithms with Attitude - [Introduction to Binary Heaps](https://www.youtube.com/watch?v=WCm3TqScBM8)
- Algorithms with Attitude - [Binary Heaps for Priority Queues](https://www.youtube.com/watch?v=-WEku8ZnynU)
- Algorithms with Attitude - [Optimized Heapify](https://www.youtube.com/watch?v=uuzVCZ-0rr8)
- Algorithms with Attitude - [HeapSort](https://www.youtube.com/watch?v=onlhnHpGgC4)
- Algorithms with Attitude - [Lineair Time BuildHeap](https://www.youtube.com/watch?v=BlMQVkakxtE)
- [Typeclasses in Scala with Dan Rosen](https://www.youtube.com/watch?v=sVMES4RZF-8) 

# Example source code
- [Vladimir Kostyukov's Scalacaster](https://github.com/vkostyukov/scalacaster): algorithms and data structures in Scala

# Test frameworks
- [ScalaTest](http://www.scalatest.org/)
- [ScalaCheck](http://scalacheck.org/)
- [Generator-driven property checks](http://www.scalatest.org/user_guide/generator_driven_property_checks)

# Scalaz
- [Scalaz - Scalaz](https://github.com/scalaz/scalaz) a Scala library for functional programming.
- [Learning Scalaz](http://eed3si9n.com/learning-scalaz/)