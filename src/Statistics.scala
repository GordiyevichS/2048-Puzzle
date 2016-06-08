import scala.collection.mutable.ListBuffer

class Statistics {
  
  var fieldList: ListBuffer[Int] = new ListBuffer[Int]()

  def getStatistics(array:Array[Array[String]]): Array[Int] = {
    var retArray = new Array[Int](4)
    var valueList = ListBuffer[Int](0,0,0,0)
    var turns: Int = 0
    
    for(i <- 0 until array.length-1) {
      for(j <- 0 until array(i).length) {
        array(i)(j) match {
          case "up" => valueList(0) += 1
          case "down" => valueList(1) += 1
          case "left" => valueList(2) += 1
          case "right" => valueList(3) += 1
        }
        turns += 1
      }
    }
    
    retArray(0) = valueList.zipWithIndex.max._2
    retArray(1) = valueList(retArray(0))
    retArray(2) = turns
    retArray(3) = turns/array.length
    
    retArray
  }

}