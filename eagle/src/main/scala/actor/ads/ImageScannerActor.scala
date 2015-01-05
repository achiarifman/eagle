package actor.ads

import actor.AbstractActor
import actor.message.PreScanImagesMessage
import config.{PropsConst, EagleProps}
import util.FileUtils


/**
 * Created by Achia.Rifman on 28/11/2014.
 */
class ImageScannerActor extends AbstractActor with FileUtils{

  val diffFolder = EagleProps.config.getString(PropsConst.IMG_DIFF_FOLDER)

  def receive = {

    case (message : PreScanImagesMessage) =>{
      execute(message)
    }

  }

  def execute(message : PreScanImagesMessage) = {

    val resultMap = message.segmentsResultMap
    //do loop

  }

  def getScanSetails = {

  }

  def scanImages = {

  }

}
