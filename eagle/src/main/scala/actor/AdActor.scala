package actor

import actor.ads.{AdsEmbederActor, BeforeEmbedActor, ImageScannerActor, CaptureImagesActor}
import actor.consts.ActorsTypes
import actor.image.ImageDiffActor
import actor.message._
import akka.actor.{ActorSystem, Props, ActorRef}
import com.eagle.dao.JobDao
import com.eagle.entity.EagleRecordEntity

import scala.collection.mutable.Stack

/**
 * Created by Achia.Rifman on 13/09/2014.
 */
class AdActor extends AbstractActor{


  val captureImagesActor = context.actorOf(Props(new CaptureImagesActor),ActorsTypes.CAPTURE_IMAGES_ACTOR)
  val imageDiffActor = context.actorOf(Props(new ImageDiffActor),ActorsTypes.IMAGE_DIFF_ACTOR)
  val imageScannerActor = context.actorOf(Props(new ImageScannerActor), ActorsTypes.IMAGE_SCANNER_ACTOR)
  val beforeEmbedActor = context.actorOf(Props(new BeforeEmbedActor), ActorsTypes.BEFORE_EMBED_ACTOR)
  val adsEmbederActor = context.actorOf(Props(new AdsEmbederActor), ActorsTypes.ADS_EMBEDER_ACTOR)
  val scaleActor = context.actorOf(Props(new ScaleActor), ActorsTypes.SCALE_ACTOR)
  val MAX_NEEDED_PERCENTAGE = 90
  val PERCENTAGE_REDUCE = 10
  val MIN_NEEDED_PERCENTAGE = 10

  def receive = {

    case (adJob: InitAdMessage) => {
      captureImagesActor ! PreCaptureImageMessage(adJob.id,adJob.adsPath,adJob.videoDuration,adJob.sourceFilePath)
    }

    case (postCaptureImageMessage :PostCaptureImageMessage) => {
      if(postCaptureImageMessage.success){
        JobDao.updateNeededPercentage(postCaptureImageMessage.id,MAX_NEEDED_PERCENTAGE)
        JobDao.saveCapturedSegFolders(postCaptureImageMessage.id,postCaptureImageMessage.capturedSegFolders)
        JobDao.saveSegmentsList(postCaptureImageMessage.id,postCaptureImageMessage.segmentList)
        imageDiffActor ! PreFindImageDiffMessage(postCaptureImageMessage.id,postCaptureImageMessage.segmentList,postCaptureImageMessage.capturedSegFolders,MAX_NEEDED_PERCENTAGE)
      }else{
        // send fail to ActorManager
      }
    }

    case (postFindImageDiffMessage : PostFindImageDiffMessage) => {
      if(postFindImageDiffMessage.success){
        imageScannerActor ! PreScanImagesMessage(postFindImageDiffMessage.id,postFindImageDiffMessage.segmentsResultList,postFindImageDiffMessage.segments)
      }else{
        // send fail to ActorManager
      }
    }

    case(postScanImagesMessage : PostScanImagesMessage) => {
      if(postScanImagesMessage.success){
        val job = JobDao.getJobById(postScanImagesMessage.id)
        beforeEmbedActor ! PreBeforeEmbedMessage(postScanImagesMessage.id,postScanImagesMessage.segmentsCandidates,job.recordOutPutPath, job.adsPath,
          job.segmentDuration)
      }else{
        // send fail to ActorManager
      }
    }

    case(postBeforeEmbedMessage : PostBeforeEmbedMessage) => {
      val job = JobDao.getJobById(postBeforeEmbedMessage.id)
      if(postBeforeEmbedMessage.success){
        adsEmbederActor ! PreAdEmbederMessage(postBeforeEmbedMessage.id,postBeforeEmbedMessage.matchedAdList, job.recordOutPutPath)
      }else{
        if(job.neededPercentage > MIN_NEEDED_PERCENTAGE){
          val neededPercentage = job.neededPercentage - PERCENTAGE_REDUCE
          log.info("Could not find spots for ads, trying with lower percentages -> " + neededPercentage)
          JobDao.updateNeededPercentage(job.id,neededPercentage)
          imageDiffActor ! PreFindImageDiffMessage(job.id,job.segmentList,job.capturedSegFolders,neededPercentage)
        }else{
          job.adsPath.foreach(ad => scaleActor ! PreScaleMessage(job.id,ad,job.height,job.width))
        }
      }
    }

    case(message : PostScaleMessage) => {
      //val job = JobDao.getJobById(message.id)
      val jobOption = JobDao.increaseScaledAdCounter(message.id)
      if(jobOption.isDefined){
        if(jobOption.get.scaledCounter == jobOption.get.adsPath.size){
            adsEmbederActor ! PreOnStartEmbedMessage(message.id,jobOption.get.recordOutPutPath,jobOption.get.scaledAdsPaths)
        }else{
        }
      }
    }

    case(postAdEmbederMessage : PostAdEmbederMessage) => {
        val actorParent = context.parent
        actorParent ! postAdEmbederMessage
    }
  }

  /*def getVideoSegmentsCandidates(adJob: EagleRecordEntity) : List[Int] ={

    val duration = adJob.getDuration
    val numOfAds = adJob.getNumberOfAds
    val segmentLength : Int = duration / numOfAds
    val segments = List.range(0,duration,segmentLength)
    segments
  }*/

}
