package actor.ads

import java.io.File

import actor.AbstractActor
import actor.message.{PreOnStartEmbedMessage, PostAdEmbederMessage, PreAdEmbederMessage}
import com.eagle.dao.{JobDao, AdDao}
import com.eagle.dao.entity.AdToEmbed
import config.{PropsConst, EagleProps}
import ffmpeg.{FFmpegOnStartEmbeder, FFmpegEmbeder}
import util.FileUtils

/**
 * Created by Achia.Rifman on 09/01/2015.
 */
class AdsEmbederActor extends AbstractActor with FileUtils{

  val EMBED_OUTPUT : String = EagleProps.config.getString(PropsConst.EMBED_OUTPUT)

  def receive = {
    case(message : PreAdEmbederMessage) => {

      execute(message)
    }
    case(message : PreOnStartEmbedMessage) => executeOnStart(message)
  }

  def execute(message : PreAdEmbederMessage) = {
    val outPutFile = createOutputFileAndFolder(message.id)
    val ffmpegEmbeder = new FFmpegEmbeder(message.sourceFilePath,message.matchedAdList,outPutFile)
    val result = ffmpegEmbeder.embedInVideo()
    //persist ads embeded to db
    if(result){
      persistEmbededAds(message.matchedAdList,message.id)
    }
      val filePath = if(result) outPutFile else message.sourceFilePath
      sender() ! PostAdEmbederMessage(message.id,result,filePath)
  }

  def executeOnStart(message : PreOnStartEmbedMessage) = {
    val outPutFile = createOutputFileAndFolder(message.id)
    val ffmpeg = new FFmpegOnStartEmbeder(message.sourceFilePath,message.adsPath,outPutFile)
    val result = ffmpeg.execute
    val filePath = if(result) outPutFile else message.sourceFilePath
    sender() ! PostAdEmbederMessage(message.id,result,filePath)
  }

  def createOutputFileAndFolder(id : String) = {
    val outPutFolder = createOutputFolder(EMBED_OUTPUT + id)
    val outPutFile = outPutFolder + File.separator + id + ".mp4"
    outPutFile
  }

  def persistEmbededAds(ads : List[AdToEmbed], jobId : String) = {
    ads.foreach(ad => AdDao.persistAd(jobId,JobDao.getJobById(jobId).programId,ad.startTime.toString,ad.endTime.toString,ad.cornerType))
  }
}
