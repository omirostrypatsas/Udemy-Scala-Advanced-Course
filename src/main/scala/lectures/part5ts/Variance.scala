package lectures.part5ts

import java.awt.ScrollPane

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // what is variance?
  // "inheritance" - type substitution of generics

  // should a cage of cat inherit from a cage of animal

  class Cage[T]
  // yes - covariance
  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  // no - invariance
  class ICage[T]
//  val icage: ICage[Animal] = new ICage[Cat] // this is not possible and it is as bad as the next line
  // val x: Int = "Hello world"

  // hell no - opposite = contravariance
  class XCage[-T]
  val xcage: XCage[Cat] = new XCage[Animal]

  class InvariantCage[T](animal: T) // invariant

  // ARGUMENTS

  // covariant problem
  class CovariantCage[+T](val animal: T) // the generic type of vals of fields are in a COVARIANT position. This means that the compiler accepts a field declared with a covariant type.

//  class ContravariantCage[-T](val animal: T)
  /*
    val catCage:XCage[Cat] = new XCage[Animal](new Crocodile)
   */

//  class CovariantVariableCage[+T](var animal: T) // types of vars are in CONTRAVARIANT POSITION
  /*
    val ccage: CCage[Animal] = new CCage[Cat](new Cat)
    ccage.animal = new Crocodile
   */

//  class ContravariantVariableCage[-T](var animal: T) // also in COVARIANT POSITION
  /*
  val catCage:XCage[Cat] = new XCage[Animal](new Crocodile)
 */
  class InvariantVariableCage[T](var animal: T) // ok

//  trait AnotherCovariantCage[+T] {
//    def addAnimal(animal: T) // CONTRAVARIANT POSITION
//  }
  /*
    VAL CCAGE: CCage[Animal] = new CCage[Dog]
    ccage.add(new Cat)
   */

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }

  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  acc.addAnimal(new Cat)
  class Kitty  extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat) // because Cat is a supertype of Kitty so the compiler is happy
  val evenMoreAnimals = moreAnimals.add(new Dog) // this would be disastrous if we didn't create the above MyList class

  // METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION

  // METHOD types

  class PetShop[-T] {
//    def get(isItaPuppy: Boolean): T // METHOD RETURN TYPES ARE IN COVARIANT POSITION
    /*
      val catShop = new PetShop[Animal] {
        def get(isItaPuppy: Boolean): Animal = new Cat
      }

      val dogShop: PetShop[Dog] = catShop
      dogShop.get(true) // EVIL CAT!!
     */

    def get[S <: T](isItaPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: PetShop[Dog] = new PetShop[Animal]
//  val evilCat = shop.get(true, new Cat)
  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova) // this is allowed

  /*
    Big rule
    - method arguments are in CONTRAVARIANT position
    - return types are in COVARIANT position
   */

  /*
    1. Invariant, Covariant, Contravariant
       Parking[T](things: List[T]) {
        park(vehicle: T)
        impound(vehicles: List[T])
        checkVehicles(conditions: String): List[T]
       }

     2. Used someone else's API: List[T]
     3. Parking = monad!
          - flatmap
   */

  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle

  class IList[T]

  class IParking[T](vehicles: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f:  T => IParking[S]): IParking[S] = ???
  }

  class CParking[+T](vehicles: List[T]) {
    def park[S >: T](vehicle: S): CParking[S] = ???
    def impound[S >: T](vehicles: List[S]): CParking[S] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f:  T => CParking[S]): CParking[S] = ???
  }

  class VParking[-T](vehicles: List[T]) {
    def park(vehicle: T): VParking[T] = ???
    def impound(vehicles: List[T]): VParking[T] = ???
    def checkVehicles[S <: T](conditions: String): List[S] = ???

    def flatMap[R <: T,S ](f:  Function1[R, VParking[S]]): VParking[S] = ???
  }

  /*
  Rule of thumb
  - use covariance = COLLECTION OF THINGS
  - use contravariance = GROUP OF ACTIONS
 */

  class CParking2[+T](vehicles: IList[T]) {
    def park[S >: T](vehicle: S): CParking2[S] = ???
    def impound[S >: T](vehicles: IList[S]): CParking2[S] = ???
    def checkVehicles[S >: T](conditions: String): IList[S] = ???
  }

  class VParking2[-T](vehicles: IList[T]) {
    def park(vehicle: T): VParking2[T] = ???
    def impound[S <: T](vehicles: IList[S]): VParking2[S] = ???
    def checkVehicles[S <: T](conditions: String): IList[S] = ???
  }

}
