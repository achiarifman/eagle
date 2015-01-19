package com.eagle.dao.entity

/**
 * Created by Achia.Rifman on 03/01/2015.
 */
case class ImageDiff(val id : Int,val  picOneId : Int,val PicTwoId : Int ,val leftUpCorner : Boolean, val rightUpCorner : Boolean,
                     val leftDownCorner : Boolean, val rightDownCorner : Boolean,val diffPath : String,val cornerWidth : Int,
                      val cornerHeight : Int) {

}
