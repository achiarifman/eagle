package ffmpeg

import java.io.{File, OutputStream}
import java.nio.file.Paths

import config.{PropsConst, EagleProps}

import scala.concurrent.SyncVar

/**
 * Created by Achia.Rifman on 22/11/2014.
 */
class BaseFFmpeg(val outPutFolder : String, val outPutFolderName : String) {

  val FFMPEG =  EagleProps.config.getString(PropsConst.FFMPEG_PATH)
  val inputStream = new SyncVar[OutputStream]
  val stringBuilder: StringBuilder = new StringBuilder
  var isStarted = false
  var isFailed = false


  def appendPair(key: String, value: String) = {
    stringBuilder.append(key)
    stringBuilder.append(value)
  }

  def appendParam(param: String) = {
    stringBuilder.append(param)
  }

  def createOutputFolder(folderName: String) : String = {
    new File(folderName).mkdir
    Paths.get(folderName).toString
  }
}
