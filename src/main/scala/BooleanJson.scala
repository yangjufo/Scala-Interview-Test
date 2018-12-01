import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.{read, write}

//a special class for test
case class FailureTest(e: BooleanExpression) extends BooleanExpression

//serialization class
object BooleanJson {

  implicit val formats = DefaultFormats + new booleanSerializer

  //customize serializer
  class booleanSerializer extends CustomSerializer[BooleanExpression](_ => ( {
    //from json to BooleanExpression.scala
    //And, Or: recursively extract e1, e2
    case JObject(JField("jsonClass", JString("And")) :: JField("e1", e1) :: JField("e2", e2) :: Nil) => And(e1.extract[BooleanExpression], e2.extract[BooleanExpression])
    case JObject(JField("jsonClass", JString("Or")) :: JField("e1", e1) :: JField("e2", e2) :: Nil) => Or(e1.extract[BooleanExpression], e2.extract[BooleanExpression])

    //Not: recursively extract e
    case JObject(JField("jsonClass", JString("Not")) :: JField("e", e) :: Nil) => Not(e.extract[BooleanExpression])

    //Variable: symbol cannot be exmpty
    case JObject(JField("jsonClass", JString("Variable")) :: JField("symbol", JString(symbol)) :: Nil) =>
      if (symbol == "") throw new MappingException("Error: Variable name cannot be empty!")
      else Variable(symbol)

    //True, False: restore from "jsonClass"
    case JObject(JField("jsonClass", JString("True")) :: Nil) => True
    case JObject(JField("jsonClass", JString("False")) :: Nil) => False
  }, {
    //from BooleanExpression.scala to json
    //And, Or: recursively deal with e1, e2
    case and: And => JObject(JField("jsonClass", JString("And")), JField("e1", parse(write(and.e1))), JField("e2", parse(write(and.e2))))
    case or: Or => JObject(JField("jsonClass", JString("Or")), JField("e1", parse(write(or.e1))), JField("e2", parse(write(or.e2))))

    //Not: recursively deal with e
    case not: Not => JObject(JField("jsonClass", JString("Not")), JField("e", parse(write(not.e))))

    //Variable: symbol cannot be empty
    case variable: Variable =>
      if (variable.symbol == "") throw new MappingException("Error: Variable name cannot be empty!")
      else JObject(JField("jsonClass", JString("Variable")), JField("symbol", JString(variable.symbol)))

    //True, False: store as a "jsonClass"
    case True => JObject(JField("jsonClass", JString("True")))
    case False => JObject(JField("jsonClass", JString("False")))
  }
  ))

  def booleanToJson(booleanExpression: BooleanExpression): String = {
    write(booleanExpression)
  }

  def jsonToBoolean(jsonExpression: String): BooleanExpression ={
    read[BooleanExpression](jsonExpression)
  }

}