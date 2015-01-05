package actor.message

import org.bson.types.ObjectId

/**
 * Created by Achia.Rifman on 27/12/2014.
 */
class PostRecordMessage(override val id : String, val outPutFile : String ) extends BaseMessage(id){

}
