package com.eagle.dao

import com.eagle.dao.entity.EmbededeAd
import com.eagle.dao.persistanceContext._
import org.bson.types.ObjectId

/**
 * Created by Achia.Rifman on 28/03/2015.
 */
object AdDao {

  def persistAd( jobId : String,
   programId : String,
   startTime : String,
   endTime : String,
   cornertType : String) = {
    transactional{
      val ad = new EmbededeAd((new ObjectId).toString,jobId,programId,startTime,endTime,cornertType)
      ad
    }
  }
}
