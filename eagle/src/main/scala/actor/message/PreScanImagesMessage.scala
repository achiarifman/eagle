package actor.message

import com.eagle.dao.entity.{SegmentCandidates, ImageDiff}
import org.bson.types.ObjectId

/**
 * Created by Achia.Rifman on 28/11/2014.
 */
case class PreScanImagesMessage (override val id : String, val segmentsResultMap : Map[Int,List[ImageDiff]],val segments : List[Int])
  extends BaseMessage(id){

}

case class PostScanImagesMessage (override val id : String, override val success : Boolean, val segmentsCandidates : List[SegmentCandidates])
  extends BaseResultMessage(id,success){

}