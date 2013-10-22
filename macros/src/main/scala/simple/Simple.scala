package simple

import scala.reflect.macros.Context
import language.experimental.macros

object Simple {

  def debug(message: Any): Unit = macro debug_impl

  def debug_impl(c: Context)(message: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._

    reify {
      println("Constant of " + message.splice)
    }
  }

}
