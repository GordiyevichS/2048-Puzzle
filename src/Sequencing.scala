import scala.collection.mutable.ArrayBuffer
class Sequencing {
  val Up = "up"                             //> Up  : String = Up
  val Down = "down"                         //> Down  : String = Down
  val Left = "left"                         //> Left  : String = Left
  val Right = "right"                       //> Right  : String = Right
    
  var movesCount: ArrayBuffer[Int] = new ArrayBuffer[Int]() //> movesCount  : Array[Int] = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                                                  //| , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,                                             //|  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
  
  def check(arr:Array[String]): Unit = {
  
    arr match {
            case Array(Up,Up,Up) => movesCount(0) += 1
            case Array(Up,Up,Down) => movesCount(1) += 1
            case Array(Up,Up,Left) => movesCount(2) += 1
            case Array(Up,Up,Right) => movesCount(3) += 1
            case Array(Up,Down,Up) => movesCount(4) += 1
            case Array(Up,Down,Down) => movesCount(5) += 1
            case Array(Up,Down,Left) => movesCount(6) += 1
            case Array(Up,Down,Right) => movesCount(7) += 1
            case Array(Up,Left,Up) => movesCount(8) += 1
            case Array(Up,Left,Down) => movesCount(9) += 1
            case Array(Up,Left,Left) => movesCount(10) += 1
            case Array(Up,Left,Right) => movesCount(11) += 1
            case Array(Up,Right,Up) => movesCount(12) += 1
            case Array(Up,Right,Down) => movesCount(13) += 1
            case Array(Up,Right,Left) => movesCount(14) += 1
            case Array(Up,Right,Right) => movesCount(15) += 1
            case Array(Down,Up,Up) => movesCount(16) += 1
            case Array(Down,Up,Down) => movesCount(17) += 1
            case Array(Down,Up,Left) => movesCount(18) += 1
            case Array(Down,Up,Right) => movesCount(19) += 1
            case Array(Down,Down,Up) => movesCount(20) += 1
            case Array(Down,Down,Down) => movesCount(21) += 1
            case Array(Down,Down,Left) => movesCount(22) += 1
            case Array(Down,Down,Right) => movesCount(23) += 1
            case Array(Down,Left,Up) => movesCount(24) += 1
            case Array(Down,Left,Down) => movesCount(25) += 1
            case Array(Down,Left,Left) => movesCount(26) += 1
            case Array(Down,Left,Right) => movesCount(27) += 1
            case Array(Down,Right,Up) => movesCount(28) += 1
            case Array(Down,Right,Down) => movesCount(29) += 1
            case Array(Down,Right,Left) => movesCount(30) += 1
            case Array(Down,Right,Right) => movesCount(31) += 1
            case Array(Left,Up,Up) => movesCount(32) += 1
            case Array(Left,Up,Down) => movesCount(33) += 1
            case Array(Left,Up,Left) => movesCount(34) += 1
            case Array(Left,Up,Right) => movesCount(35) += 1
            case Array(Left,Down,Up) => movesCount(36) += 1
            case Array(Left,Down,Down) => movesCount(37) += 1
            case Array(Left,Down,Left) => movesCount(38) += 1
            case Array(Left,Down,Right) => movesCount(39) += 1
            case Array(Left,Left,Up) => movesCount(40) += 1
            case Array(Left,Left,Down) => movesCount(41) += 1
            case Array(Left,Left,Left) => movesCount(42) += 1
            case Array(Left,Left,Right) => movesCount(43) += 1
            case Array(Left,Right,Up) => movesCount(44) += 1
            case Array(Left,Right,Down) => movesCount(45) += 1
            case Array(Left,Right,Left) => movesCount(46) += 1
            case Array(Left,Right,Right) => movesCount(47) += 1
            case Array(Right,Up,Up) => movesCount(48) += 1
            case Array(Right,Up,Down) => movesCount(49) += 1
            case Array(Right,Up,Left) => movesCount(50) += 1
            case Array(Right,Up,Right) => movesCount(51) += 1
            case Array(Right,Down,Up) => movesCount(52) += 1
            case Array(Right,Down,Down) => movesCount(53) += 1
            case Array(Right,Down,Left) => movesCount(54) += 1
            case Array(Right,Down,Right) => movesCount(55) += 1
            case Array(Right,Left,Up) => movesCount(56) += 1
            case Array(Right,Left,Down) => movesCount(57) += 1
            case Array(Right,Left,Left) => movesCount(58) += 1
            case Array(Right,Left,Right) => movesCount(59) += 1
            case Array(Right,Right,Up) => movesCount(60) += 1
            case Array(Right,Right,Down) => movesCount(61) += 1
            case Array(Right,Right,Left) => movesCount(62) += 1
            case Array(Right,Right,Right) => movesCount(63) += 1
        }
    }
  
  def createArrBuf() {
    for(i <- 0 until 64) movesCount += 0
  }
  
  def getBestSeq(): Array[Int] = {
    Array(movesCount.zipWithIndex.max._2,movesCount.max);
  }
}