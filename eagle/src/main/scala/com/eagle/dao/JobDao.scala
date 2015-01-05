package com.eagle.dao

import com.eagle.dao.entity.EagleRecordJob
import com.eagle.dao.persistanceContext._
/**
 * Created by Achia.Rifman on 27/12/2014.
 */
object JobDao {


  def updateRecordOutputPath(path : String, id : String) = {

    transactional{
     val job =  byId[EagleRecordJob](id)
      transactional(nested) {
        job.get.recordOutPutPath  = path
      }
    }
  }

  def getJobById(id : String) = {
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

  def saveUploadFolder(id : String, uploadFolder : String) = {
    transactional{
      val jobOption =  byId[EagleRecordJob](id)
      transactional(nested) {
        val job = jobOption.get
        job.uploadFolder  = uploadFolder
        job
      }
    }
  }
}
