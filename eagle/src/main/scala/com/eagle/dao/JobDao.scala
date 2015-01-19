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

  def persistNewJob(eagleJob : EagleRecordEntity,actors : List[String]) = {
    val t = transactional {

      val job = new EagleRecordJob(new ObjectId().toString,eagleJob.getUrl,eagleJob.getDuration,
        eagleJob.getChannelName,actors,eagleJob.getAdsPaths.toList)
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
  def updateActorsList(id : String, finishedActors : List[String], waitingActors : List[String]) = {
    transactional{
      val jobOption =  byId[EagleRecordJob](id)
      transactional(nested) {
        val job = jobOption.get
          job.waitingActorList  = waitingActors
          job.finishedActorsList = job.finishedActorsList ::: finishedActors
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

  def updateJobResolution(id : String, width : Int, height : Int) = {
    transactional{
      val jobOption = byId[EagleRecordJob](id)
      transactional(nested) {
        val job = jobOption.get
        job.width = width
        job.height = height
      }
    }
  }

}
