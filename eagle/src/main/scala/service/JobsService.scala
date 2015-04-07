package service

import actor.ActorsManager
import actor.consts.ActorsTypes
import akka.actor.{Props, ActorSystem}
import com.eagle.dao.JobDao
import com.eagle.entity.EagleRecordEntity
import com.eagle.dao.entity.{EagleRecordJob}
import com.eagle.dao.persistanceContext._
import config.{PropsConst, EagleProps}
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._

import scala.collection.mutable


/**
 * Created by Achia.Rifman on 28/11/2014.
 */
object JobsService {

  val LOGGER = LoggerFactory.getLogger(JobsService.getClass)
  val dropboxRelativePath = EagleProps.config.getString(PropsConst.DROPBOX_REALATIVE_PATH)
  val system = ActorSystem("eagle")
  val actorsManager = system.actorOf(Props(new ActorsManager()), ActorsTypes.ACTOR_MANAGER)


  def createAndPersistNewJob(eagleJob : EagleRecordEntity) =  {

    val job = JobDao.persistNewJob(eagleJob,getActorsList(eagleJob),getAdsPaths(eagleJob))
    initialJob(job)
    eagleJob.setId(new ObjectId(job.id))
    eagleJob
  }

  def getActorsList(eagleJob : EagleRecordEntity) = {
    val actorsList = mutable.MutableList[String]()
    if(eagleJob.getUrl.contains("http") || eagleJob.getUrl.contains("m3u8")){
      actorsList += ActorsTypes.RECORD_ACTOR
    }
    if(eagleJob.getAdsPaths != null && !eagleJob.getAdsPaths.isEmpty){
      actorsList += ActorsTypes.AD_ACTOR
    }
    actorsList += ActorsTypes.UPLOAD_ACTOR
    actorsList += ActorsTypes.PUBLISH_ACTOR
    actorsList.toList
  }

  def getAdsPaths(eagleJob : EagleRecordEntity) = {
    val adsFullPaths = eagleJob.getAdsPaths.map(ad => dropboxRelativePath + ad.replace("/","\\"))
    adsFullPaths.toList
  }

  def initialJob(eagleRecordEntity: EagleRecordJob) {

    actorsManager ! eagleRecordEntity
  }





}
