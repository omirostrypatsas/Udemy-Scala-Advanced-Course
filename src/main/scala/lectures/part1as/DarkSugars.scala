package lectures.part1as

import scala.util.Try

object DarkSugars extends App{

  // syntax sugar #1: methods with single param
  def singleArgMethod(arg: Int): String = s"$arg little ducks..."

  val description = singleArgMethod {
    // good method if we want to write some complex code inside here, if it was just a number can do the normal
    // way of doing singleArgMethod(42)
    42
  }

  val aTryInstance = Try { // java's try {...}
    throw new RuntimeException
  }

  List(1,2,3).map { x =>
    x + 1
  }

  // syntax sugar #2: single abstract method
  trait Action {
    def act(x: Int): Int
  }

  val anInstance: Action = new Action {
    override def act(x: Int): Int = x + 1
  }

  val aFunkyInstance: Action = (x: Int) => x + 1 // magic, the compiler understand that this function-one type will trigger act method to be applied here.

  // example: Runnables
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("hello, Scala")
  })

  val aSweeterThread = new Thread(() => println("Sweet, Scala!")) // as method run does not take any parameters.

  abstract class AnAbstractType {
    def implemented: Int = 23
    def f(a: Int): Unit
  }

  val anAbstractInstance: AnAbstractType = (a: Int) => println("sweet")

  // syntax sugar #3: the :: and #:: methods are special

  val prependedList = 2 :: List(3,4)
  // 2.::(List(3,4)) would normally be the case.
  // List(3,4).::(2) is in fact what the compiler translates it to.
  // ?!

  // scala spec: last char decides associativity of method (in this case if it ends with :: it is right associative instead of broadly used left associativity).
  1 :: 2 :: 3 :: List(4,5)
  List(4,5).::(3).::(2).::(1) // equivalent

  class MyStream[T] {
    def -->:(value:T): MyStream[T] = this //
  }

  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]

  // syntax sugar #4: multi-word method naming

  class TeenGirl(name: String) {
    def `and then said`(gossip:String): Unit =println(s"$name said $gossip")
  }

  val Lilly = new TeenGirl("Lilly")
  Lilly `and then said` "Scala is so sweet!"

  // syntax sugar #5: infix types (with generics)
  class Composite[A, B]
  val composite: Composite[Int, String] = ??? // can be written as
  val compositeInfix: Int Composite String = ???

  class -->[A, B]
  val towards: Int --> String = ???

  // syntax sugar #6: update() is very special, much like apply()
  val anArray = Array(1,2,3)
  anArray(2) = 7 // rewritten to anArray.update(2, 7)
  // used in mutable collections

  // remember apply() AND update()!

  // syntax sugar #7: setters for mutable containers
  class Mutable {
    private var internalMember: Int = 0 // private for OO encapsulation
    def member: Int = internalMember // "getter"
    def member_=(value: Int): Unit =
      internalMember = value // "setter"
  }

  val aMutableContainer = new Mutable
  aMutableContainer.member = 42 // rewritten as aMutableContainer.member_=(42)

}
