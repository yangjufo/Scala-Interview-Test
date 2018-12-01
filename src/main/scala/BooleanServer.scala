import java.io._
import java.net._

import BooleanAlgebra.booleanToAlgebra
import com.typesafe.scalalogging.LazyLogging
import org.json4s.MappingException

import scala.io._

object BooleanServer extends LazyLogging{
  val server = new ServerSocket(9999)

  def main(args: Array[String]) {
    while (true) {
      val s = server.accept()
      val in = new BufferedSource(s.getInputStream).getLines()
      val out = new PrintStream(s.getOutputStream)
      var connected = true
      while (connected) {
        if (in.isEmpty) {
          s.close()
          connected = false
          logger.info("Client has disconnected!")
        }
        else {
          val jsonExpression = in.next()
          if (jsonExpression == "close") {
            s.close()
            connected = false
            logger.info("Client has disconnected!")
          }
          else {
            try {
              val booleanExpression = BooleanJson.jsonToBoolean(jsonExpression)
              val algebraExpression = booleanToAlgebra(booleanExpression)
              out.println(algebraExpression)
              out.flush()
              logger.info("Json Expression: " + jsonExpression)
              logger.info("Boolean Expression" + booleanExpression)
              logger.info("Algebra Expression" + algebraExpression)
            } catch {
              case e: MappingException =>
                out.println(e.getMessage)
                //logger.error("Exception: " + e.getMessage)
              case e: Exception =>
                out.println(e.printStackTrace())
                //logger.error("Exception: " + e.printStackTrace())
            }
          }
        }
      }
    }
  }
}