package com.eagle.dao.entity

import com.eagle.dao.persistanceContext._
import org.bson.types.ObjectId

/**
 * Created by Achia.Rifman on 28/11/2014.
 */
trait BaseEagleEntity extends Entity{


}
/*@Alias("jobs")
case class EagleJob(var eagleRec : EagleRecordJob) extends BaseEagleEntity {

}*/
/*
case class EagleJob(var subJobs : List[String], var enc : EncodeTest) extends BaseEagleEntity{
}
*/
abstract class BaseJob{

/*  var finished : Boolean
  var actorList : List[ActorJob]*/
}
@Alias("jobs")
class EagleRecordJob(val id : String, var recordUrl : String, var recordDuration : Int, var channelName : String,
                     var waitingActorList: List[String], val adsPath : List[String],val callBackUrl :String,
                      val programId : String)
  extends EntityWithCustomID[String] {
  var finished: Boolean = false
  var recordOutPutPath : String = null
  var finishedActorsList = List[String]()
  var uploadFolder : String = null
  var segmentDuration : Int = 0
  var width : String = "0"
  var height : String = "0"
  var finalOutPutPath : String = null
  var callBackResponse : String = null
  var publishUrl : String = null
  var embedAdsSuccess : Boolean = _
  var cleaned : Boolean = false
  var scaledAdsPaths = List[String]()
  var scaledCounter : Int = 0
  var neededPercentage : Int = 100
  var segmentList = List[Int]()
  var capturedSegFolders = List[String]()
}

class EagleAdJob(var sourceFilePath : String, var ImageOutPutFolder : String) extends BaseJob{
  /*override var finished: Boolean = false
  override var actorList: List[ActorJob] = _*/
}

class ActorJob(val actorName : String,var finished : Boolean) extends Serializable{


}

class EncodeTest(val x : Int, val s : String) extends Serializable {


}
/*
class MyEncoder extends Encoder[EncodeTest, Int] {

  def encode(value: EncodeTest) = value.
  def decode(x: Int) = new EncodeTest(x)

}*/
