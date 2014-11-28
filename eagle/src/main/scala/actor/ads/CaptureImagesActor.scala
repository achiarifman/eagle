package actor.ads

import actor.AbstractActor
import actor.message.CaptureImageMessage
import ffmpeg.FFmpegImageCapture
import org.bson.types.ObjectId

/**
 * Created by Achia.Rifman on 22/11/2014.
 */
class CaptureImagesActor extends AbstractActor{

  //will get it from db
  val segmentsList = (0,20,40)
  val segmentDuration = 20

  def receive = {
    case message : CaptureImageMessage => {

      captureImages(message)
    }

  }

  def getCaptureImageDetails() = {

  }

  def captureImages(message : CaptureImageMessage) = {

    // eache segment of pics should be in diff sub folder
    // we should save the array of the folders in db
    log.debug("Going to capture image")
    val startTime = "00:00:05"
    val sourceFilePath = "C:\\Users\\achia.rifman\\Videos\\Test\\sviaznoi_iz_otpuska1.mp4"
    val segDuration = "00:00:15"
    val jobId = new ObjectId()
    val outPutFolder = "C:\\Users\\achia.rifman\\Videos\\Test"
    val outPutFolderName = "pics"
    val ffmpegImagesCapture = new FFmpegImageCapture(sourceFilePath,startTime,segDuration,jobId.toString,outPutFolder,outPutFolderName)
    ffmpegImagesCapture.captureImages()
  }

}
