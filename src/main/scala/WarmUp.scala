object WarmUp {
  def main(args:Array[String]) {
    //read n from console
    val n = scala.io.StdIn.readInt()
    //print result
    println(recur(n))
  }

  //recursive function
  def recur(n: Int): Int =  {
    if (n == 0)
      1
    else
      recur(n - 1) + recur(n - 1)
  }
}
