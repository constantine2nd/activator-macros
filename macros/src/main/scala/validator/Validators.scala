package validator

import scala.annotation.{implicitNotFound, StaticAnnotation}
import scala.reflect.macros.Context

@implicitNotFound("You are missing a Validator for ${A}")
trait Validator[A] {

  def validate(a: A): Boolean

}

trait JsonFormat[A] {

  def toJson(a: A): String

}

case class max(value: Int) extends StaticAnnotation

import language.experimental.macros

trait Json {

  def jsonFormat1: String = "one"

  def jsonFormat2: String = "two"

  def jsonFormat3: String = "three"

}

object Json extends Json {

  implicit def deriveJsonFormat[A]: JsonFormat[A] = macro deriveJsonFormat_impl[A]

  def deriveJsonFormat_impl[A : c.WeakTypeTag](c: Context): c.Expr[JsonFormat[A]] = {
    import c.universe._

    val count = c.weakTypeOf[A].typeSymbol.typeSignature.declaration(newTermName("<init>")).asMethod.paramss(0).size
    println(s"Seen $count params")

    count match {
      case 1 => reify(new JsonFormat[A] {
        def toJson(a: A): String = jsonFormat1
      })
      case 2 => reify(new JsonFormat[A] {
        def toJson(a: A): String = jsonFormat2
      })
    }
  }x

}

object Validators {

  implicit def deriveValidator[A]: Validator[A] = macro deriveValidator_impl[A]

  def deriveValidator_impl[A : c.WeakTypeTag](c: Context): c.Expr[Validator[A]] = {
    import c.universe._

    reify {
      new Validator[A] {
        def validate(a: A): Boolean = true
      }
    }
  }
}
