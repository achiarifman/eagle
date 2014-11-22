package actor

import akka.actor.{ActorLogging, ActorRef, Actor}
import com.eagle.entity.EagleRecordEntity
import entity.FailedEntity

import scala.collection.mutable.Stack

/**
 * Created by Achia.Rifman on 12/09/2014.
 */
abstract class AbstractActor extends Actor with ActorLogging{


  def handleFailedEntity(eagleRecordJob: EagleRecordEntity, it: Stack[ActorRef], failed: FailedEntity) {
    log.info("Received failed entity: " + failed.reason)
    val act = it.pop()
    act !(eagleRecordJob, it)
  }

}
