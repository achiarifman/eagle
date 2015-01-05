package actor.message

import java.awt.image.BufferedImage

import org.bson.types.ObjectId

/**
 * Created by Achia.Rifman on 13/12/2014.
 */
case class ImagesDiffResultMessage(val id : ObjectId, val diffOutputs : List[Boolean]) {

}
