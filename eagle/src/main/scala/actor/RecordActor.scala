package actor

import akka.actor.ActorRef
import com.eagle.entity.EagleRecordEntity
import com.eagle.dao.entity.FailedEntity
import ffmpeg.FFmpegRecorder

import scala.collection.mutable.Stack

/**
 * Created by Achia.Rifman on 12/09/2014.
 */
class RecordActor extends AbstractActor{


  def receive = {

    case (eagleRecordJob: EagleRecordEntity, it: Stack[ActorRef]) => {
      val result = startFFmpegRecordProcess(eagleRecordJob)
      if (!result) {handleFailedRecording(eagleRecordJob, it)}
      else {handleSuccessRecording(eagleRecordJob, it)}
    }
    case (eagleRecordJob: EagleRecordEntity, it: Stack[ActorRef], failed: FailedEntity) => {
      handleFailedEntity(eagleRecordJob, it, failed)
    }
  }

  def startFFmpegRecordProcess(eagleRecordJob: EagleRecordEntity) :Boolean = {

    log.info("Start recording process")
    val ffmpegRecorder = new FFmpegRecorder
    ffmpegRecorder.init(eagleRecordJob)
    ffmpegRecorder.startRecording
  }

  def handleSuccessRecording(eagleRecordJob: EagleRecordEntity, it: Stack[ActorRef]) {
    log.info("Handling success recording")
    val act = it.pop()
    act !(eagleRecordJob, it)
  }

  def handleFailedRecording(eagleRecordJob: EagleRecordEntity, it: Stack[ActorRef]) {
    log.error("Handling failed recording")
    val failed = new FailedEntity("The record phase as failed")
    eagleRecordJob.setFailed(true, failed)
    val act = it.pop()
    act !(eagleRecordJob, it, failed)
  }



}
