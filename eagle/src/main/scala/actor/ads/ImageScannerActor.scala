package actor.ads

import actor.AbstractActor
import actor.message.{PostScanImagesMessage, PreScanImagesMessage}
import com.eagle.dao.entity.{SegmentCandidates, ImageDiff}
import config.{PropsConst, EagleProps}
import util.FileUtils
import scala.util.control.Breaks._
import scala.collection.mutable


/**
 * Created by Achia.Rifman on 28/11/2014.
 */
class ImageScannerActor extends AbstractActor with FileUtils{

  //val diffFolder = EagleProps.config.getString(PropsConst.IMG_DIFF_FOLDER)

  def receive = {

    case (message : PreScanImagesMessage) =>{
      log.info("got PreScanImagesMessage going to process it")
      execute(message)
    }

  }

  def execute(message : PreScanImagesMessage) = {

    val resultMap = message.segmentsResultMap
    val segmentsCandidates = mutable.MutableList[SegmentCandidates]()
    message.segments.foreach(s => {
      val imageDiffList = resultMap.get(s)
      val segmentCandidate = analyzeSegmentCandidates(s,imageDiffList.get)
      segmentsCandidates += segmentCandidate
  })
    sender() ! PostScanImagesMessage(message.id,true, segmentsCandidates.toList)
  }

  def analyzeSegmentCandidates(segId : Int, imageDiffList : List[ImageDiff]) = {

    val leftDownCorner = imageDiffList.filter(i => i.leftDownCorner)
    val leftUpCorner = imageDiffList.filter(i => i.leftUpCorner)
    val rightDownCorner = imageDiffList.filter(i => i.rightDownCorner)
    val rightUpCorner = imageDiffList.filter(i => i.rightUpCorner)
    val ldCandidates = analyzeCorner(leftDownCorner)
    val luCandidates = analyzeCorner(leftUpCorner)
    val rdCandidates = analyzeCorner(rightDownCorner)
    val ruCandidates= analyzeCorner(rightUpCorner)
    SegmentCandidates(segId,luCandidates,ldCandidates,ruCandidates,rdCandidates)
  }

  def analyzeCorner(imageDiffList : List[ImageDiff]) = {

    //var finish = false
    //val fullImageList = mutable.MutableList[List[ImageDiff]]()
    if(!imageDiffList.isEmpty){
      analyzeFirstInCorner(imageDiffList.head,imageDiffList.drop(0))
    }else{
      List[List[ImageDiff]]()
    }


    /*fullImageList += imageCandidates.toList
    while(!finish){
      if(!nextImageCandidates.isEmpty && nextImageCandidates.size > 1){
        (imageCandidates,nextImageCandidates) = analyzeFirstInCorner(nextImageCandidates.head,nextImageCandidates.drop(0))
        fullImageList += imageCandidates.toList
      }else{
        finish = true
      }
    }*/

  }

  def analyzeFirstInCorner(first : ImageDiff,imageDiffList : List[ImageDiff]) : List[List[ImageDiff]]  = {

    var prev = first
    val (imageCandidates,nextImageCandidates) = imageDiffList.partition(i => {
      if(prev.PicTwoId == i.picOneId){
        prev = i
        true
      }else{
        false
      }
    })
    val imageCandidatesWithFirst = first :: imageCandidates
    if(nextImageCandidates.isEmpty){
      List[List[ImageDiff]](imageCandidatesWithFirst)
    }else{
      List[List[ImageDiff]](imageCandidatesWithFirst) ::: analyzeFirstInCorner(nextImageCandidates.head,nextImageCandidates.tail)
    }
  }

/*  def group[A](list: List[A], size: Int): List[List[A]] =
    list.foldLeft( (List[List[A]](),0) ) { (r,c) => r match {
      case (head :: tail, num) =>
        if (num < size)  ( (c :: head) :: tail , num + 1 )
        else             ( List(c) :: head :: tail , 1 )
      case (Nil, num) => (List(List(c)), 1)
    }
    }._1.foldLeft(List[List[A]]())( (r,c) => c.reverse :: r)

  def getScanDetails = {

  }

  def scanImages = {

  }*/

}
