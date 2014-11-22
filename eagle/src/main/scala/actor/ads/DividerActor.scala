package actor.ads

import actor.AbstractActor

/**
 * Created by Achia.Rifman on 22/11/2014.
 */
class DividerActor extends AbstractActor{

  //will replace it from db
  val videoLength = 60
  val numOfAds  = 3


  def receive = {

    case _ => {splitVideoByTime()}

  }

  def splitVideoByTime() = {
    val segmentLength : Int = videoLength / numOfAds
    //val segmentsList  = List.range(0,duration,segmentLength)
  }

}
