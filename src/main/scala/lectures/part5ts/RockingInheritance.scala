package lectures.part5ts

object RockingInheritance extends App {

  // convenience

  trait Writer[T] {
    def write(value: T): Unit
  }

  trait Closeable {
    def close(status: Int): Int
  }

  trait GenericStream[T] {
    // some methods
    def foreach(f: T => Unit): Unit
  }

  def processStream[T](stream: GenericStream[T] with Writer[T] with Closeable): Unit = { // this is its own type
    stream.foreach(println)
    stream.close(0)
  }

  // diamond problem
  trait Animal {
    def name: String
  }
  trait Lion extends Animal { override def name: String = "Lion" }
  trait Tiger extends Animal { override def name: String = "Tiger" }
  class Mutant extends Lion with Tiger {
    override def name: String = "ALIEN"
  } // this works as it is extending both Lion and Tiger traits but it is overriding the name method in the Mutant class
  // works even if the overwritten method is removed

  val m: Mutant = new Mutant
  println(m)

  /*
    Mutant extends Animal with { override def name: String = "Lion" } with Animal with { override def name: String = "Tiger" }

    LAST OVERRIDE GETS PICKED is the solution to the diamond problem
   */

  // the super problem + type linearization

  trait Cold {
    def print = println("Cold")
  }

  trait Green extends Cold {
    override def print(): Unit = {
      println("Green")
      super.print
    }
  }

  trait Blue extends Cold {
    override def print: Unit = {
      println("Blue")
      super.print
    }
  }

  class Red {
    def print: Unit = println("Red")
  }

  class White extends Red with Green with Blue {
    override def print(): Unit = {
      println("White")
      super.print
    }
  }

  val colour = new White
  colour.print()

}
