package dao

import com.mongodb.ServerAddress
import com.mongodb.casbah.{MongoCredential, MongoClient}


/**
 * Created by Achia.Rifman on 13/09/2014.
 */
object persistanceContext {

  val server = new ServerAddress("localhost", 27017)
  val credentials = MongoCredential.createMongoCRCredential("local", "admin", "vidmind12".toCharArray)
  val mongoClient = MongoClient(server, List(credentials))


}
