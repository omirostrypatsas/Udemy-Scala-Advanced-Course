package lectures.part2afp

object PartialFunctions extends App{
  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }
  // {1,2,5} => Int

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // curly braces and their contents are called partial function value and is equivalent in implementation to the one above.

  // Partial functions are actually based on Pattern matching

  println(aPartialFunction(2))
  // println(aPartialFunction(7444)) // this will crush

  // PF utilities
  println(aPartialFunction.isDefinedAt(67)) // prints false as we don't have a case for 67

  // lift
  val lifted = aPartialFunction.lift // Int => Option[Int]
  println(lifted(2))
  println(98)

  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

  println(pfChain(2))
  println(pfChain(45))

  // PF extend normal functions

  val aTotalFunction: Int => Int = {
    case 1 => 59
  }

  // HOFs accept partial functions as well
  val aMappedList = List(1,2,3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }
  println(aMappedList)

  /*
    Note: PF can only have ONE parameter type
   */

  /**
   * Exercises
   *
   * 1 - construct a PF instance yourself (anonymous class)
   * 2 - dumb chatbot as a PF
   */

  // Exercise 1

  val exercisePF: PartialFunction[String, Boolean] = {
    case "Is that thing working?" => true
    case "Are you a human" => false
  }

  println(exercisePF("Is that thing working?"))
  println(exercisePF("Are you a human"))

  val aManualFussyFunction = new PartialFunction[Int, Int] {
    override def apply(x: Int): Int = x match {
      case 1 => 42
      case 2 => 44
      case 5 => 999
    }
      override def isDefinedAt(x: Int): Boolean =
        x == 1 || x == 2 || x == 5
  }

  // Exercise 2

  val chatbot: PartialFunction[String, String] = {
    case "hello" => "hello to you too"
    case "how are you" => "I am very good thanks"
    case "are you here?" => "Yes I am"
  }

  scala.io.Source.stdin.getLines().map(chatbot).foreach(println)
}
