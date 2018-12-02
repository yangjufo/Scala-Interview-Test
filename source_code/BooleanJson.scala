import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.{read, write}

//Serialization class
object BooleanJson {

  //Regular expression used to check variable
  val symbolPattern = "[0-9a-zA-Z_]".r

  implicit val formats = DefaultFormats + new booleanSerializer

  //Customize serializer
  class booleanSerializer extends CustomSerializer[BooleanExpression](_ => ( {
    //From JSON expression to Boolean expression
    //And, Or: recursively extract e1, e2
    case JObject(JField("jsonClass", JString("And")) :: JField("e1", e1) :: JField("e2", e2) :: Nil) => And(e1.extract[BooleanExpression], e2.extract[BooleanExpression])
    case JObject(JField("jsonClass", JString("Or")) :: JField("e1", e1) :: JField("e2", e2) :: Nil) => Or(e1.extract[BooleanExpression], e2.extract[BooleanExpression])

    //Not: recursively extract e
    case JObject(JField("jsonClass", JString("Not")) :: JField("e", e) :: Nil) => Not(e.extract[BooleanExpression])

    //Variable: symbol cannot be exmpty
    case JObject(JField("jsonClass", JString("Variable")) :: JField("symbol", JString(symbol)) :: Nil) =>
      val checkSymbol = (symbolPattern findAllIn symbol).mkString("")
      if (checkSymbol != symbol || symbol.isEmpty)
        throw new MappingException("Error: Variable name contains illegal character or is empty!")
      else
        Variable(symbol)

    //True, False: restore from "jsonClass"
    case JObject(JField("jsonClass", JString("True")) :: Nil) => True
    case JObject(JField("jsonClass", JString("False")) :: Nil) => False
  }, {
    //From Boolean expression to JSON expression
    //And, Or: recursively deal with e1, e2
    case and: And => JObject(JField("jsonClass", JString("And")), JField("e1", parse(write(and.e1))), JField("e2", parse(write(and.e2))))
    case or: Or => JObject(JField("jsonClass", JString("Or")), JField("e1", parse(write(or.e1))), JField("e2", parse(write(or.e2))))

    //Not: recursively deal with e
    case not: Not => JObject(JField("jsonClass", JString("Not")), JField("e", parse(write(not.e))))

    //Variable: symbol cannot be empty
    case variable: Variable =>
      val checkSymbol = (symbolPattern findAllIn variable.symbol).mkString("")
      if (checkSymbol != variable.symbol || variable.symbol.isEmpty)
        throw new MappingException("Error: Variable name contains illegal character or is empty!")
      else JObject(JField("jsonClass", JString("Variable")), JField("symbol", JString(variable.symbol)))

    //True, False: store as a "jsonClass"
    case True => JObject(JField("jsonClass", JString("True")))
    case False => JObject(JField("jsonClass", JString("False")))
  }
  ))

  def booleanToJson(booleanExpression: BooleanExpression): String = {
    write(booleanExpression)
  }

  def jsonToBoolean(jsonExpression: String): BooleanExpression = {
    read[BooleanExpression](jsonExpression)
  }

  def main(args: Array[String]) {
    try {
      var exp = "{\"jsonClass\":\"Variable\",\"symbol\":\"a\"}"
      println(jsonToBoolean(exp))
    }catch {
      case e:MappingException => println(e.getMessage)
      case e:Exception => e.printStackTrace()
    }
  }

}