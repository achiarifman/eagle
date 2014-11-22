package service

import javax.annotation.PostConstruct

import actor.ActorsManager
import akka.actor._
import com.eagle.entity.EagleRecordEntity
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.stereotype.Service
import util.EagleSpringProperties

/**
 * Created by Achia.Rifman on 12/09/2014.
 */
@Service
class ActorsService {


  val system = ActorSystem("eagle")


  val actorsManager = system.actorOf(Props(new ActorsManager()), "actorsManager")
  @Value("${record.output}") var OUTPUT_FOLDER: String = null
  @Value("${ftp.host.folder}") var hostDirectory: String = null
  @Value("${ftp.host}") var host: String = null
  @Value("${ftp.username}")  var userName: String = null
  @Value("${ftp.password}") var password: String = null


  @PostConstruct
  def afterInit(){
    EagleSpringProperties.host = host
    EagleSpringProperties.userName = userName
    EagleSpringProperties.password = password
    EagleSpringProperties.hostDirectory = hostDirectory
    EagleSpringProperties.OUTPUT_FOLDER = OUTPUT_FOLDER
  }


  def initialJob(eagleRecordEntity: EagleRecordEntity) {

    eagleRecordEntity.setOutputFolder(OUTPUT_FOLDER)
    println(OUTPUT_FOLDER)
    actorsManager ! eagleRecordEntity
  }
}
