package ffmpeg

import java.io.File
import java.util.regex.{Pattern, Matcher}

import com.eagle.consts.{FFmpegConst, VIDEO_CODEC, FFMPEG_CMD}
import org.slf4j.LoggerFactory
import scala.sys.process._
import scala.reflect.io.Path


/**
 * Created by Achia.Rifman on 22/11/2014.
 */
class FFmpegImageCapture (val sourceFilePath : String, val startFrom : String, val time : String,
                          outPutFolder : String, outPutFolderName : String) extends BaseFFmpeg(outPutFolder,outPutFolderName) {

  val CAPTURE_INTERVAL = "1"
  val IMAGE_OUT = "%d.png"
  val track : Pattern = Pattern.compile("^\\bframe\\b.*")
  val error: Pattern = Pattern.compile(".*\\bError\\b.*")
  val LOGGER = LoggerFactory.getLogger(classOf[FFmpegImageCapture])

  def captureImages() = {
    init()
    val pLogger = ProcessLogger(line => {
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
    val command = FFMPEG + stringBuilder.toString()
    LOGGER.info("Executing -> " + command)
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
      LOGGER.error("There was an error to start capturing images")
      false
    }
  }

  def init() {
    appendPair(FFMPEG_COMMAND.INPUT, sourceFilePath)
    appendPair(FFMPEG_COMMAND.START_TIME, startFrom)
    appendPair(FFMPEG_COMMAND.TIME_LIMIT, time)
    appendPair(FFMPEG_COMMAND.FORMAT, FFMPEG_COMMAND.IMAGE2)
    appendPair(FFMPEG_COMMAND.VF, FFMPEG_COMMAND.IMAGE_FPS + CAPTURE_INTERVAL)
    appendParam(FFmpegConst.SPACE + outPutFolder + File.separator + IMAGE_OUT)
  }
}
