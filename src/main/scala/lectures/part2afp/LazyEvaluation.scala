package lectures.part2afp

import javax.annotation.processing.Generated

object LazyEvaluation extends App {

  // lazy DELAYS the evaluation of values
  lazy val x: Int = throw new RuntimeException // it won't execute without calling it for the first time
  // println(x) // this will crush our program

  lazy val y: Int = {
    println("hello")
    42
  }

  println(y) // this will evaluate the x for the first time
  println(y) // it won't execute again, so it will only print the number 42

  // examples of implications:
  // side effects
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }

  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition
  println(if (simpleCondition && lazyCondition) "yes" else "no") // I won't see the side effect println("Boo") printed here, this is because lazy val is not evaluate unless needed and here the first condition is anyway false so it won't be evaluated.

  // in conjunction with call by name
  def byNameMethod(n: => Int): Int = n + n + n + 1
  def retrieveMagicValue = {
    // side effect or a long computation
    println("waiting")
    Thread.sleep(1000)
    42
  }

  println(byNameMethod(retrieveMagicValue)) // it prints "waiting 3 times, so it means that it is calling and evaluating the retrieveMagicValue three times.

  // use lazy vals
  def byNameMethodWithLazyVals(n: => Int): Int = {
    lazy val t = n
    t + t + t + 1
  } // this is called: CALL BY NEED
  // in this way retrieveMagicValue is called and evaluated only once

  println(byNameMethodWithLazyVals(retrieveMagicValue))

  // filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers.filter(lessThan30) // List(1, 25, 5, 23)
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)

  val lt30lazy = numbers.withFilter(lessThan30) // lazy vals under the hood
  val gt20lazy = lt30lazy.withFilter(greaterThan20) // withFilter uses lazy vals and it evaluates the predicates on a by need basis.
  println
  println(gt20lazy) // it doesn't print the side effects on screen as it is not applying the filtering
  gt20lazy.foreach(println)

  // for-comprehensions use withFilter with guards
  for {
    a <- List(1, 2, 3) if a % 2 == 0 // use lazy vals!!
  } yield a + 1
  List(1, 2, 3).withFilter(_ % 2 == 0).map(_ + 1) // List[Int]

  /*
    Exercise: implement a lazily evaluated, singly linked STREAM of elements.

    naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers (potentially infinite!)
    naturals.take(100).foreach(println) // lazily evaluated stream of the first 100 naturals (finite stream)
    naturals.foreach(println) // will crash - infinite!
    naturals.map(_ * 2) // stream of all even numbers (potentially infinite)
   */

  abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B] // prepend operator
    def ++ [B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate two streams

    def foreach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A] // takes the first n elements out of this stream
    def takeAsList(n: Int): List[A]
  }

  object MyStream {
    def from[A](start: A)(generated: A => A): MyStream[A] = ???
  }

}
