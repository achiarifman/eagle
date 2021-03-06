package actor.ads

import java.io.File
import javax.imageio.ImageIO

import actor.AbstractActor
import actor.message.{PostCaptureImageMessage, PreCaptureImageMessage}
import com.eagle.dao.JobDao
import config.{PropsConst, EagleProps}
import ffmpeg.FFmpegImageCapture
import org.bson.types.ObjectId
import util.FileUtils
import scala.concurrent.duration._

/**
 * Created by Achia.Rifman on 22/11/2014.
 */
class CaptureImagesActor extends AbstractActor with FileUtils{

  val splittedFolder = EagleProps.config.getString(PropsConst.IMG_SPLITTED_FOLDER)

  def receive = {
    case message : PreCaptureImageMessage => {

      execute(message)
    }

  }

  def execute(message : PreCaptureImageMessage) = {
    val segmentDuration = splitVideoByTime(message.videoDuration, message.adsList.size)
    JobDao.updateJobSegmentDuration(message.id,segmentDuration)
    val segmentsList = List.range(0,message.adsList.size)
    val outputFolder = createOutputFolder(splittedFolder + message.id)
    var result = true
    val capturedImages = segmentsList.map(s => {
      val startTime = secondsToTime(s * segmentDuration)
      val time = secondsToTime(segmentDuration)
      val folder = createOutputFolder(outputFolder + File.separator + s)
      result = result && captureImages(message.videoPathToCapture,startTime,time, folder)
      folder
    })
    val imageOption = capturedImages.headOption
    if(imageOption.isDefined) saveResolutions(message.id,imageOption.get)
    sender() ! PostCaptureImageMessage(message.id,result,capturedImages,segmentsList,segmentDuration)
  }

  def saveResolutions(id : String, folderPath : String ) = {
    val filesList = getListOfFiles(folderPath).toList
    val imageOption = filesList.headOption
    if(imageOption.isDefined){
      val image = ImageIO.read(imageOption.get)
      val width = image.getWidth
      val height = image.getHeight
      JobDao.updateJobResolution(id,width.toString,height.toString)
    }

  }
  
  def getCaptureImageDetails() = {
      val duration = Duration(20, SECONDS)
    duration.toString()
  }

  def splitVideoByTime(duration : Int, numOfAds : Int) = {
    val segmentLength : Int = duration / numOfAds
    segmentLength
  }

  def captureImages(videoSrc : String, startTime : String, time : String, outputFolder : String) = {

    val ffmpegImagesCapture = new FFmpegImageCapture(videoSrc,startTime,time,outputFolder,null)
    ffmpegImagesCapture.captureImages()
  }

  def secondsToTime(seconds : Long) = {
    getTimeFromDuration(Duration(seconds, SECONDS))
  }

  def getTimeFromDuration(duration: Duration ) = {
    val df = new java.text.SimpleDateFormat("HH:mm:ss")
    df.setTimeZone(java.util.TimeZone.getTimeZone("GMT"))
    df.format(duration.toMillis)
    //convertToTwoDigit(duration.toHours) + ":" + convertToTwoDigit(duration.toMinutes) + ":" + convertToTwoDigit(duration.toSeconds)
  }

  def convertToTwoDigit(long: Long) = {
    if (long.toString.size < 2){
      0 + long.toString
    }else{
      long.toString
    }
  }

}
