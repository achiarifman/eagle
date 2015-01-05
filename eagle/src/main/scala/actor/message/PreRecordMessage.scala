package actor.message

import org.bson.types.ObjectId

/**
 * Created by Achia.Rifman on 27/12/2014.
 */
case class PreRecordMessage(override val id : String, url : String, duration : Int, channelName : String) extends BaseMessage(id){

}

case class InitAdMessage(override val id : String, adsPath : List[String], sourceFilePath : String, videoDuration : Int)
  extends BaseMessage(id) {

}
