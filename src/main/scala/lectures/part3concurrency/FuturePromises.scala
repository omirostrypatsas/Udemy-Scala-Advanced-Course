package lectures.part3concurrency

import scala.concurrent.Future
import scala.util.{Failure, Random, Success}

// important for futures
import scala.concurrent.ExecutionContext.Implicits.global

object FuturePromises extends App{

  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {
    calculateMeaningOfLife // calculates the meaning of life on ANOTHER thread
  } // (global) which is passed by the compiler

  println(aFuture.value) // Option[Try[Int]]

  println("Waiting on the future")
  aFuture.onComplete {
    case Success(meaningOfLife) => println(s"the meaning of life is $meaningOfLife")
    case Failure(e) => println(s"I have failed with $e")
  } // SOME thread

  Thread.sleep(3000)

  // mini social network

  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile): Unit =
      println(s"${this.name} poking ${anotherProfile.name}")
  }

  object SocialNetwork {
    // "database"
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "Dummy"
    )
    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )

    val random = new Random()

    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      // fetching from the DB
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile):  Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }
  }

  // client: mark to poke bill
  val mark = SocialNetwork.fetchProfile(id = "fb.id.1-zuck")
//  mark.onComplete {
//    case Success(markProfile) => {
//      val bill = SocialNetwork.fetchBestFriend(markProfile)
//      bill.onComplete {
//        case Success(billProfile) => markProfile.poke(billProfile)
//        case Failure(e) => e.printStackTrace()
//      }
//    }
//    case Failure(ex) => ex.printStackTrace()
//  }


  //  functional composition of futures
  // map, flatMap, filter
  val nameOnTheWall = mark.map(profile => profile.name) // from a Future[Profile] we are returning a Future[String]. if it is an Exception then the same Exception is returned.

  val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile)) // from a Future[Profile] we are returning a Future[Profile]

  val zucksBestFriendRestricted = marksBestFriend.filter(profile => profile.name.startsWith("Z")) // filter a Future and return a Future of the same type or if it fails will return a NoSuchElementException

  // for-comprehensions

  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)

  Thread.sleep(1000)

  // fallbacks
  val profileNotMatterWhat = SocialNetwork.fetchProfile("unknown id").recover {
    case e: Throwable => Profile("fb.id.0.dummy", "Forever alone")
  }

  val aFetchedProfileNotMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith {
    case e: Throwable => SocialNetwork.fetchProfile("fb.id.0.dummy")
  }

  val fallbackResult = SocialNetwork.fetchProfile("unknown id").fallbackTo(SocialNetwork.fetchProfile("fb.id.0.dummy")) // if both fail, then the Exception of the first Future will be returned.

  // online banking app
  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {

  }

}
