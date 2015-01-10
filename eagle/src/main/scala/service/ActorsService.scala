package service

import javax.annotation.PostConstruct

import actor.ActorsManager
import actor.consts.ActorsTypes
import akka.actor._
import com.eagle.dao.entity.EagleRecordJob
import com.eagle.entity.EagleRecordEntity
import config.{PropsConst, EagleProps}
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.stereotype.Service
import util.EagleSpringProperties

/**
 * Created by Achia.Rifman on 12/09/2014.
 */

object ActorsService {


  val system = ActorSystem("eagle")
  val actorsManager = system.actorOf(Props(new ActorsManager()), ActorsTypes.ACTOR_MANAGER)
  val OUTPUT_FOLDER: String = EagleProps.config.getString(PropsConst.RECORD_OUTPUT)


  def initialJob(eagleRecordEntity: EagleRecordJob) {

    //eagleRecordEntity.setOutputFolder(OUTPUT_FOLDER)
   // println(OUTPUT_FOLDER)
    actorsManager ! eagleRecordEntity
  }
}
