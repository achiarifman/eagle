package actor.image

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import actor.AbstractActor
import actor.message.{PostFindImageDiffMessage, ImagesDiffResultMessage, PreFindImageDiffMessage, PreScanImagesMessage}
import com.eagle.dao.entity.ImageDiff
import config.{PropsConst, EagleProps}
import magick.ImageMagickCompare
import util.{ImageUtils, FileUtils}

import scala.collection.immutable.HashMap
import scala.collection.mutable

/**
 * Created by Achia.Rifman on 13/12/2014.
 */
class ImageDiffActor extends AbstractActor with FileUtils{

  val SPLITTED_IMG_FOLDER = EagleProps.config.getString(PropsConst.IMG_SPLITTED_FOLDER)
  val DIFF_IMG_FOLDER = EagleProps.config.getString(PropsConst.IMG_DIFF_FOLDER)
  val NEEDED_PERCENTAGE = EagleProps.config.getInt(PropsConst.NEEDED_PERCENTAGES)
  val CORNER_U_L = "U_L"
  val CORNER_U_R = "U_R"
  val CORNER_D_L = "D_L"
  val CORNER_D_R = "D_R"

  def receive = {
    case (message : PreFindImageDiffMessage) => {
      execute(message)
    }
  }

  def execute(message : PreFindImageDiffMessage) = {

    //val diffList = generateDiffImages(message)
    val outPutFolder = createOutputFolder(DIFF_IMG_FOLDER + message.id)
    var segmentResultList : mutable.HashMap[Int,List[ImageDiff]] = mutable.HashMap()
    message.capturedSegmentsFolder.foreach(s => {
      val filesList = getListOfFiles(s).toList
      val numOfFiles = filesList.size
      val segmentNum = s.substring(s.lastIndexOf(File.separator) + 1).toInt
      val outPutSegmentFolder = createOutputFolder(outPutFolder + File.separator + segmentNum)
      var picResultList: mutable.MutableList[ImageDiff] = mutable.MutableList.empty[ImageDiff]
      for( a <- 1 to numOfFiles - 1){
        val image1 = s + File.separator + a + ".png"
        val next = a + 1
        val image2 = s + File.separator + next + ".png"
        val result = isImagesEquals(segmentNum,image1,image2,outPutSegmentFolder,a,next)
        picResultList += result
      }
      segmentResultList += (segmentNum -> picResultList.sortBy(f => f.id).toList)
    })
    sender() ! PostFindImageDiffMessage(message.id,true,segmentResultList.toMap, message.segments)
  }



  def isImagesEquals(segId : Int, image1 :String, image2 : String , outputFolder : String , img1Id : Int, img2Id : Int)  =  {

    val diffFile = outputFolder + File.separator + img1Id + "_" + img2Id + ".png"
    val imageMagick = new ImageMagickCompare
    val result = imageMagick.compare(image1,image2,diffFile)
    if(result){
     isImageDiff(segId,diffFile, img1Id, img2Id)
    }else{
      ImageDiff(segId,img1Id,img2Id,false,false,false,false,diffFile,0,0)
    }
  }

  def isImageDiff(segId : Int,imagePath : String, img1Id : Int, img2Id : Int) = {

    val image = ImageIO.read(new File(imagePath))
    val width = image.getWidth
    val height = image.getHeight
    val ratio = ImageUtils.numToRatio(width / height)

    val cornerSize = getCornerSize(width,height,ratio)
    val leftUpResult = scanImageCorner(CORNER_U_L,image,cornerSize)
    val rightUpResult = scanImageCorner(CORNER_U_R,image,cornerSize)
    val leftDownResult = scanImageCorner(CORNER_D_L,image,cornerSize)
    val rightDownResult = scanImageCorner(CORNER_D_R,image,cornerSize)
    ImageDiff(segId,img1Id,img2Id,leftUpResult,rightUpResult,leftDownResult,rightDownResult,imagePath,cornerSize._1,cornerSize._2)
    //val cornersMap = Map[String,Boolean](CORNER_U_L -> leftUpResult, CORNER_U_R -> rightUpResult, CORNER_D_L -> leftDownResult, CORNER_D_R -> rightDownResult)
    //cornersMap

  }

  def scanImageCorner(cornerType : String, image : BufferedImage, sizeTuple : (Int,Int)) = {

    cornerType match {
      case CORNER_D_L => {
        scan(image,0,image.getHeight - sizeTuple._2,sizeTuple._1,sizeTuple._2)
      }
      case  CORNER_D_R => {
        scan(image,image.getWidth - sizeTuple._1,image.getHeight - sizeTuple._2,sizeTuple._1,sizeTuple._2)
      }
      case  CORNER_U_L => {
        scan(image,0,0,sizeTuple._1,sizeTuple._2)
      }
      case CORNER_U_R => {
        scan(image,image.getWidth - sizeTuple._1,0,sizeTuple._1,sizeTuple._2)
      }
    }

  }

  def scan(image : BufferedImage, startX : Int, startY : Int, w :Int, h : Int) = {
    val pixels = image.getRGB(startX,startY,w,h,null,0,w).toList
    var blackPixels = 0
    pixels.foreach(p => {
      if(p == Color.BLACK.getRGB){
        blackPixels += 1
      }
    })
    val neededPixels = pixels.size * NEEDED_PERCENTAGE / 100 // find how much is NEEDED_PERCENTAGE from the whole pixels
    if(blackPixels >= neededPixels){
      true
    }else {
      false
    }
  }

  def getCornerSize(width : Int, height : Int, ratio : String) : (Int,Int) = {

    ratio match  {

      case ImageUtils.R16_9 => {
        val h = height / 4
        val w = width / 4
        (w,h)

      }

      case ImageUtils.R21_9 => {
        val h = height / 4
        val w = width / 4
        (w,h)
      }

      case ImageUtils.R4_3 => {
        val h = height / 4
        val w = width / 4
        (w,h)
      }

    }

  }

}
