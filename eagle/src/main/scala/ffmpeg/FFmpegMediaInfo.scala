package ffmpeg

import java.io.File
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.regex.{Pattern, Matcher}

import com.eagle.consts.FFmpegConst
import com.eagle.dao.entity.MediaInfo
import org.slf4j.LoggerFactory
import scala.concurrent.duration._
import scala.sys.process._
import scala.sys.process.ProcessLogger
import scala.io.Source._
/**
 * Created by Achia.Rifman on 09/01/2015.
 */
class FFmpegMediaInfo(val sourceFilePath : String) extends BaseFFmpeg("",""){

  val DURATION = "Duration"
  val track : Pattern = Pattern.compile("^\\b" + DURATION + "\\b.*")
  val error: Pattern = Pattern.compile(".*\\bError\\b.*")
  val LOGGER = LoggerFactory.getLogger(classOf[FFmpegMediaInfo])

  def getMediaInfo() = {

    var duration : Duration = null
    init()
    val filename = "temp_info_" +System.currentTimeMillis() + ".txt"
    val outputFile = new File(filename)
    val pLogger = ProcessLogger(outputFile)
    /*val pLogger = ProcessLogger(line => {
      println(line)

        var mTrack: Matcher = track.matcher(line)
        if (mTrack.matches) {
          println("reading output and found duration -> " + line)
          duration = extractDuration(line)
          isStarted = true
        }
        mTrack = error.matcher(line)
        if (mTrack.matches) {
          println("****Could not capture the images***** - >" + line)
          isFailed = true
        }
      }
    )*/
    val command = FFMPEG + stringBuilder.toString()
    LOGGER.info("Executing -> " + command)
    command lineStream_!(pLogger)
    val outPutlines = fromFile(outputFile).getLines()
    while(outPutlines.hasNext) {
      val line = outPutlines.next
      val mTrack: Matcher = track.matcher(line)
      if (mTrack.matches) {
        println("reading output and found duration -> " + line)
        duration = extractDuration(line)
        isStarted = true
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
      LOGGER.error("There was an error to get media info")
      false
    }

    MediaInfo(sourceFilePath,duration)
  }

  def extractDuration(line : String) = {
    // Duration: 00:00:20.00, start: 0.000000, bitrate: 5198 kb/s
    val rePlaced = line.replace(DURATION + " ", "")
    val strDuration = rePlaced.substring(0,rePlaced.indexOf(","))
    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT"))
    val date = sdf.parse("1970-01-01 " + strDuration);
    Duration(date.getTime,MILLISECONDS)
  }

  def init() {
    appendPair(FFMPEG_COMMAND.INPUT, sourceFilePath)
  }
}
