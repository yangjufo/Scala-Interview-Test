import BooleanJson._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.{read, write}
import org.scalatest._

//Scala test object
class BooleanJsonTest extends FlatSpec with Matchers {

  //Check Json expression
  def checkJson(jsonExpression: JValue) {
    //check type of expression
    ((jsonExpression \ "jsonClass") \\ classOf[JString]) should contain oneOf("And", "Or", "Not", "Variable", "True", "False")

    jsonExpression \ "jsonClass" match {
      //And, Or: e1, e2 should be JObject
      case JString("And") =>
      case JString("Or") =>
        (jsonExpression \ "e1") shouldBe a[JObject]
        (jsonExpression \ "e2") shouldBe a[JObject]

      //Not: e should be JObject
      case JString("Not") =>
        (jsonExpression \ "e") shouldBe a[JObject]

      //Variable: symbol should be JString
      case JString("Variable") =>
        (jsonExpression \ "symbol") shouldBe a[JString]

      //Others: do not need check
      case _ =>
    }

    jsonExpression match {
      //And, Or: recursively check e1, e2
      case JObject(JField("jsonClass", JString("And")) :: JField("e1", e1) :: JField("e2", e2) :: Nil) =>
      case JObject(JField("jsonClass", JString("Or")) :: JField("e1", e1) :: JField("e2", e2) :: Nil) =>
        checkJson(e1)
        checkJson(e2)

      //Not: recursively check e
      case JObject(JField("jsonClass", JString("Not")) :: JField("e", e) :: Nil) =>
        checkJson(e)

      //Others: do not need check
      case _ =>
    }
  }

  //Check Boolean expression
  def checkBoolean(booleanExpression: BooleanExpression): Unit = {
    //Check class
    List(booleanExpression.getClass) should contain oneOf(classOf[And], classOf[Or], classOf[Not], classOf[Variable], True.getClass, False.getClass)

    booleanExpression match {
      //And, or: recursively check e1, e2
      case and: And =>
        checkBoolean(and.e1)
        checkBoolean(and.e2)
      case or: Or =>
        checkBoolean(or.e1)
        checkBoolean(or.e2)

      //Not: recursively check e
      case not: Not =>
        checkBoolean(not.e)

      //Variable: symbol should be a string
      case variable: Variable =>
        variable.symbol shouldBe a[String]

      //Others: do not need check
      case _ =>
    }
  }

  //Test Set
  //Basic Boolean expression
  val basicBoolean = List(
    True,
    False,
    Variable("a"),
    Not(Variable("a")),
    And(Variable("a"), Variable("a")),
    Or(Variable("a"), Variable("a"))
  )

  //Combined Boolean expression
  val combineBoolean = List(
    And(And(Variable("a"), True), And(Variable("b"), False)),
    Or(Or(Variable("a"), True), Or(Variable("b"), False)),
    Not(Not(Variable("a"))),

    And(Or(Variable("a"), True), Not(Variable("b"))),
    Or(And(Variable("a"), True), Not(Variable("b"))),
    Not(And(Variable("a"), True)),
    Not(Or(Variable("a"), True)),

    And(And(Or(Variable("a"), True), Not(Variable("b"))), Or(And(Variable("a"), True), Not(Variable("b")))),
  )

  //False Boolean expression
  val falseBoolean = List(
    FailureTest(True),
    Not(FailureTest(Not(True))),
    And(FailureTest(And(Variable("a"), False)), And(Variable("b"), True)),
    Or(FailureTest(And(Variable("a"), False)), Or(Variable("b"), True))
  )

  //Basic Json expression
  val basicJson = List(
    "{\"jsonClass\":\"True\"}",
    "{\"jsonClass\":\"False\"}",
    "{\"jsonClass\":\"Variable\",\"symbol\":\"a\"}",
    "{\"jsonClass\":\"Not\",\"e\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"}}",
    "{\"jsonClass\":\"And\",\"e1\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"},\"e2\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"}}",
    "{\"jsonClass\":\"Or\",\"e1\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"},\"e2\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"}}"
  )

  //combined Json expression
  val combineJson = List(
    "{\"jsonClass\":\"And\",\"e1\":{\"jsonClass\":\"And\",\"e1\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"},\"e2\":{\"jsonClass\":\"True\"}},\"e2\":{\"jsonClass\":\"And\",\"e1\":{\"jsonClass\":\"Variable\",\"symbol\":\"b\"},\"e2\":{\"jsonClass\":\"False\"}}}",
    "{\"jsonClass\":\"Or\",\"e1\":{\"jsonClass\":\"Or\",\"e1\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"},\"e2\":{\"jsonClass\":\"True\"}},\"e2\":{\"jsonClass\":\"Or\",\"e1\":{\"jsonClass\":\"Variable\",\"symbol\":\"b\"},\"e2\":{\"jsonClass\":\"False\"}}}",
    "{\"jsonClass\":\"Not\",\"e\":{\"jsonClass\":\"Not\",\"e\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"}}}",
    "{\"jsonClass\":\"And\",\"e1\":{\"jsonClass\":\"Or\",\"e1\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"},\"e2\":{\"jsonClass\":\"True\"}},\"e2\":{\"jsonClass\":\"Not\",\"e\":{\"jsonClass\":\"Variable\",\"symbol\":\"b\"}}}",
    "{\"jsonClass\":\"Or\",\"e1\":{\"jsonClass\":\"And\",\"e1\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"},\"e2\":{\"jsonClass\":\"True\"}},\"e2\":{\"jsonClass\":\"Not\",\"e\":{\"jsonClass\":\"Variable\",\"symbol\":\"b\"}}}",
    "{\"jsonClass\":\"Not\",\"e\":{\"jsonClass\":\"And\",\"e1\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"},\"e2\":{\"jsonClass\":\"True\"}}}",
    "{\"jsonClass\":\"Not\",\"e\":{\"jsonClass\":\"Or\",\"e1\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"},\"e2\":{\"jsonClass\":\"True\"}}}",
    "{\"jsonClass\":\"And\",\"e1\":{\"jsonClass\":\"And\",\"e1\":{\"jsonClass\":\"Or\",\"e1\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"},\"e2\":{\"jsonClass\":\"True\"}},\"e2\":{\"jsonClass\":\"Not\",\"e\":{\"jsonClass\":\"Variable\",\"symbol\":\"b\"}}},\"e2\":{\"jsonClass\":\"Or\",\"e1\":{\"jsonClass\":\"And\",\"e1\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"},\"e2\":{\"jsonClass\":\"True\"}},\"e2\":{\"jsonClass\":\"Not\",\"e\":{\"jsonClass\":\"Variable\",\"symbol\":\"b\"}}}}"
  )

  //False Json expression
  val falseJson = List(
    "{\"jsonClass\":\"Good\"}",
    "{\"jsonClass\":\"Variable\",\"symbols\":\"a\"}",
    "{\"jsonClass\":\"Not\",\"e1\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"}}",
    "{\"jsonClass\":\"And\",\"e1\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"},\"e3\":{\"jsonClass\":\"Variable\",\"symbol\":\"a\"}}",
  )


  //Test Boolean expression serialization
  "Boolean expression" should "be serialized to Json correctly" in {
    for (b <- basicBoolean ::: combineBoolean) {
      val jsonExpression = write(b)
      println(jsonExpression)
      checkJson(parse(jsonExpression))
    }
  }

  //Test Json expression deserialization
  "Json expression" should "be deserialized correctly" in {
    for (j <- basicJson ::: combineJson) {
      val booleanExpression = read[BooleanExpression](j)
      println(booleanExpression)
      checkBoolean(booleanExpression)
    }
  }
}
