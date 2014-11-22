package ffmpeg

import java.io.{OutputStream, File}
import java.nio.file.Paths
import java.nio.file.Path
import java.util.Scanner
import java.util.regex.{Matcher, Pattern}
import com.eagle.consts.{FFmpegConst, VIDEO_CODEC, FFMPEG_CMD}
import com.eagle.entity.EagleRecordEntity
import org.slf4j.{LoggerFactory, Logger}
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.context.annotation.Scope

import scala.concurrent.SyncVar
import scala.io._
import scala.sys.process._
/**
 * Created by Achia.Rifman on 13/09/2014.
 */

class FFmpegRecorder {

  //@Value("${record.output}")  var OUTPUT_FOLDER: String = null
  val FFMPEG: String = "ffmpeg"
  var stringBuilder: StringBuilder = new StringBuilder
  var isStarted: Boolean = false
  var isFailed: Boolean = false
  val track: Pattern = Pattern.compile("^\\bframe\\b.*")
  val failedHostName: Pattern = Pattern.compile(".*\\bFailed to resolve hostname\\b.*")
  val LOGGER = LoggerFactory.getLogger(classOf[FFmpegRecorder])
  var outPutFolderPath : Path = _
  val inputStream = new SyncVar[OutputStream];
  var outPutFolderName : String = _

  def startRecording() : Boolean =  {

    val pLogger = ProcessLogger(line => {
      println(line)
    },
      line => {
        var mTrack: Matcher = track.matcher(line)
        if (mTrack.matches) {
          println("It is a frame line")
          isStarted = true
        }
        mTrack = failedHostName.matcher(line)
        if (mTrack.matches) {
          println("****Could not reach the url*****")
          isFailed = true
        }
      }
    )
    val command = FFMPEG + stringBuilder.toString()
    command lineStream_!(pLogger)
    if (isStarted && !isFailed){
      LOGGER.info("Could not find more lines")
      true
    }
    else if (isStarted && isFailed){
      LOGGER.error("Could not reach the url")
      false
    }
    else {
      LOGGER.error("There was an error to start the recording")
      false
    }

  }

  def appendPair(key: String, value: String) {
    stringBuilder.append(key)
    stringBuilder.append(value)
  }

  def appendParam(param: String) {
    stringBuilder.append(param)
  }

  def init(eagleRecordEntity : EagleRecordEntity ) : EagleRecordEntity ={

    appendPair(FFMPEG_CMD.INPUT, eagleRecordEntity.getUrl)
    appendPair(FFMPEG_CMD.TIME_LIMIT, String.valueOf(eagleRecordEntity.getDuration))
    appendPair(FFMPEG_CMD.VIDEO_CODEC, VIDEO_CODEC.H_264)
    outPutFolderName = eagleRecordEntity.getId.toString
    createOutputFolder(eagleRecordEntity.getOutputFolder + File.separator + eagleRecordEntity.getId.toString)
    appendParam(FFmpegConst.SPACE + outPutFolderPath + File.separator + eagleRecordEntity.getId.toString + FFmpegConst.UNDERSCORE + eagleRecordEntity.getChannelName + ".mp4")
    eagleRecordEntity.setOutPutFolderPath(outPutFolderPath)
    eagleRecordEntity
  }

  def createOutputFolder(folderName: String) {
    new File(folderName).mkdir
    outPutFolderPath = Paths.get(folderName)
  }



}