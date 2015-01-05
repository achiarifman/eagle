package actor.message

import java.nio.file.Path

import org.bson.types.ObjectId

/**
 * Created by Achia.Rifman on 27/12/2014.
 */
case class PreUploadMessage(override val id : String, val uploadPath : Path) extends BaseMessage(id){

}
