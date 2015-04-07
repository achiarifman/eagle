package actor

import com.eagle.dao.entity.CallBackEntity
import dispatch.{Http, as, url}
import actor.message.{PostPublishMessage, PrePublishMessage}
import com.fasterxml.jackson.databind.ObjectMapper
import util.FileUtils
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
/**
 * Created by Achia.Rifman on 30/01/2015.
 */
class PublishActor extends AbstractActor with FileUtils{

  def receive = {
    case (message: PrePublishMessage) => {
      sendCallBack(message)
    }
   }

  def sendCallBack(message : PrePublishMessage) = {

    val theSender = sender
    val req = url(message.callBackUrl).setContentType("application/json", "UTF-8").addHeader("Accept","application/json; charset=UTF-8")
    val reqWithBody = req << buildBody(message)
    val response = Http(reqWithBody.POST OK as.String)
    response onComplete {
      case Success(content) => {
        println("Successful callback response" + content)
        theSender ! PostPublishMessage(message.id,true,content)
      }
      case Failure(ex) => {
        println("An error has occurred on callback: " + ex.getMessage)
        theSender ! PostPublishMessage(message.id,false,ex.getMessage)
      }
    }
  }

  def buildBody(message : PrePublishMessage) : String = {
    import spray.json._
    import actor.message.PublishSerializer._
    message.toJson.toString()
  }
}
