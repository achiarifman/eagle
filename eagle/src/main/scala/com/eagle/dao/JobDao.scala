package com.eagle.dao

import com.eagle.dao.entity.EagleRecordJob
import com.eagle.dao.persistanceContext._
import com.eagle.entity.EagleRecordEntity
import org.bson.types.ObjectId
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._
/**
 * Created by Achia.Rifman on 27/12/2014.
 */
object JobDao {

  def persistNewJob(eagleJob : EagleRecordEntity,actors : List[String], ads : List[String]) = {
    val t = transactional {

      val job = new EagleRecordJob(new ObjectId().toString,eagleJob.getUrl,eagleJob.getDuration,
        eagleJob.getChannelName,actors,ads,eagleJob.getCallBackUrl,eagleJob.getProgramId)
      job
    }
    t
  }

  def updateRecordOutputPath(path : String, id : String) = {

    transactional{
     val job =  byId[EagleRecordJob](id)
      transactional(nested) {
        job.get.recordOutPutPath  = path
      }
    }
  }


    def getJobById(id: String) = {
      val job = transactional {
        byId[EagleRecordJob](id)
      }
      job.get

  }
  def updateActorsList(id : String, finishedActor : String, waitingActors : List[String]) = {
    transactional{
      val jobOption =  byId[EagleRecordJob](id)
      transactional(nested) {
        val job = jobOption.get
          job.waitingActorList  = waitingActors
          job.finishedActorsList = job.finishedActorsList :+ finishedActor
        job
      }
    }
  }

  def updateUploadFolder(id : String, uploadFolder : String) = {
    transactional{
      val jobOption =  byId[EagleRecordJob](id)
      transactional(nested) {
        val job = jobOption.get
        job.uploadFolder  = uploadFolder
        job
      }
    }
  }

  def updateJobSegmentDuration(id : String, segmentDuration : Int) = {
    transactional{
      val jobOption = byId[EagleRecordJob](id)
      transactional(nested) {
        val job = jobOption.get
        job.segmentDuration = segmentDuration
      }
    }
  }

  def updateJobResolution(id : String, width : String, height : String) = {
    transactional{
      val jobOption = byId[EagleRecordJob](id)
      transactional(nested) {
        val job = jobOption.get
        job.width = width
        job.height = height
      }
    }
  }

  def updateEmbeddedPath(id : String, path : String) = {
    transactional{
      val jobOption = byId[EagleRecordJob](id)
      transactional(nested) {
        val job = jobOption.get
        job.finalOutPutPath = path
      }
    }
  }

  def updateJobStatus(id : String, finished : Boolean, callBackResponse : String) = {
    transactional{
      val jMap = new MutableEntityMap[EagleRecordJob]()
      jMap.put(_.finished)(finished)
      jMap.put(_.callBackResponse)(callBackResponse)
      jMap.tryUpdate(id)
    }
  }

  def updatePublishUrl(id : String, url : String) = {
    transactional{
      val jMap = new MutableEntityMap[EagleRecordJob]()
      jMap.put(_.publishUrl)(url)
      jMap.tryUpdate(id)
    }
  }

  def updateEmbedAds(id : String, status : Boolean) = {
    transactional{
      val jMap = new MutableEntityMap[EagleRecordJob]
      jMap.put(_.embedAdsSuccess)(status)
      jMap.tryUpdate(id)
    }
  }

  def getNotCleanedJobs = {
    transactional{
      val navigator = paginatedQuery{
        (entity: EagleRecordJob) =>
          where((entity.finished :== true) :&& (entity.cleaned :== false)) select(entity.id) orderBy(entity.programId)
      }.navigator(100)
      navigator
    }
  }

  def markJobAsCleaned(id : String) = {
    transactional{
      val jMap = new MutableEntityMap[EagleRecordJob]
      jMap.put(_.cleaned)(true)
      jMap.tryUpdate(id)
    }
  }

  def initialScaledAdCounter(id : String, num : Int) = {
    transactional{
      val jMap = new MutableEntityMap[EagleRecordJob]()
      jMap.put(_.scaledCounter)(num)
      jMap.tryUpdate(id)
    }
  }

  def increaseScaledAdCounter(id : String) = {
    transactional{
      val job = byId[EagleRecordJob](id)
      if(job.isDefined) {
        val jMap = new MutableEntityMap[EagleRecordJob]()
        jMap.put(_.scaledCounter)(job.get.scaledCounter + 1)
        jMap.tryUpdate(id)
        byId[EagleRecordJob](id)
      }else job
    }
  }

  def addScaledAd(id : String, adPath : String) = {
    transactional{
      val job = byId[EagleRecordJob](id)
      if(job.isDefined) {
        val jMap = new MutableEntityMap[EagleRecordJob]()
        jMap.put(_.scaledAdsPaths)(job.get.scaledAdsPaths :+ adPath)
        jMap.tryUpdate(id)
        byId[EagleRecordJob](id)
      }else job
    }
  }

  def updateNeededPercentage(id : String, num : Int) = {
    transactional{
      val jMap = new MutableEntityMap[EagleRecordJob]
      jMap.put(_.neededPercentage)(num)
      jMap.tryUpdate(id)
    }
  }

  def saveSegmentsList(id : String, segments : List[Int]) = {
    transactional{
      val jMap = new MutableEntityMap[EagleRecordJob]
      jMap.put(_.segmentList)(segments)
      jMap.tryUpdate(id)
    }
  }

  def saveCapturedSegFolders(id : String, segmentsFolders : List[String]) = {
    transactional{
      val jMap = new MutableEntityMap[EagleRecordJob]
      jMap.put(_.capturedSegFolders)(segmentsFolders)
      jMap.tryUpdate(id)
    }
  }
}
