import java.io._
import java.net._

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._

import scala.io._

//Client object
object BooleanClient {

  //Establish a connection to server
  val s = new Socket(InetAddress.getByName("localhost"), 9999)
  val out = new PrintStream(s.getOutputStream)
  lazy val in: Iterator[String] = new BufferedSource(s.getInputStream).getLines()

  implicit val formats = DefaultFormats

  def main(args: Array[String]) {

    while (true) {
      //Read JSON expression from console
      val jsonExpression = scala.io.StdIn.readLine()
      val prettyJsonExpression = pretty(render(parse(jsonExpression)))
      println(prettyJsonExpression)
      //If user input "close", send a "close" message, then disconnect
      if (jsonExpression == "close") {
        out.println(jsonExpression)
        s.close()
        return
      }
      else {
        //Print the Algebra expression
        out.println(jsonExpression)
        out.flush()
        println("output: " + in.next())
      }
    }
  }
}
