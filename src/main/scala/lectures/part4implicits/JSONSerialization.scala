package lectures.part4implicits

import java.util.Date

object JSONSerialization extends App {

  /*
    Users, posts, feeds
    Serialization to JSON
   */

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  /*
    1 - Intermediate data types: Int, String, List, Date
    2 - type classes for conversion to intermediate data types
    3 - serialize to JSON
   */

  sealed trait JSONValue {
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    def stringify: String = "\"" + value + "\""
  }

  final case class JSONNumber(value: Int) extends JSONValue {
    def stringify: String = value.toString
  }

  final case class JSONArray(value: List[JSONValue]) extends JSONValue {
    def stringify: String = value.map(_.stringify).mkString("[", ",", "]")
  }

  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    /*
      {
        name: "John"
        age: Int
        friends: [ ... ]
        latestPost: {
          content: "Scala Rocks",
          date: ...
     */

    def stringify: String = values.map {
      case (key, value) => "\"" + key + "\":" + value.stringify
    }.mkString("{", ",", "}")
  }

  val data = JSONObject(Map(
    "user" ->  JSONString("Daniel"),
    "posts" -> JSONArray(List(
      JSONString("Scala Rocks!"),
      JSONNumber(453)
    ))
  ))

  println(data.stringify)

  // type class
  /*
    1 - type class
    2 - type class instances (implicit)
    3 - pimp library to use type class instances
   */

  // 2.1
  trait JSONConverter[T] {
    def convert(value: T): JSONValue
  }

  // 2.2

  // existing data types
  implicit object StringConverter extends JSONConverter[String] {
    def convert(value: String): JSONValue = JSONString(value)
  }

  implicit object NumberConverter extends JSONConverter[Int] {
    def convert(value: Int): JSONValue = JSONNumber(value)
  }

  // custom data types
  implicit object UserConverter extends JSONConverter[User] {
    def convert(user: User): JSONValue = JSONObject(Map(
      "name" -> JSONString(user.name),
      "age" -> JSONNumber(user.age),
      "email" -> JSONString(user.email)
    ))
  }

  implicit object PostConverter extends JSONConverter[Post] {
    def convert(post: Post): JSONValue = JSONObject(Map(
      "content" -> JSONString(post.content),
      "createdAt" -> JSONString(post.createdAt.toString)
    ))
  }

  implicit object FeedConverter extends JSONConverter[Feed] {
    def convert(feed: Feed): JSONValue = JSONObject(Map(
      "user" -> UserConverter.convert(feed.user), // TODO
      "posts" -> JSONArray(feed.posts.map(PostConverter.convert)) // TODO
    ))
  }

  // 2.3 conversion

  // call stringify on result

}
