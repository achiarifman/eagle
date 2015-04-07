package actor

import java.nio.file.Paths

import actor.ads.{ImageScannerActor, CaptureImagesActor}
import actor.consts.ActorsTypes
import actor.image.ImageDiffActor
import actor.message._
import akka.actor.{ActorRef, Actor, ActorLogging, Props}
import akka.routing.{SmallestMailboxRoutingLogic, Router, ActorRefRoutee}
import com.eagle.dao.JobDao
import com.eagle.entity.EagleRecordEntity
import com.eagle.dao.entity.{EagleRecordJob, FailedEntity}
import config.{PropsConst, EagleProps}
import org.bson.types.ObjectId
import util.Alert
import scala.collection.mutable.Stack
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by Achia.Rifman on 12/09/2014.
 */
class ActorsManager extends Actor with ActorLogging{

  val cleanDelay = EagleProps.config.getInt(PropsConst.CLEAN_DELAY)
  val cleanInterval = EagleProps.config.getInt(PropsConst.CLEAN_INTERVAL)
  val fileCleanActor = context.actorOf(Props[CleanActor])
  context.system.scheduler.schedule(new FiniteDuration(cleanDelay.toInt,MINUTES),
    new FiniteDuration(cleanInterval.toInt,MINUTES), fileCleanActor, FilesCleanMessage())

  val recordRouter = {
    val routees = Vector.fill(4){
      val r = context.actorOf(Props[RecordActor])
      context watch r
      ActorRefRoutee(r)
    }
    Router(SmallestMailboxRoutingLogic(), routees)
  }

  val uploadRouter = {
    val routees = Vector.fill(2){
      val r = context.actorOf(Props[UploadActor])
      context watch r
      ActorRefRoutee(r)
    }
    Router(SmallestMailboxRoutingLogic(), routees)
  }

  val adRouter = {
    val routees = Vector.fill(2){
      val r = context.actorOf(Props[AdActor])
      context watch r
      ActorRefRoutee(r)
    }
    Router(SmallestMailboxRoutingLogic(), routees)
  }

  val publishRouter = {
    val routees = Vector.fill(2){
      val r = context.actorOf(Props[PublishActor])
      context watch r
      ActorRefRoutee(r)
    }
    Router(SmallestMailboxRoutingLogic(), routees)
  }


  def receive = {

    case (init : EagleRecordJob) => {
      log.info("Got new job, starting to process is")
      initRecordJob(init)
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
        JobDao.updatePublishUrl(postUploadMessage.id,postUploadMessage.publishUrl)
        updateJobActorList(postUploadMessage.id.toString)
        val persistedJobEntity = JobDao.getJobById(postUploadMessage.id.toString)
        mapAndSendMessage(persistedJobEntity)
      }
    }
    case (eagleRecordJob: EagleRecordEntity, it: Stack[ActorRef], failed: FailedEntity) => {
      handleFailedJob(eagleRecordJob,failed)
    }

    case(postAdEmbederMessage : PostAdEmbederMessage) => {
      updateJobActorList(postAdEmbederMessage.id)
      JobDao.updateEmbedAds(postAdEmbederMessage.id,postAdEmbederMessage.success)
      JobDao.updateEmbeddedPath(postAdEmbederMessage.id,postAdEmbederMessage.outputPath)
      if(postAdEmbederMessage.success){
       println("AD embed Finished!!!!!")
      }else{
        log.warning("AD embed failed!!!!")
        Alert.sendAlertMail(Alert.SUBJECT_ERROR_EMBED,"There was an error while embedding ads in job " + postAdEmbederMessage.id)
      }
      val persistedJobEntity = JobDao.getJobById(postAdEmbederMessage.id.toString)
      mapAndSendMessage(persistedJobEntity)
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

  def initRecordJob(initRecordJob : EagleRecordJob){

    recordRouter.route(PreRecordMessage(initRecordJob.id,initRecordJob.recordUrl,initRecordJob.recordDuration,initRecordJob.channelName),self)
    log.info("Initializing a new Job")
  }

  def updateJobActorList(id : String) = {
    val jobEntity = JobDao.getJobById(id)
    val (finished,waiting) = (jobEntity.waitingActorList.head, jobEntity.waitingActorList.tail)
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
         uploadRouter.route(PreUploadMessage(job.id,Paths.get(job.finalOutPutPath)),self)
        }
        case ActorsTypes.AD_ACTOR => {
          adRouter.route(InitAdMessage(job.id, job.adsPath, job.recordOutPutPath, job.recordDuration),self)
        }
        case ActorsTypes.PUBLISH_ACTOR => {
          publishRouter.route(PrePublishMessage(job.id,job.callBackUrl,"finish",job.adsPath.size, job.publishUrl, job.programId),self)
        }
      }
    }

  }






}
