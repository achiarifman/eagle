package actor.message

import spray.json._

/**
 * Created by Achia.Rifman on 30/01/2015.
 */
case class PrePublishMessage(override val id : String,
                             callBackUrl : String,
                             status : String,
                             numOfAds : Int,
                             mediaUrl : String,
                             programmeId : String
                              ) extends BaseMessage(id){

}

object PublishSerializer extends DefaultJsonProtocol {

  //implicit val publishFormat = jsonFormat6(PrePublishMessage)

  implicit  object PublishJsonFormat extends RootJsonFormat[PrePublishMessage]{
    def write(c: PrePublishMessage) =
      JsObject(
        "programmeId" -> JsString(c.programmeId),
        "status" -> JsString(c.status),
        "mediaUrl" -> JsString(c.mediaUrl),
        "numOfAds" -> JsNumber(c.numOfAds),
        "id" -> JsString(c.id)
      )

    def read(value: JsValue) = value match {

      case _ => deserializationError("TedMedia expected")

    }
  }
}

case class PostPublishMessage(override val id : String, override val success : Boolean, content : String ) extends BaseResultMessage(id,success) {

}
