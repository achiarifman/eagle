import scala.concurrent.duration._

/*val duration = Duration(20, SECONDS)
println(convert(duration.toHours) + ":" + convert(duration.toMinutes) + ":" + duration.toSeconds)

def convert(long: Long) = {
  if (long.toString.size < 2){
    0 + long.toString
  }else{
    long.toString
  }
}*/

val duration = 30
val numOfAds = 3
val segmentLength : Int = duration / numOfAds
val segments = List.range(0,duration,segmentLength)
segments
