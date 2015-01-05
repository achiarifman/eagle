package actor.message

import org.bson.types.ObjectId

/**
 * Created by Achia.Rifman on 27/12/2014.
 */
case class PostUploadMessage(override val id : String, override val success : Boolean) extends BaseResultMessage(id,success){

}
