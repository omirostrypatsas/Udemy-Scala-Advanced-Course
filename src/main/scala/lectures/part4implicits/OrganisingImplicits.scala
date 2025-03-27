package lectures.part4implicits

object OrganisingImplicits extends App {

  implicit def reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
//  implicit def normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)
  // can only be defined inside a class, object or trait, cannot be defined as top level

  println(List(1, 2, 3, 4, 5).sorted)

  // scala.Predef

  /*
    Implicits:
      - val/var
      - object
      - accessor methods = defs with no parenthesis
   */

  // Exercise
  case class Person(name: String, age: Int)

  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )

//  object Person {
//    implicit def alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
//  }

//  implicit def ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age)
//  println(persons.sorted)

  /*
    Implicit scope
    - normal scope = LOCAL SCOPE
    - imported scope
    - companion objects of all types involved in the method signature
      - List
      - Ordering
      - all the types involved
   */

  // def sorted[B >: A](implicit ord: Ordering[B]): List[B]

  object AlphabeticNameOrdering {
    implicit def alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  object AgeOrdering {
    implicit def ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age)
  }

  import AgeOrdering.ageOrdering
  println(persons.sorted)

  /*
    Exercise.
    - totalPrice = most used (50%)
    - by unit count (25%)
    - by unit price (25%)
   */

  case class Purchase(nUnits: Int, unitPrice: Double)

  object Purchase {
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a,b) => a.unitPrice * a.nUnits < b.unitPrice * b.nUnits)
  }

  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
  }

  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }

}
