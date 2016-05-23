class SortReplays {
  def sort(fileName: Array[String],score: Array[Int]) {
    def swap(a: Int, b: Int) {
      val temp1 = fileName(a);
      fileName(a) = fileName(b)
      fileName(b) = temp1
      
      val temp2 = score(a)
      score(a) = score(b)
      score(b) = temp2
    }

    def quickSort(begin: Int, end: Int) {
      val temp = score((begin + end) / 2)
      var i = begin
      var j = end
      while (i <= j) {
        while (score(i) > temp) {
          i += 1
        }
        while (score(j) < temp) {
          j -= 1
        }
        if (i <= j) {
          swap(i, j)
          i += 1
          j -= 1
        }
      }
      if (begin < j) quickSort(begin, j)
      if (j < end) quickSort(i, end)
    }
    quickSort(0, score.length - 1)
  }
}