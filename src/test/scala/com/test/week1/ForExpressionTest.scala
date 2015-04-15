package com.test.week1

import com.test.TestSpec

class ForExpressionTest extends TestSpec {
  case class Person(name: String, isMale: Boolean, children: Person*)

  val lara = Person("Lara", isMale = false)
  val bob = Person("Bob", isMale = true)
  val julie = Person("Julie", isMale = false, lara, bob)
  val persons = List(lara, bob, julie)

  "for expression" should "query users" in {
    // find out the names of all pairs of mothers and their children
    // in the list persons

    // persons is a generator, thus for each person
    // test if the person is a male
    // if not, loop over children
    // yield a pair which contains the name of the mother
    // and the name of the child
    type MotherAndChildName = (String, String)
    val xs: List[MotherAndChildName] =
      for { p <- persons
          if !p.isMale
          c <- p.children
      } yield (p.name, c.name)
    xs shouldBe List(("Julie","Lara"), ("Julie", "Bob"))
  }

  case class Book(title: String, authors: String*)
  val books: List[Book] =
    List(
      Book(
        "Structure and Interpretation of Computer Programs",
        "Abelson, Harold", "Sussman, Gerald J."
      ),
      Book(
        "Principles of Compiler Design",
        "Aho, Alfred", "Ullman, Jeffrey"
      ),
      Book(
        "Programming in Modula-2",
        "Wirth, Niklaus"
      ),
      Book(
        "Elements of ML Programming",
        "Ullman, Jeffrey"
      ),
      Book(
        "The Java Language Specification", "Gosling, James",
        "Joy, Bill", "Steele, Guy", "Bracha, Gilad"
      )
    )

  it should "find the title of all books whose author's last name is Gosling" in {
    val result =
      for { b <- books
            a <- b.authors
            if a.startsWith("Gosling")
      } yield b.title

    result shouldBe List("The Java Language Specification")
  }

  it should "find the titles of all books that have the string 'Program' in their title" in {
    val result =
      for { b <- books
            if b.title.indexOf("Program") >= 0
      } yield b.title

    result shouldBe List(
      "Structure and Interpretation of Computer Programs",
      "Programming in Modula-2",
      "Elements of ML Programming"
    )
  }

  it should "find the names of all authors that have written at least two books in the database" in {
    val result =
      for { b1 <- books
            b2 <- books
            if b1 != b2;
            a1 <- b1.authors
            a2 <- b2.authors
            if a1 == a2
      } yield a1

    result shouldBe List("Ullman, Jeffrey", "Ullman, Jeffrey")

    // let's remove the double entry
    def removeDuplicates(xs: List[String]): List[String] = xs match {
      case Nil => xs
      case head :: tail =>
        head :: removeDuplicates(
          for { x <- tail
                if x != xs.head
          } yield x
        )
    }
    removeDuplicates(result) shouldBe List("Ullman, Jeffrey")
  }
}