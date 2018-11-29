object WarmUp {  
  def main(args:Array[String]) {
    var n = readInt();
    println(recur(n));
  }

  def recur(n: Int): Int =  {
    if (n == 0)
      1
    else
      recur(n - 1) + recur(n - 1)
  }
}

