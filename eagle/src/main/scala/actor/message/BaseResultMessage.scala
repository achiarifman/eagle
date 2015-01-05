package actor.message

import org.bson.types.ObjectId

/**
 * Created by Achia.Rifman on 27/12/2014.
 */
class BaseResultMessage(override val id : String, val success : Boolean) extends BaseMessage(id){

}
