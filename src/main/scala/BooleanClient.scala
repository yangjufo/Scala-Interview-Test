import java.net._
import java.io._
import scala.io._

object BooleanClient {

  val s = new Socket(InetAddress.getByName("localhost"), 9999)
  val out = new PrintStream(s.getOutputStream)
  lazy val in = new BufferedSource(s.getInputStream).getLines()

  def main(args: Array[String]) {

    while (true) {
      val jsonExpression = scala.io.StdIn.readLine()
      println("input:" + jsonExpression)
      if (jsonExpression == "close") {
        out.println(jsonExpression)
        s.close()
        return
      }
      else {
        out.println(jsonExpression)
        out.flush()
        println("output: " + in.next())
      }
    }
  }
}
