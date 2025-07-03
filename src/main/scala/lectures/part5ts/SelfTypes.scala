package lectures.part5ts

object SelfTypes extends App {

  // requiring a type to be mixed in

  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer { self: Instrumentalist => // SELF TYPE whoever implements Singer to implement Instrumentalist
    // self is just a name we chose, it can be this, scala or whatever we choose
    // the construct self is completely separate from the implementation below
    def sing(): Unit

  }

  trait LeadSinger extends Singer with Instrumentalist {
    override def play(): Unit = ???

    override def sing(): Unit = ???
  }

//  class Vocalist extends Singer {
//    override def sing(): Unit = ???
//  }
  // this will not compile as Singer is a self type and thus cannot be extended

  val jamesHetfield = new Singer with Instrumentalist {
    override def sing(): Unit = ???

    override def play(): Unit = ???
  }

  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("solo guitar")
  }

  val ericClapton = new Guitarist with Singer {
    override def sing(): Unit = ???
  }

  // vs inheritance
  class A
  class B extends A // this means that class B must also be a class A

  trait T
  trait S { self: T => } // this means S requires a T

  // different concepts

  // CAKE PATTERN => "dependency injection"

  // DI
  class Component {
    // API
  }

  class ComponentA extends Component
  class ComponentB extends Component
  class DependentComponent(val component: Component)

  // CAKE PATTERN
  trait ScalaComponent {
    // API
    def action(x: Int): String
  }

  trait ScalaDependentComponent { self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + " this rocks!"
  }
  trait ScalaApplication { self: ScalaDependentComponent => }

  // layer 1 - small components

  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // layer 2 - compose components

  trait Profile extends ScalaDependentComponent with Picture
  trait Analytics extends ScalaDependentComponent with Stats

  // layer 3 - app
  trait AnalyticsApp extends ScalaApplication with Analytics

  // cyclical dependencies
//  class X extends Y
//  class Y extends X
// this will not compile

  // this is possible with self types
  trait X { self: Y => }
  trait Y { self: X => }


}
