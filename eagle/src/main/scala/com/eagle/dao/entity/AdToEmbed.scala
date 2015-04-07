package com.eagle.dao.entity

import com.eagle.dao.persistanceContext._

/**
 * Created by Achia.Rifman on 10/01/2015.
 */
case class AdToEmbed(val adPath :String, val startTime : Long, val endTime : Long, val cornerType : String, val cornerWidth : Int,
                      val cornerHeight : Int) {

}
@Alias("ads")
class EmbededeAd(val id : String, val jobId : String,
                      val programId : String,
                      val startTime : String,
                      val endTime : String,
                      val cornertType : String) extends EntityWithCustomID[String]
