package validator

object ValidatorDemo extends App {
  import Validators._
  import Json._

  def validate[A : Validator](a: A): Boolean = implicitly[Validator[A]].validate(a)

  def toJson[A : JsonFormat](a: A): String = implicitly[JsonFormat[A]].toJson(a)

  case class Foo(name: String)

  case class Bar(name: String, @max(50) age: Int)

  validate(Foo("Jan"))
  validate(Bar("Jan", 23))
  validate(Bar("Jan", 63))

  println(toJson(Foo("Jan")))
  println(toJson(Bar("Jan", 33)))

}
