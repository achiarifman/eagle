package magick

import java.awt.image.BufferedImage
import java.util.regex.{Pattern, Matcher}
import org.slf4j.LoggerFactory
import scala.sys.process._


/**
 * Created by Achia.Rifman on 12/12/2014.
 */
class ImageMagickCompare {

  val COMPARE = "compare "
  val COMPOSE = "-compose "
  val SRC = "src "
  val DEBUG = "-debug "
  val EXCEPTION = "Exception "
  val BLACK_WHITE = "-highlight-color White -lowlight-color Black "
  val stringBuilder: StringBuilder = new StringBuilder
  val LOGGER = LoggerFactory.getLogger(classOf[ImageMagickCompare])
  var isStarted: Boolean = false
  var isFailed: Boolean = false
  val track: Pattern = Pattern.compile("")
  val failedCapture: Pattern = Pattern.compile(".*\\bException\\b.*")

  def compare(image1 : String, image2 : String, output : String) : Boolean = {

    stringBuilder.append(DEBUG).append(EXCEPTION).append(image1 + " ").append(image2 + " ").append(COMPOSE).append(SRC).append(BLACK_WHITE).append(output)
    //compare -debug Exception spider.jpg spider1.jpg -compose src diff.jpeg
    val pLogger = getLogger
    val command = COMPARE + stringBuilder.toString()
    isStarted = true
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
      LOGGER.error("There was an error to do image diff")
      false
    }

  }

  def getLogger = {
    ProcessLogger(line => {
      println(line)
    },
      line => {
        var mTrack: Matcher = track.matcher(line)
        /*if (mTrack.matches) {
          println("capturing -> " + line)
          isStarted = true
        }*/
        mTrack = failedCapture.matcher(line)
        if (mTrack.matches) {
          println("****Could not capture the images***** - >" + line)
          isFailed = true
        }
      }
    )
  }


}
