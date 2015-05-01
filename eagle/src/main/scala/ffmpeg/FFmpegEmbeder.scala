package ffmpeg

import java.io.File
import java.util.regex.{Matcher, Pattern}

import com.eagle.consts.FFmpegConst
import com.eagle.dao.entity.AdToEmbed
import org.slf4j.LoggerFactory
import scala.sys.process._

/**
 * Created by Achia.Rifman on 10/01/2015.
 */
class FFmpegEmbeder(val sourceFilePath : String, val embedVideos : List[AdToEmbed], outPutPath : String) extends BaseFFmpeg{


  val track : Pattern = Pattern.compile("^\\bframe\\b.*")
  val error: Pattern = Pattern.compile(".*\\bError\\b.*")
  val LOGGER = LoggerFactory.getLogger(classOf[FFmpegEmbeder])

  val TOP_RIGHT = "overlay=main_w-overlay_w-10:10"
  val BOTTOM_RIGHT = "overlay=main_w-overlay_w-10:main_h-overlay_h-10"
  val TOP_LEFT = "overlay=10:10"
  val BOTTOM_LEFT = "overlay=10:main_h-overlay_h-10"
  val SCALE = "scale=iw/10:ih/10 "
  val FILTER_COMPLEX = " -filter_complex "
  val BETWEEN = ":enable='between(t,%s,%s)'"
  val TMP = "tmp"
  val PIP = "pip"

  def embedInVideo() = {

    init()
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
          println("****Could not embed media***** - >" + line)
          isFailed = true
        }
      }
    )
    val command = FFMPEG + stringBuilder.toString()
    val commandInBytes = command.getBytes("UTF-8"); // Correct.
    val stringUsingUTF8 = new String(commandInBytes, "UTF-8"); // Correct.
    LOGGER.info("Executing -> " + stringUsingUTF8)
    stringUsingUTF8 lineStream_!(pLogger)
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

  def init() {

    appendPair(FFMPEG_COMMAND.INPUT, sourceFilePath)
    embedVideos.foreach(a => {
      appendPair(FFMPEG_COMMAND.ITS_OFFSET, a.startTime.toString)
      appendParam(FFMPEG_COMMAND.INPUT + a.adPath)
    })
    var filterValue : String = "\""
    for(i <-1 to embedVideos.size){
      filterValue = filterValue.concat(buildEmbedFilter(embedVideos(i-1),i))
    }
    appendPair(FILTER_COMPLEX, filterValue.concat("\""))
    appendParam(FFmpegConst.SPACE + outPutPath)
  }

  def buildEmbedFilter(adToEmbed: AdToEmbed,index : Int) = {
    val pip =  "[" + PIP + index + "]"
    val prevIndex = index - 1
    val prevTmp = {if (prevIndex > 0) "[" + TMP + prevIndex + "]" else "[" + prevIndex + "]" }
    val corner = adToEmbed.cornerType
    val between = String.format(BETWEEN,adToEmbed.startTime.toString,adToEmbed.endTime.toString)
    val tmp = {if (prevIndex > 0) "[" + TMP + index + "]" else "" }
    val filter = "[" + index + "]" + SCALE + pip + "; " + prevTmp + pip + " " + getLocation(corner) + between
    if(prevIndex > 0){
      " [" + TMP + prevIndex + "];" + filter
    }else{
      filter
    }
  }

  /*-filter_complex "[1]scale=iw/3:ih/3 [pip1]; [0][pip1] overlay=main_w-overlay_w-10:main_h-overlay_h-10:enable='between(t,3,10)' [tmp1];
   [2]scale=iw/5:ih/5 [pip2]; [tmp1][pip2] overlay=10:main_h-overlay_h-10:enable='between(t,11,19)'" PIP_output1.mp4*/


  def getLocation(corner: String) = {

    corner match {

      case "lu" => TOP_LEFT
      case "ld" => BOTTOM_LEFT
      case "ru" => TOP_RIGHT
      case "rd" => BOTTOM_RIGHT
    }
  }
}
