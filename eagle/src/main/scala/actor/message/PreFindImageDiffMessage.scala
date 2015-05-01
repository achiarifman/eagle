package actor.message

import akka.actor.Status.Success
import com.eagle.dao.entity.ImageDiff
import org.bson.types.ObjectId

/**
 * Created by Achia.Rifman on 13/12/2014.
 */
case class PreFindImageDiffMessage(override val id : String,
                                   val segments : List[Int],
                                   val capturedSegmentsFolder : List[String],
                                   val neededPercentage : Int )
  extends BaseMessage(id){

}

case class PostFindImageDiffMessage(override val id : String, override val success : Boolean ,val segmentsResultList : Map[Int,List[ImageDiff]],
                                    val segments : List[Int])
  extends BaseResultMessage(id, success){

}
