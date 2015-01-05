package actor.message

import org.bson.types.ObjectId

/**
 * Created by Achia.Rifman on 27/12/2014.
 */
case class RecordFailedMessage(override val id : String, failMessage : String) extends BaseMessage(id){

}
