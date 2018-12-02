import java.io._
import java.net._

import BooleanAlgebra.booleanToAlgebra
import com.typesafe.scalalogging.LazyLogging
import org.json4s.MappingException

import scala.io._

//Server object
object BooleanServer extends LazyLogging{
  //Initialize the server at port 9999
  val server = new ServerSocket(9999)

  def main(args: Array[String]) {
    while (true) {
      //Listen and accept connection
      val s = server.accept()
      val in = new BufferedSource(s.getInputStream).getLines()
      val out = new PrintStream(s.getOutputStream)

      var connected = true
      while (connected) {
        //If client is disconnected, then disconnect
        if (in.isEmpty) {
          s.close()
          connected = false
          logger.info("Client is disconnected!")
        }
        else {
          val jsonExpression = in.next()
          //If client send a "close" message, then disconnect
          if (jsonExpression == "close") {
            s.close()
            connected = false
            logger.info("Client has disconnected!")
          }
          else {
            try {
              //Convert JSON expression to Boolean expression
              val booleanExpression = BooleanJson.jsonToBoolean(jsonExpression)
              //convert Boolean expression to Algebra expression
              val algebraExpression = booleanToAlgebra(booleanExpression)
              out.println(algebraExpression)
              out.flush()
              logger.info("JSON Expression: " + jsonExpression)
              logger.info("Boolean Expression" + booleanExpression)
              logger.info("Algebra Expression" + algebraExpression)
            } catch {
              case e: MappingException =>
                out.println(e.getMessage)
                logger.error("Exception: " + e.getMessage)
              case e: Exception =>
                out.println(e.printStackTrace())
                logger.error("Exception: " + e.printStackTrace())
            }
          }
        }
      }
    }
  }
}