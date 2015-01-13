package actor.ads

import actor.AbstractActor
import actor.message.{PostBeforeEmbedMessage, PreBeforeEmbedMessage}
import akka.actor.Actor.Receive
import com.eagle.dao.entity.{SegmentCandidates, AdToEmbed, ImageDiff}
import ffmpeg.FFmpegMediaInfo

import scala.collection.mutable

/**
 * Created by Achia.Rifman on 09/01/2015.
 */
class BeforeEmbedActor  extends AbstractActor{



  def receive = {
     case message : PreBeforeEmbedMessage => {

       execute(message)
     }
   }

  def execute(message: PreBeforeEmbedMessage) = {
    val numOfSeg = message.segmentsCandidates.size
    val chosenCorners = message.segmentsCandidates.map(f => {
      val allCorners = getMaxCandidates(f)
      if(!allCorners.isEmpty){
        val chosenCorner = allCorners.maxBy(_._1.size)
        chosenCorner
      }else{
        // handle empty list
        (List[ImageDiff](),"")
      }
    })
    val matchedAds = attachAdToCorner(chosenCorners,message.adsPath,message.segmentDuration)
    sender() ! PostBeforeEmbedMessage(message.id,true,matchedAds )
  }

  def getMaxCandidates(segmentCandidate : SegmentCandidates) : List[(List[ImageDiff],String)] = {

    val corners = mutable.MutableList[(List[ImageDiff],String)]()

    if(!segmentCandidate.ldCandidates.isEmpty){
      corners += ((segmentCandidate.ldCandidates.maxBy(_.size),"ld"))
    }
    if(!segmentCandidate.luCandidates.isEmpty){
      corners += ((segmentCandidate.luCandidates.maxBy(_.size),"lu"))
    }
    if(!segmentCandidate.rdCandidates.isEmpty){
      corners += ((segmentCandidate.rdCandidates.maxBy(_.size),"rd"))
    }
    if(!segmentCandidate.ruCandidates.isEmpty){
      corners += ((segmentCandidate.ruCandidates.maxBy(_.size),"ru"))
    }
    corners.toList
  }

  def attachAdToCorner(imagesList : List[(List[ImageDiff],String)], adsPath : List[String] , segmentDuration : Int) = {
    val adsWithDuration : List[(Long,String)] = adsPath.map(a => {
      if(isImageAd(a)){
        (1.toLong,a) //we put one because it is an image
      }else{
        val adInfo = getAdMediaInfo(a)
        (adInfo.mediaDuration.toSeconds,a)
      }
    })
    val sortedAds = adsWithDuration.sortWith(_._1 > _._1)
    val sortedCorners = imagesList.sortWith(_._1.size > _._1.size)
    val zippedList = sortedCorners zip sortedAds
    val (readyPairs,notMatches) = zippedList.partition(p => p._1._1.size >= p._2._1)
    val adImageList : List[AdToEmbed] = readyPairs.map(f => {
      val times = getStartEndFromImageList(f._1._1,segmentDuration)
      AdToEmbed(f._2._2,times._1,times._2,f._1._2,f._1._1.head.cornerWidth,f._1._1.head.cornerHeight)
    })
    if(!notMatches.isEmpty){
      val unMatchedSolved : List[AdToEmbed]= handleUnMatchedAds(notMatches,sortedAds).map(f => {
        val times = getStartEndFromImageList(f._1._1,segmentDuration)
        AdToEmbed(f._2._2,times._1,times._2,f._1._2,f._1._1.head.cornerWidth,f._1._1.head.cornerHeight)
      })
      adImageList ::: unMatchedSolved
    }else{
      adImageList
    }
  }

  def getAdMediaInfo(adPath : String) = {
    val ffmpegMediaInfo = new FFmpegMediaInfo(adPath)
    ffmpegMediaInfo.getMediaInfo()
  }

  def isImageAd(adPath : String) = {
    val ad = adPath.toLowerCase
    if(adPath.endsWith(".png") || adPath.endsWith(".jpg")) true else false
  }

  def getStartEndFromImageList(images : List[ImageDiff], segmentDuration : Int) = {

    val firstImage = images.head
    val segment = firstImage.id
    val startTime = segment * segmentDuration + images.head.picOneId
    val endTime = segment * segmentDuration + images.last.PicTwoId
    (startTime,endTime)
  }

  def handleUnMatchedAds(unMatched :  List[((List[ImageDiff], String), (Long, String))], allAds : List[(Long, String)]) = {
    val imagesList = unMatched.map(f => f._1)
    val matched = imagesList.map(f => {
      (f,allAds.find(p => p._1 <= f._1.size))
    }).filter(f => f._2.get != None).map(z => (z._1,z._2.get)) // need to check there is a problem
    matched
  }
}
