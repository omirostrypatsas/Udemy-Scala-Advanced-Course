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

}
