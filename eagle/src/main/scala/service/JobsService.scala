package service

import actor.consts.ActorsTypes
import com.eagle.entity.EagleRecordEntity
import com.eagle.dao.entity.{EagleRecordJob}
import com.eagle.dao.persistanceContext._
import config.EagleProps
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
  val test = EagleProps.config.getString("mongo.users.db")
  def createAndPersistNewJob(eagleJob : EagleRecordEntity) {
    println(test)
    //val eagleRecordJob = EagleRecordJob(eagleJob.getUrl,eagleJob.getDuration,eagleJob.getChannelName)
    val job = persistNewJob(eagleJob,getActorsList(eagleJob))
    val ent = getIt(job.id)
    println(ent.channelName)
    println(ent.recordUrl)
    ent.waitingActorList.foreach( f => println(f))
    ActorsService.initialJob(job)
  }

  def persistNewJob(eagleJob : EagleRecordEntity,actors : List[String]) = {
    val t = transactional {
        //val job = new EagleJob(List("first", "second", "third"), new EncodeTest(11, "yes"))
        val job = new EagleRecordJob(new ObjectId().toString,eagleJob.getUrl,eagleJob.getDuration,
          eagleJob.getChannelName,actors,eagleJob.getAdsPaths.toList)
        job
      }
    t
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
    actorsList.toList
  }

  def getIt(id : String ) : EagleRecordJob = {

    val t = transactional  {
      byId[EagleRecordJob](id)
    }
    t.get

  }



}
