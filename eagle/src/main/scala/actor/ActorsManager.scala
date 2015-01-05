package actor

import java.nio.file.Paths

import actor.ads.{ImageScannerActor, CaptureImagesActor}
import actor.consts.ActorsTypes
import actor.image.ImageDiffActor
import actor.message._
import akka.actor.{ActorRef, Actor, ActorLogging, Props}
import com.eagle.dao.JobDao
import com.eagle.entity.EagleRecordEntity
import com.eagle.dao.entity.{EagleRecordJob, FailedEntity}
import org.bson.types.ObjectId
import scala.collection.mutable.Stack
/**
 * Created by Achia.Rifman on 12/09/2014.
 */
class ActorsManager extends Actor with ActorLogging{

  val actorsMap = Map[String,List[ActorRef]](ActorsTypes.RECORD_ACTOR ->
    List[ActorRef](context.actorOf(Props(new RecordActor), "recordActor1"),context.actorOf(Props(new RecordActor), "recordActor2")),
    ActorsTypes.UPLOAD_ACTOR -> List[ActorRef](context.actorOf(Props(new UploadActor), "uploadActor")))
  //val captureImagesActor = context.actorOf(Props(new CaptureImagesActor),ActorsTypes.CAPTURE_IMAGES_ACTOR)
  val adActor = context.actorOf(Props(new AdActor))
/*  val imageDiffActor = context.actorOf(Props(new ImageDiffActor),ActorsTypes.IMAGE_DIFF_ACTOR)
  val imageScannerActor = context.actorOf(Props(new ImageScannerActor), ActorsTypes.IMAGE_SCANNER_ACTOR)*/
  /*val recordActor = context.actorOf(Props[RecordActor], "recordActor1")
  val recordActor2 = context.actorOf(Props[RecordActor], "recordActor2")
  val uploadActor = context.actorOf(Props[UploadActor], "uploadActor")*/
  //val captureImagesActor = context.actorOf(Props[CaptureImagesActor] , "captureImagesActor")

  def receive = {

    case (init : EagleRecordJob) => {
      //captureImagesActor ! "start"
      log.info("Got new job, starting to process is")
      initJob(init)
      println("send to record")
    }
    case (postRecordMessage : PostRecordMessage) => {
      JobDao.updateRecordOutputPath(postRecordMessage.outPutFile, postRecordMessage.id.toString)
      updateJobActorList(postRecordMessage.id.toString)
      val persistedJobEntity = JobDao.getJobById(postRecordMessage.id.toString)
      //need to check if there are others actors
      mapAndSendMessage(persistedJobEntity)
    }
    case (postUploadMessage : PostUploadMessage) => {
      if(postUploadMessage.success){
        updateJobActorList(postUploadMessage.id.toString)
        val persistedJobEntity = JobDao.getJobById(postUploadMessage.id.toString)
        if(persistedJobEntity.waitingActorList.isEmpty){
          println("Finished the process")
        }else {
          println("There are other actors")
        }
      }
    }
    case (eagleRecordJob: EagleRecordEntity, it: Stack[ActorRef], failed: FailedEntity) => {
      handleFailedJob(eagleRecordJob,failed)
    }
    case _ => println("NONE")
  }

  def initJob(initRecordJob : EagleRecordJob){

    //we should save the list of actors we will need in array, so each on call back we can check if we already run the actor
    val actorsPhaseList = actorsMap.get(initRecordJob.waitingActorList.head)
    val firstActor = actorsPhaseList.get.head
    firstActor ! PreRecordMessage(initRecordJob.id,initRecordJob.recordUrl,initRecordJob.recordDuration,initRecordJob.channelName)
    log.info("Initializing a new Job")
  }

  def updateJobActorList(id : String) = {
    val jobEntity = JobDao.getJobById(id)
    val (finished,waiting) = jobEntity.waitingActorList.partition(sender().path.name.contains(_))
    JobDao.updateActorsList(jobEntity.id,finished,waiting)
  }

  def handleFailedJob(eagleRecordJob: EagleRecordEntity, failed: FailedEntity){
    log.error("The job as failed")
  }

  def mapAndSendMessage(job : EagleRecordJob) = {

    val actorName = job.waitingActorList.head
    actorName match {
      case ActorsTypes.UPLOAD_ACTOR => {
        /*if(job.uploadFolder == null){
          job.uploadFolder = job.recordOutPutPath
        }*/
        //need to change the recordOutPutPath
        actorsMap.get(job.waitingActorList.head).get.head ! PreUploadMessage(job.id,Paths.get(job.recordOutPutPath))
      }
      case ActorsTypes.AD_ACTOR => {
        adActor ! InitAdMessage(job.id, job.adsPath, job.recordOutPutPath, job.recordDuration)
      }
      case ActorsTypes.PUBLISH_ACTOR => {

      }
    }

  }




}
