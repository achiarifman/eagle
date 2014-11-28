import com.eagle.dao.entity.EagleJob
import net.fwbrasil.activate.ActivateContext
import net.fwbrasil.activate.storage.mongo.MongoStorage
/**
 * Created by Achia.Rifman on 13/09/2014.
 */
object persistanceContext extends ActivateContext {

  val storage = new MongoStorage {
    val host = "localhost"
    override val port = 27017
    val db = "activate"
    //override val authentication = Option("local", "vidmind12")
  }
}

import persistanceContext._

val t = transactional {
  val job = new EagleJob("test", "test")
  job
  //LOGGER.info("Going to persist new job " + job.id)
}
println(t.id)

