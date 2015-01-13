package ffmpeg

import java.io.{FileInputStream, File}
import java.util.regex.{Pattern, Matcher}

import com.eagle.dao.entity.ProbeMediaInfo
import com.fasterxml.jackson.databind.ObjectMapper

import org.slf4j.LoggerFactory

import scala.sys.process._


/**
 * Created by Achia.Rifman on 10/01/2015.
 */
object FFProbeInfo extends BaseFFmpeg("",""){

  val FFPROBE = "ffprobe"

  val KEY_V = " -v "
  val QUIET = "quiet"
  val kEY_PRINT_F = " -print_format "
  val JSON = "json"
  val KEY_SHOW_STREAMS = " -show_streams "
 // val track : Pattern = Pattern.compile("^\\b" + DURATION + "\\b.*")
 // val error: Pattern = Pattern.compile(".*\\bError\\b.*")
  val LOGGER = LoggerFactory.getLogger(this.getClass)

  def getFileInfo(filePath : String) = {
    val (pLogger,jsonFile) = getFileLogger
    val command = getCommand(filePath)
    LOGGER.info("Executing -> " + command)
    command lineStream_!(pLogger)
    if (isStarted && !isFailed){
      LOGGER.info("Could not find more lines")
      val mapper = new ObjectMapper()
      val probeMediaInfo = mapper.readValue(jsonFile,classOf[ProbeMediaInfo])
      Option(probeMediaInfo)
    }
    else if (isStarted && isFailed){
      LOGGER.error("Could not reach the url")
      None
    }
    else {
      LOGGER.error("There was an error to FFProbe media info")
      None
    }


  }

/*  def getLogger() = {
    ProcessLogger(line => {
      println(line)
    },
      line => {
        var mTrack: Matcher = track.matcher(line)
        if (mTrack.matches) {
          println("capturing -> " + line)
          isStarted = true
        }
        mTrack = error.matcher(line)
        if (mTrack.matches) {
          println("****Could not capture the images***** - >" + line)
          isFailed = true
        }
      }
    )
  }*/

  def getFileLogger() = {
    val file = new File("log_" + System.currentTimeMillis() + ".json")
    (ProcessLogger(file),file)
  }

  def getCommand(filePath : String) = {

    appendPair(KEY_V, QUIET)
    appendPair(kEY_PRINT_F,JSON)
    appendPair(KEY_SHOW_STREAMS, filePath)
    FFPROBE + stringBuilder.toString()
  }
}
