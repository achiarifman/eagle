package com.eagle.dao

import net.fwbrasil.activate.ActivateContext
import net.fwbrasil.activate.serialization.{jsonSerializer, Serializer}
import net.fwbrasil.activate.storage.mongo.MongoStorage

/**
 * Created by Achia.Rifman on 13/09/2014.
 */
object persistanceContext extends ActivateContext{

  val storage = new MongoStorage {
    val host = "localhost"
    override val port = 27017
    val db = "eagle"
    //override val authentication = Option("local", "vidmind12")
  }

/*  override protected def customSerializers  = List(
    serialize[EagleJob](_.eagleRec) using jsonSerializer)*/
}
