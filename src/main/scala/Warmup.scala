object Warmup {
  def main(args: Array[String]) {
    //Read n from console
    //val n = scala.io.StdIn.readInt()

    val n = 10

    //Print result
    println(recursion(n))
    println(dynamicProgramming(n))
    println(power(n))
  }

  //Recursive function
  def recursion(n: Int): Int = {
    if (n == 0)
      1
    else
      recursion(n - 1) + recursion(n - 1)
  }

  //Dynamic programming function
  def dynamicProgramming(n: Int): Int = {
    var dp = new Array[Int](n + 1)
    dp(0) = 1
    1 to n foreach { i => dp(i) = dp(i - 1) + dp(i - 1) }
    dp(n)
  }

  //Power function
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
