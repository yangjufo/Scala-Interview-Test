//Algebraic transformation
object BooleanAlgebra {

  def booleanToAlgebra(booleanExpression: BooleanExpression): String = {
    booleanExpression match {
      case And(e1, e2) => "(" + booleanToAlgebra(e1) + ")∧(" + booleanToAlgebra(e2) + ")"
      case Or(e1, e2) => "(" + booleanToAlgebra(e1) + ")v(" + booleanToAlgebra(e2) + ")"
      case Not(e) => "¬(" + booleanToAlgebra(e) + ")"
      case Variable(symbol) => symbol
      case True => "1"
      case False => "0"
    }
  }

  def main(args: Array[String]): Unit = {
    val booleanExpression = And(Or(Variable("a"), True), Not(Variable("b")))
    val algebraExpression = booleanToAlgebra(booleanExpression)
    println(algebraExpression)
  }
}