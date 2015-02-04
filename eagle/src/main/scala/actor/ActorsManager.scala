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
    ActorsTypes.UPLOAD_ACTOR -> List[ActorRef](context.actorOf(Props(new UploadActor), "uploadActor")),
    ActorsTypes.PUBLISH_ACTOR -> List[ActorRef](context.actorOf(Props(new PublishActor), "publishActor")))
  val adActor = context.actorOf(Props(new AdActor),ActorsTypes.AD_ACTOR)

  def receive = {

    case (init : EagleRecordJob) => {
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
        mapAndSendMessage(persistedJobEntity)
      }
    }
    case (eagleRecordJob: EagleRecordEntity, it: Stack[ActorRef], failed: FailedEntity) => {
      handleFailedJob(eagleRecordJob,failed)
    }

    case(postAdEmbederMessage : PostAdEmbederMessage) => {
      if(postAdEmbederMessage.success){
        updateJobActorList(postAdEmbederMessage.id)
        JobDao.updateEmbeddedPath(postAdEmbederMessage.id,postAdEmbederMessage.outputPath)
        val persistedJobEntity = JobDao.getJobById(postAdEmbederMessage.id.toString)
        mapAndSendMessage(persistedJobEntity)
       println("AD embed Finished!!!!!")
      }else{
        // send fail to ActorManager
      }
    }

    case(postPublishMessage : PostPublishMessage) => {
      if(postPublishMessage.success){
        updateJobActorList(postPublishMessage.id)
        JobDao.updateJobStatus(postPublishMessage.id,true,postPublishMessage.content)
        println("Process Finished!!!!!")
      }else{
        // send fail to ActorManager
      }
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

    if(!job.waitingActorList.isEmpty){
      val actorName = job.waitingActorList.head
      actorName match {
        case ActorsTypes.UPLOAD_ACTOR => {
          actorsMap.get(job.waitingActorList.head).get.head ! PreUploadMessage(job.id,Paths.get(job.finalOutPutPath))
        }
        case ActorsTypes.AD_ACTOR => {
          adActor ! InitAdMessage(job.id, job.adsPath, job.recordOutPutPath, job.recordDuration)
        }
        case ActorsTypes.PUBLISH_ACTOR => {
          actorsMap.get(actorName).get.head ! PrePublishMessage(job.id,job.callBackUrl,"finish",job.adsPath.size)
        }
      }
    }

  }




}
