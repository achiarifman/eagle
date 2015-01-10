package actor.message

import com.eagle.dao.entity.{AdToEmbed, ImageDiff, SegmentCandidates}

/**
 * Created by Achia.Rifman on 09/01/2015.
 */
case class PreAdEmbederMessage(override val id : String,  matchedAdList : List[AdToEmbed], val sourceFilePath : String) extends BaseMessage(id){

}

case class PostAdEmbederMessage(override val id : String, override val success : Boolean, outputPath : String) extends BaseResultMessage(id,success){

}




