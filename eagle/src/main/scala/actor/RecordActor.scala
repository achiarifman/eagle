package actor

import java.io.File
import java.nio.file.Paths

import actor.message.{PostRecordMessage, RecordFailedMessage, PreRecordMessage}
import akka.actor.ActorRef
import com.eagle.consts.FFmpegConst
import com.eagle.entity.EagleRecordEntity
import com.eagle.dao.entity.FailedEntity
import config.{PropsConst, EagleProps}
import ffmpeg.FFmpegRecorder
import org.bson.types.ObjectId

import scala.collection.mutable.Stack

/**
 * Created by Achia.Rifman on 12/09/2014.
 */
class RecordActor extends AbstractActor{

  val OUTPUT_FOLDER: String = EagleProps.config.getString(PropsConst.RECORD_OUTPUT)


  def receive = {

    case (message: PreRecordMessage) => {
      startFFmpegRecordProcess(message)
    }
  }

  def startFFmpegRecordProcess(message: PreRecordMessage) = {

    log.info("Start recording process")
    val ffmpegRecorder = new FFmpegRecorder
    val outPutFolder = OUTPUT_FOLDER + File.separator + message.id
    createOutputFolder(outPutFolder)
    val outPutFilePath = outPutFolder + File.separator +  message.id.toString + FFmpegConst.UNDERSCORE + message.channelName
    val filePath = ffmpegRecorder.init(message.id.toString, message.url, message.duration, outPutFilePath)
    val result = ffmpegRecorder.startRecording
    if (!result) {
      handleFailedRecording(message.id)
    }
    else {
      handleSuccessRecording(message.id,filePath)
    }
  }

  def createOutputFolder(folderName: String) {
    new File(folderName).mkdir
  }

  def handleSuccessRecording(id : String, outPutFile : String) {
    log.info("Handling success recording")
    sender() ! new PostRecordMessage(id,outPutFile)
  }

  def handleFailedRecording(id : String) {
    log.error("Handling failed recording")
    sender() ! new RecordFailedMessage(id,"record failed")
  }



}
