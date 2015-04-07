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

  def receive = {

    case (adJob: InitAdMessage) => {
      captureImagesActor ! PreCaptureImageMessage(adJob.id,adJob.adsPath,adJob.videoDuration,adJob.sourceFilePath)
    }

    case (postCaptureImageMessage :PostCaptureImageMessage) => {
      if(postCaptureImageMessage.success){
        imageDiffActor ! PreFindImageDiffMessage(postCaptureImageMessage.id,postCaptureImageMessage.segmentList,postCaptureImageMessage.capturedSegFolders)
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
      if(postBeforeEmbedMessage.success){
        val job = JobDao.getJobById(postBeforeEmbedMessage.id)
        adsEmbederActor ! PreAdEmbederMessage(postBeforeEmbedMessage.id,postBeforeEmbedMessage.matchedAdList, job.recordOutPutPath)
      }else{
        // send fail to ActorManager
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
