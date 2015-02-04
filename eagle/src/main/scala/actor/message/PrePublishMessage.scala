package actor.message

/**
 * Created by Achia.Rifman on 30/01/2015.
 */
case class PrePublishMessage(override val id : String, callBackUrl : String, status : String, numOfAds : Int) extends BaseMessage(id){

}

case class PostPublishMessage(override val id : String, override val success : Boolean, content : String ) extends BaseResultMessage(id,success) {

}
