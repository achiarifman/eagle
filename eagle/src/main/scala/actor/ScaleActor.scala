package actor

import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO

import actor.message.{PreScaleImageMessage, PostScaleMessage, PreScaleMessage}
import akka.actor.Actor.Receive
import com.eagle.dao.JobDao
import config.{PropsConst, EagleProps}
import ffmpeg.FFmpegScale
import org.imgscalr.Scalr
import util.FileUtils

/**
 * Created by Achia.Rifman on 09/04/2015.
 */
class ScaleActor extends AbstractActor with FileUtils {

  val SCALED_OUTPUT : String = EagleProps.config.getString(PropsConst.SCALED_OUTPUT)

  override def receive: Receive = {
    case message : PreScaleMessage => execute(message)

    case message : PreScaleImageMessage =>  executeScaleImage(message)
  }

  def execute(message : PreScaleMessage) = {
    val fileName = Paths.get(message.source).getFileName
    val output = SCALED_OUTPUT + message.id + "_" +fileName + ".mp4"
    val ffmpeg = new FFmpegScale(message.source,output,message.height,message.width)
    val result = ffmpeg.execute
    if(result){
      JobDao.addScaledAd(message.id,output)
      sender() ! PostScaleMessage(message.id,true,output)
    }else{
      sender() ! PostScaleMessage(message.id,false,output)
    }
  }

  def executeScaleImage(message : PreScaleImageMessage) = {

    val image = ImageIO.read(new File(message.source))
    val resizedImage = resizeImage(image,Scalr.Method.ULTRA_QUALITY,message.width.toInt,message.height.toInt)
    val fileName = Paths.get(message.source).getFileName
    val output = SCALED_OUTPUT + message.id + "_" + fileName + ".mp4"
    val result = ImageIO.write(resizedImage,"JPEG", new File(output))
    if(result){
      JobDao.addScaledAd(message.id,output)
      sender() ! PostScaleMessage(message.id,true,output)
    }else{
      sender() ! PostScaleMessage(message.id,false,output)
    }
  }

  def resizeImage(src: BufferedImage, scalingMethod: Scalr.Method, targetWidth: Int, targetHeight: Int): BufferedImage = {
    return Scalr.resize(src, scalingMethod, targetWidth, targetHeight)
  }

}
