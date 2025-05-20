package lectures.part4implicits

object Givens extends App {

  val aList = List(2,4,3,1)
  val anOrderedList = aList.sorted // implicit Ordering[Int]

  // Scala 2 style
  object Implicits {
    implicit val descendingOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  }

  // Scala 3 style
//  object Givens {
//    given descendingOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _) // commented out as we still use Scala 2
//    givens <=> implicit vals
//  }

  // instantiating an anonymous class
  //  object GivenAnonymousClassNaive {
  //    given descendingOrdering_v2: Ordering[Int] = new Ordering[Int]
  //    override def compare(x: Int, y: Int) = y - x
  //  }

  //  object GivenWith {
  //    given descendingOrdering_v3: Ordering[Int] with {
  //    override def compare(x: Int, y: Int) = y - x
  //  }

  // import GivenWith._ // in Scala 3, this import does NOT import givens as well
  // import GivenWith.given // imports all givens

  // implicit arguments?
  def extremes[A](list: List[A])(implicit ordering: Ordering[A]): (A, A) = {
    val sortedList = list.sorted
    (sortedList.head, sortedList.last)
  }

  println(anOrderedList)
}
