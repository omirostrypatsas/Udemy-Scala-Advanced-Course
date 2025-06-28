package lectures.part5ts

import lectures.part1as.AdvancedPatternMatching.{MyList, humanDescription}
import lectures.part5ts

object TypeMembers extends App{

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    type AnimalType // abstract class member
    type BoundedAnimal <: Animal
    type SuperBoundedAnimal >: Dog <: Animal
    type AnimalC = Cat
  } // abstract type members are mostly for us to help the compiler do some type inference for us

  val ac = new AnimalCollection
  val dog: ac.AnimalType = ???

//  val cat: ac.BoundedAnimal = new Cat

  val pup: ac.SuperBoundedAnimal = new Dog // this is allowed because SuperBoundedAnimal >: Dog but wouldn't work for Cat
  val cat: ac.AnimalC = new Cat // as AnimalC = Cat

  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat

  // alternative to generics
  trait MyList {
    type T
    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList {
    override type T = Int // we can override an abstract type

    override def add(element: Int): MyList = ???
  }

  // .type
  type CatsType = cat.type
  val newCat: CatsType = cat
//  new CatsType // this will confuse the compiler and will not run

  /*
    Exercise - enforce a type to be applicable to SOME TYPES only
   */
  // LOCKED
  trait MList {
    type A
    def head: A
    def tail: MList
  }

  trait ApplicableToNumbers {
    type A <: Number
  }

  // NOT OK
//  class CustomList(hd: String, tl: CustomList) extends MList with ApplicableToNumbers { // extending with ApplicableToNumbers won't work as the type is Sting
//    type A = String
//    def head = hd
//    def tail = tl
//  }

  // OK
  class IntList(hd: Int, tl: IntList) extends MList {
    type A = Int
    def head = hd
    def tail = tl
  }

  // Number
  // type members and type member constraints (bounds)

}
