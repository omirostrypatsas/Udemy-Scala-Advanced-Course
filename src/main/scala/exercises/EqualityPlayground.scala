package exercises

import lectures.part4implicits.TypeClasses.User

object EqualityPlayground extends App {

  /*
  *Equality
 */

  trait Equal[T] {
    def equal(value1: T, value2: T): Boolean
  }

  implicit object NameEquality extends Equal[User] {
    override def equal(user1: User, user2: User): Boolean = user1.name == user2.name
  }

  object NameAndEmailEquality extends Equal[User] {
    override def equal(user1: User, user2: User): Boolean = user1.name == user2.name & user1.email == user2.email

  }

  /*
  Exercise: implement the TC pattern for the Equality tc.
 */

  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.equal(a, b)
  }

  val john = User("John", 32, "john@rockthejvm.com")
  val anotherJohn = User("John", 45, "anotherJohn@rtjvm.com")
  println(Equal(john, anotherJohn))

  // AD-HOC polymorphism

  /*
    Exercises - improve the Equal TC with an implicit conversion class
    ===(anotherValue: T)
    !==(anotherValue: T)
   */

  implicit class TypeSafeEqual[T](value: T) {
    def ===(anotherValue: T)(implicit equalizer: Equal[T]): Boolean = equalizer.equal(value, anotherValue)

    def !==(anotherValue: T)(implicit equalizer: Equal[T]): Boolean = ! equalizer.equal(value, anotherValue)
  }

  println(john === anotherJohn)
  /*
    john.===(anotherJohn)
    new TypeSafeEqual[User](john).===(anotherJohn)
    new TypeSafeEqual[User](john).===(anotherJohn)(NameEquality)
   */
  /*
    TYPE SAEE
   */
  println(john == 43) // applicable only to Scala 2
//  println(john === 43) // TYPE SAFE as the compiler forces that the two types should be the same

}
