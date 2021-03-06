package ffmpeg

import java.io.{OutputStream, File}
import java.nio.file.Paths
import java.nio.file.Path
import java.util.Scanner
import java.util.concurrent.TimeoutException
import java.util.regex.{Matcher, Pattern}
import com.eagle.consts.{FFmpegConst, VIDEO_CODEC, FFMPEG_CMD}
import com.eagle.entity.EagleRecordEntity
import org.bson.types.ObjectId
import org.slf4j.{LoggerFactory, Logger}
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.context.annotation.Scope
import scala.concurrent.duration._
import scala.concurrent.{Future, SyncVar}
import scala.io._
import scala.sys.process._
import scala.concurrent._
import ExecutionContext.Implicits.global
/**
 * Created by Achia.Rifman on 13/09/2014.
 */

class FFmpegRecorder  extends BaseFFmpeg{

  val track: Pattern = Pattern.compile("^\\bframe\\b.*")
  val failedHostName: Pattern = Pattern.compile(".*\\bFailed to resolve hostname\\b.*")
  val LOGGER = LoggerFactory.getLogger(classOf[FFmpegRecorder])
  var retryCounter = 0
  val MAX_RETRY = 3
  var duration = 0
  def startRecording() : Boolean =  {

    val pLogger = ProcessLogger(line => {
      println(line)
    },
      line => {
        var mTrack: Matcher = track.matcher(line)
        if (mTrack.matches) {
          println("It is a frame line" + line)
          isStarted = true
        }//else {println(line)}
        mTrack = failedHostName.matcher(line)
        if (mTrack.matches) {
          println("****Could not reach the url*****" + line)
          if(retryCounter == MAX_RETRY){
            isFailed = true
          }else {
            retryCounter += 1
            startRecording()
          }

        }
      }
    )
    val command = FFMPEG + stringBuilder.toString()
    LOGGER.info("Executing -> " + command)
    val process = command.run(pLogger)
    val resultFuture = Future(blocking(process.exitValue()))
    val timeout =  duration + minInSeconds
    try{
      Await.result(resultFuture,timeout seconds)
    }catch {
      case te : TimeoutException => {
        println("there was a timeout in ffmpeg recorder")
        process.destroy()
      }
    }
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

  def init(jobId : String, url : String, duration : Int, outPutFilePath : String) : String ={

    this.duration = duration
    val outPutFile = outPutFilePath + ".mp4"
    appendPair(FFMPEG_CMD.INPUT, url)
    appendPair(FFMPEG_CMD.TIME_LIMIT, String.valueOf(duration))
    appendPair(FFMPEG_CMD.VIDEO_CODEC, VIDEO_CODEC.H_264)
    appendParam(FFmpegConst.SPACE + outPutFile)
    outPutFile
  }




}
