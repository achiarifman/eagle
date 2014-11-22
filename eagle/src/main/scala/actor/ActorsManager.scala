package actor

import actor.ads.CaptureImagesActor
import akka.actor.{ActorRef, Actor, ActorLogging, Props}
import com.eagle.entity.EagleRecordEntity
import entity.{FailedEntity}
import util.EagleSpringProperties
import scala.collection.mutable.Stack
/**
 * Created by Achia.Rifman on 12/09/2014.
 */
class ActorsManager extends Actor with ActorLogging{

  val recordActor = context.actorOf(Props[RecordActor], "recordActor")
  val uploadActor = context.actorOf(Props[UploadActor], "uploadActor")
  val captureImagesActor = context.actorOf(Props[CaptureImagesActor] , "captureImagesActor")

  def receive = {

    case (init : EagleRecordEntity) => {
      captureImagesActor ! "start"
      log.info("Got new job, starting to process is")
      initJob(init)
      println("send to record")
    }
    case (uploadRecordJob: EagleRecordEntity, stack : Stack[ActorRef]) => {
      if(stack.isEmpty){
        println("Finished the process")
      }
    }
    case (eagleRecordJob: EagleRecordEntity, it: Stack[ActorRef], failed: FailedEntity) => {
      handleFailedJob(eagleRecordJob,failed)
    }
    case _ => println("NONE")
  }

  def initJob(initRecordJob : EagleRecordEntity){

    log.info("Initializing a new Job")
    val actors = new Stack[ActorRef]
    actors.push(context.self)
    actors.push(uploadActor)
    actors.push(recordActor)
    log.info("Starting the job, calling for the first actor")
    actors.pop() ! (initRecordJob,actors)
  }

  def handleFailedJob(eagleRecordJob: EagleRecordEntity, failed: FailedEntity){
    log.error("The job as failed")
  }


}
