import scala.collection.mutable.ListBuffer

class Statistics {
  
  var fieldList: ListBuffer[Int] = new ListBuffer[Int]()

  def getStatistics(array:Array[Array[Int]]): Array[Int] = {
    var retArray = new Array[Int](2)
    var valueList: ListBuffer[Int] = new ListBuffer[Int]()
    var cellMaxList: ListBuffer[Int] = new ListBuffer[Int]()
    
    newValueList(valueList)
    
    for(i <- 0 until array.length) {
      for(j <- 0 until array(i).length/16) {
        for(k <- 16*j until 16*j+16) {
          fieldList+=array(i)(k)
        }
        var index = fieldList.zipWithIndex.max._2
        valueList(index)+=1
        fieldList.clear
      }
      cellMaxList+=array(i).max
    }
    
    retArray(0) = valueList.zipWithIndex.max._2
    retArray(1) = cellMaxList.max
    
    retArray
  }

  def newValueList(valueList:ListBuffer[Int]) {
    for(i <- 0 until 16)
      valueList += 0
  }
}