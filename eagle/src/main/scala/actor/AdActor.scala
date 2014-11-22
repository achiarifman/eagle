package actor

import akka.actor.ActorRef
import com.eagle.entity.EagleRecordEntity

import scala.collection.mutable.Stack

/**
 * Created by Achia.Rifman on 13/09/2014.
 */
class AdActor extends AbstractActor{

  def receive = {

    case (adJob: EagleRecordEntity, it : Stack[ActorRef]) => {

    }

  }

  def getVideoSegmentsCandidates(adJob: EagleRecordEntity) : List[Int] ={

    val duration = adJob.getDuration
    val numOfAds = adJob.getNumberOfAds
    val segmentLength : Int = duration / numOfAds
    val segments = List.range(0,duration,segmentLength)
    segments
  }

}
