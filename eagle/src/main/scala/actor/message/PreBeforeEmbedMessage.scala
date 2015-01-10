package actor.message

import com.eagle.dao.entity.{AdToEmbed, ImageDiff, SegmentCandidates}

/**
 * Created by Achia.Rifman on 09/01/2015.
 */
case class PreBeforeEmbedMessage(override val id : String, segmentsCandidates : List[SegmentCandidates], val sourceFilePath : String,
                                 val adsPath : List[String], segmentDuration : Int) extends BaseMessage(id){

}

case class PostBeforeEmbedMessage(override val id : String, override val success : Boolean, matchedAdList : List[AdToEmbed]) extends BaseResultMessage(id,success){

}
