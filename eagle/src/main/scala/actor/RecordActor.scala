package actor

import java.io.File
import java.nio.file.Paths

import actor.message.{PostRecordMessage, RecordFailedMessage, PreRecordMessage}
import akka.actor.ActorRef
import com.eagle.consts.FFmpegConst
import com.eagle.dao.JobDao
import com.eagle.entity.EagleRecordEntity
import com.eagle.dao.entity.FailedEntity
import config.{PropsConst, EagleProps}
import ffmpeg.{FFProbeInfo, FFmpegRecorder}
import org.bson.types.ObjectId
import util.FileUtils

import scala.collection.mutable.Stack

/**
 * Created by Achia.Rifman on 12/09/2014.
 */
class RecordActor extends AbstractActor with FileUtils{

  val OUTPUT_FOLDER: String = EagleProps.config.getString(PropsConst.RECORD_OUTPUT)


  def receive = {

    case (message: PreRecordMessage) => {
      startFFmpegRecordProcess(message,sender())
    }
  }

  def startFFmpegRecordProcess(message: PreRecordMessage,theSender : ActorRef) = {

    log.info("Start recording process")
    val ffmpegRecorder = new FFmpegRecorder
    val outPutFolder = OUTPUT_FOLDER + File.separator + message.id
    createOutputFolder(outPutFolder)
    val outPutFilePath = outPutFolder + File.separator +  message.id.toString
    val filePath = ffmpegRecorder.init(message.id.toString, message.url, message.duration, outPutFilePath)
    val result = ffmpegRecorder.startRecording
    if (result) {
      log.info("Handling success recording")
      theSender ! new PostRecordMessage(message.id,filePath,true)
    }
    else {
      log.error("Handling failed recording")
      theSender ! new PostRecordMessage(message.id,"record failed",false)
    }
  }

/*  def createOutputFolder(folderName: String) {
    new File(folderName).mkdir
  }*/

/*  def handleSuccessRecording(id : String, outPutFile : String) {
    log.info("Handling success recording")
    sender() ! new PostRecordMessage(id,outPutFile,true)
  }

  def handleFailedRecording(id : String) {
    log.error("Handling failed recording")
    sender() ! new PostRecordMessage(id,"record failed",false)
  }*/



}
