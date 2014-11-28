package service

import com.eagle.entity.EagleRecordEntity
import com.eagle.dao.entity.{ActorJob, EagleJob, EncodeTest, EagleRecordJob}
import com.eagle.dao.persistanceContext._
import org.slf4j.LoggerFactory


/**
 * Created by Achia.Rifman on 28/11/2014.
 */
object JobsService {

  val LOGGER = LoggerFactory.getLogger(JobsService.getClass)

  def createAndPersistNewJob(eagleJob : EagleRecordEntity) {

    val eagleRecordJob = new EagleRecordJob(eagleJob.getUrl,eagleJob.getDuration,eagleJob.getChannelName)
    val actors = List(new ActorJob("actor1", false), new ActorJob("actor2", false), new ActorJob("actor3", false))
    eagleRecordJob.actorList = actors
    val t = transactional {
     //val job = new EagleJob(List("first", "second", "third"), new EncodeTest(11, "yes"))
      val job = new EagleJob(eagleRecordJob)
      job
   }
    val ent = getIt(t.id)
    println(ent.eagleRec.channelName)
    println(ent.eagleRec.recordUrl)
    ent.eagleRec.actorList.foreach( f => println(f.actorName))
    //println(ent.enc.s)
  }


  def getIt(id : String ) : EagleJob = {

    val t = transactional  {
      byId[EagleJob](id)
    }
    t.get

  }



}
