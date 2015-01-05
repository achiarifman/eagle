package actor.message

import com.eagle.dao.entity.ImageDiff
import org.bson.types.ObjectId

/**
 * Created by Achia.Rifman on 28/11/2014.
 */
case class PreScanImagesMessage (override val id : String, val segmentsResultMap : Map[Int,List[ImageDiff]])
  extends BaseMessage(id){

}