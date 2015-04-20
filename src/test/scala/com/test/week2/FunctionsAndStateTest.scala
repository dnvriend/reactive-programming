package com.test.week2

class FunctionsAndStateTest {
  // the function iterate has three parameters,
  // * n, that is of type Int
  // * f: A function from Int => Int
  // * x: that is of type Int
  def iterate(n: Int, f: Int => Int, x: Int): Int =
    if(n == 0) x else iterate(n-1, f, f(x))

  // the function square, that is of type Int => Int
  // I know, it's a method, but methods can be converted
  // to Functions by the Scala compiler
  def square(x: Int) = x * x
}
