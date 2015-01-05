package actor.message

import org.bson.types.ObjectId

/**
 * Created by Achia.Rifman on 28/11/2014.
 */
case class PreCaptureImageMessage(override val id : String, val adsList : List[String], val videoDuration : Int,
                                   videoPathToCapture : String) extends BaseMessage(id){

}

case class PostCaptureImageMessage(override val id : String,override val success : Boolean, val capturedSegFolders : List[String],
                                   segmentList : List[Int], segmentDuration : Int) extends BaseResultMessage(id,success){

}
