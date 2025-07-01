package lectures.part5ts

object PathDependentTypes extends App {

  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner) = println(i)
    def printGeneral(i: Outer#Inner) = println(i)
  }

  def aMethod: Int = {
    class HelperClass
    type HelperType = String // you need to define the type so it works
    42
  }

  // per-instance
  val o = new Outer
  val inner = new o.Inner // o.Inner is a TYPE

  val oo = new Outer
//  val otherInner: oo.InnerType = new o.InnerType // this doesn't compile as oo.InnerType and o.InnerType are different

  o.print(inner)
//  oo.print(inner) // not allowed as they are different types and inner is under o

  // this is what we call path-dependent types

  // Outer#Inner
  o.printGeneral(inner)
  oo.printGeneral(inner) // this is now allowed as o.Inner is a subtype of Outer#Inner after printGeneral was defined

  /*
    Exercise:
    DB keyed by Int or String, but maybe others
   */

  /*
    use path-dependent types
    abstract type members and/or type aliases
   */

  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike {
    type Key = K
  }
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???

  get[IntItem](42) // ok
  get[StringItem]("hello") // ok

//  get[IntItem]("Scala") // not ok

}
