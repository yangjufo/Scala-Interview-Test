object WarmUp {
  def main(args: Array[String]) {
    //read n from console
    //val n = scala.io.StdIn.readInt()
    val n = 10
    //print result
    println(recursion(n))
    println(dynamicProgramming(n))
    println(power(n))
  }

  //recursive function
  def recursion(n: Int): Int = {10
    if (n == 0)
      1
    else
      recursion(n - 1) + recursion(n - 1)
  }

  //dynamic programming function
  def dynamicProgramming(n: Int): Int = {
    var dp = new Array[Int](n + 1)
    dp(0) = 1
    1 to n foreach { i => dp(i) = dp(i - 1) + dp(i - 1) }
    dp(n)
  }

  //power function
  def power(n: Int): Int = {
    if (n == 0)
      1
    else {
      val tmp = power(n / 2)
      if (n % 2 != 0)
        2 * tmp * tmp
      else
        tmp * tmp
    }
  }

}
