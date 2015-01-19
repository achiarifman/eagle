package com.eagle.dao.entity

/**
 * Created by Achia.Rifman on 09/01/2015.
 */
case class SegmentCandidates(val segId : Int, val luCandidates : List[List[ImageDiff]], val ldCandidates : List[List[ImageDiff]], val ruCandidates : List[List[ImageDiff]],
                             val rdCandidates : List[List[ImageDiff]]) {

}
