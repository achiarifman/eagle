package util

/**
 * Created by Achia.Rifman on 03/01/2015.
 */
object ImageUtils {

  val d16_9 : Double = 16 /9
  val d21_9 : Double = 21 /9

  val extraDiff = 0.2

  val R16_9 = "16_9"
  val R21_9 = "21_9"
  val R4_3 = "4_3"

  def numToRatio(d : Double) = {
    if(d < d16_9 + extraDiff && d > d16_9 - extraDiff){
      R16_9
    }else{
      if(d < d21_9 + extraDiff && d > d21_9 - extraDiff){
        R21_9
      }else {
        R4_3
      }
    }
  }
}
