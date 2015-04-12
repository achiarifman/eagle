package ffmpeg

import java.util.regex.{Matcher, Pattern}
import com.eagle.consts.FFmpegConst
import org.slf4j.LoggerFactory
import scala.sys.process._


/**
 * Created by Achia.Rifman on 09/04/2015.
 */
class FFmpegScale(val source : String,
                  val output : String,
                  val height : String,
                  val width : String ) extends BaseFFmpeg{

  val track : Pattern = Pattern.compile("^\\bframe\\b.*")
  val error: Pattern = Pattern.compile(".*\\bError\\b.*")
  val LOGGER = LoggerFactory.getLogger(classOf[FFmpegScale])

  def execute = {

    init
    val pLogger = ProcessLogger(line => {
      println(line)
    },
      line => {
        var mTrack: Matcher = track.matcher(line)
        if (mTrack.matches) {
          println("scaling -> " + line)
          isStarted = true
        }
        mTrack = error.matcher(line)
        if (mTrack.matches) {
          println("****Could not scale media***** - >" + line)
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
      LOGGER.error("There was an error to start the embedding")
      false
    }
  }

  def init = {
    appendPair(FFMPEG_COMMAND.INPUT, source)
    val scale = "scale=" + width + ":" + height + ",setsar=1"
    appendPair(FFMPEG_COMMAND.VF,scale)
    appendParam(FFmpegConst.SPACE + output)
  }
}
