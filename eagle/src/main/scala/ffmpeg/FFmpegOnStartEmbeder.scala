package ffmpeg

import java.util.regex.{Matcher, Pattern}
import org.slf4j.LoggerFactory
import scala.sys.process._

/**
 * Created by Achia.Rifman on 09/04/2015.
 */
class FFmpegOnStartEmbeder(val source : String, ads : List[String], val output : String) extends BaseFFmpeg{

  val track : Pattern = Pattern.compile("^\\bframe\\b.*")
  val error: Pattern = Pattern.compile(".*\\bError\\b.*")
  val LOGGER = LoggerFactory.getLogger(classOf[FFmpegOnStartEmbeder])

  def execute = {
    init
    val pLogger = ProcessLogger(line => {
      println(line)
    },
      line => {
        var mTrack: Matcher = track.matcher(line)
        if (mTrack.matches) {
          println("embeding -> " + line)
          isStarted = true
        }
        mTrack = error.matcher(line)
        if (mTrack.matches) {
          println("****Could not embed ads on start***** - >" + line)
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
    ads.foreach(ad => appendPair(FFMPEG_COMMAND.INPUT, ad))
    appendPair(FFMPEG_COMMAND.INPUT, source)
    appendPair(FFMPEG_COMMAND.FILTER_COMPLEX, "\"")
    val n = ads.size + 1
    for(i <-0 to n-1){
      appendPair("[" + i + ":0] ","[" + i + ":1] ")
    }
    appendParam(FFMPEG_COMMAND.CONCAT + n + ":v=1:a=1[v] [a]\" ")
    appendParam(" -map \"[v]\" -map \"[a]\" ")
    appendParam(output)
  }




}
