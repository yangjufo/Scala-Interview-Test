//Algebra expression transformation

object BooleanAlgebra {

  def booleanToAlgebra(booleanExpression: BooleanExpression): String = {
    booleanExpression match {

      //And, or: recursively deal with e1, e2
      case And(e1, e2) => "(" + booleanToAlgebra(e1) + ")∧(" + booleanToAlgebra(e2) + ")"
      case Or(e1, e2) => "(" + booleanToAlgebra(e1) + ")v(" + booleanToAlgebra(e2) + ")"

      //Not: recursively deal with e
      case Not(e) => "¬(" + booleanToAlgebra(e) + ")"

      //Variable: add symbol
      case Variable(symbol) => symbol

      //True, False: add a "$T" or "$F"
      case True => "$T"
      case False => "$F"
    }
  }

  def main(args: Array[String]): Unit = {
    val booleanExpression = And(Or(Variable("a"), True), Not(Variable("b")))
    val algebraExpression = booleanToAlgebra(booleanExpression)
    println(algebraExpression)
  }
}